package ontey.config;

import static ontey.check.TryCatch.*;
import static org.bukkit.util.NumberConversions.*;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.*;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO add comment support

/**
 * Represents a section in a {@link Config}.
 * Is stored in memory.
 * Using default setter methods like {@link #set} will save the current config to its file.
 * This can be bypassed using the setters' soft variations like {@link #softSet}.
 */

public class ConfigSection {
   
   private static final char SEPARATOR = '.';
   private static final String SEPARATOR_PATTERN = "\\.";
   
   @NonNull
   protected final Map<String, SectionPathData> map = new LinkedHashMap<>();
   
   /**
    * The root of this ConfigSection
    */
   
   @NonNull
   @Getter
   private final Config root;
   
   /**
    * This ConfigSection's parent.
    * Might be null if this section is a {@link Config}.
    *
    * <blockquote>
    * <pre>{@code
    * section:
    *   this-section: {}
    * }</pre>
    * parent = {@code section}
    * </blockquote>
    */
   
   @Nullable
   @Getter
   private final ConfigSection parent;
   
   /**
    * The name of this section
    *
    * <blockquote>
    * <pre>{@code
    * section:
    *   this-section: {}
    * }</pre>
    * name = {@code this-section}
    * </blockquote>
    */
   
   @NonNull
   @Getter
   private final String name;
   
   /**
    * The path of this section
    *
    * <blockquote>
    * <pre>{@code
    * section:
    *   this-section: {}
    * }</pre>
    * path = {@code section.this-section}
    * </blockquote>
    */
   
   @NonNull
   @Getter
   private final String path;
   
   /**
    * The no-arg constructor for overriding classes.
    * Should only be used for that purpose.
    *
    * @throws IllegalArgumentException if not called in a subclasses' constructor as {@code super()}.
    */
   
   protected ConfigSection() {
      if(!(this instanceof Config config))
         throw new IllegalStateException("Cannot construct a root ConfigSection when not a Config");
      
      this.name = "";
      this.path = "";
      this.parent = null;
      this.root = config;
   }
   
   /**
    * Creates a new ConfigSection
    *
    * @param parent The parent of the created section. Can not be
    *               null as {@link #parent} can only be null when
    *               the section is a {@link Config} which needs to
    *               use the {@link ConfigSection#ConfigSection()
    *               no-arg constructor}.
    * @param name
    */
   
   protected ConfigSection(@NonNull ConfigSection parent, @NonNull String name) {
      this.name = name;
      this.parent = parent;
      this.root = parent.getRoot();
      this.path = createPath(parent, name);
   }
   
   /**
    * @param deep Whether to also find keys in nested sections
    *             <blockquote>
    *             <pre>{@code
    *             simple-scoreboard:
    *               aliases: [ssb]
    *               message: '<green>Yay a simple scoreboard!'
    *               commands:
    *                 - 'scoreboard objectives add $arg1'
    *                 - 'scoreboard objectives setdisplay sidebar'
    *                 - 'scoreboard players add $player $arg1 0'
    *             }</pre>
    *             not deep = {@code [simple-scoreboard]}, deep = {@code }
    *             </blockquote>
    * @return All keys in this section.
    */
   
   //TODO deep doesn't work.
   @NonNull
   public Set<String> getKeys(boolean deep) {
      Set<String> result = new LinkedHashSet<>();
      
      mapChildrenKeys(result, this, deep);
      
      return result;
   }
   
   //TODO deep doesn't work
   @NonNull
   public Map<String, Object> getValues(boolean deep) {
      Map<String, Object> result = new LinkedHashMap<>();
      
      mapChildrenValues(result, this, deep);
      
      return result;
   }
   
   public boolean contains(@NonNull String path) {
      return contains(path, false);
   }
   
   public boolean contains(@NonNull String path, boolean ignoreDefault) {
      return((ignoreDefault) ? get(path, null) : get(path)) != null;
   }
   
   public boolean isSet(@NonNull String path) {
      return get(path, null) != null;
   }
   
   public void set(@NonNull String path, @Nullable Object value) {
      softSet(path, value);
      root.save();
   }
   
   public void softSet(@NonNull String path, @Nullable Object value) {
      Preconditions.checkArgument(!path.isEmpty(), "Cannot set to an empty path");
      
      String[] parts = path.split(SEPARATOR_PATTERN);
      
      ConfigSection section = this;
      
      for(int i = 0; i < parts.length - 1; i++) {
         ConfigSection sub = section.getSection(parts[i]);
         
         if(sub == null) {
            if(value == null)
               return;
            section = section.createSection(parts[i]);
            continue;
         }
         
         section = sub;
      }
      
      String key = parts[parts.length - 1];
      if(section == this) {
         if(value == null) {
            map.remove(key);
         } else {
            SectionPathData entry = map.get(key);
            if(entry == null)
               map.put(key, new SectionPathData(value));
            else
               entry.setData(value);
         }
      } else {
         section.set(key, value);
      }
   }
   
   @Nullable
   public Object get(@NonNull String path) {
      return get(path, null);
   }
   
   @Contract("_, !null -> !null")
   @Nullable
   public Object get(@NonNull String path, @Nullable Object def) {
      if(path.isEmpty())
         return this;
      
      String[] parts = path.split(SEPARATOR_PATTERN);
      
      ConfigSection section = this;
      
      for(int i = 0; i < parts.length - 1; i++) {
         if(!section.contains(parts[i], true))
            return def;
         
         section = section.getSection(parts[i]);
         if(section == null)
            return def;
      }
      
      String key = parts[parts.length - 1];
      
      if(section == this) {
         SectionPathData result = map.get(key);
         return result == null ? def : result.getData();
      }
      
      return section.get(key, def);
   }
   
   @NonNull
   public ConfigSection createSection(@NonNull String path) {
      var sec = softCreateSection(path);
      root.save();
      return sec;
   }
   
   @NonNull
   public ConfigSection softCreateSection(@NonNull String path) {
      Preconditions.checkArgument(!path.isEmpty(), "Cannot create section at empty path");
      
      String[] parts = path.split(SEPARATOR_PATTERN);
      
      ConfigSection section = this;
      
      for(int i = 0; i < parts.length - 1; i++) {
         ConfigSection sub = section.getSection(parts[i]);
         section = sub == null ? section.softCreateSection(parts[i]) : sub;
      }
      
      String key = parts[parts.length - 1];
      
      if(section == this) {
         ConfigSection result = new ConfigSection(this, key);
         map.put(key, new SectionPathData(result));
         return result;
      }
      
      return section.softCreateSection(key);
   }
   
   @NonNull
   public ConfigSection createSection(@NonNull String path, @NonNull Map<String, Object> values) {
      var sec = softCreateSection(path, values);
      root.save();
      return sec;
   }
   
   @NonNull
   public ConfigSection softCreateSection(@NonNull String path, @NonNull Map<String, Object> values) {
      ConfigSection section = softCreateSection(path);
      
      values.forEach((key, value) -> {
         if(value instanceof Map<?, ?> m)
            section.createSection(key, toPathMap(m));
         else
            section.set(key, value);
      });
      
      return section;
   }
   
   private Map<String, Object> toPathMap(Map<?, ?> input) {
      Map<String, Object> out = new HashMap<>();
      
      input.forEach((k, v) -> out.put((String) k, v));
      
      return out;
   }
   
   // Primitives
   @Nullable
   public String getString(@NonNull String path) {
      return getString(path, null);
   }
   
   @Contract("_, !null -> !null")
   public String getString(@NonNull String path, @Nullable String def) {
      Object val = get(path, def);
      return val != null ? val.toString() : null;
   }
   
   public boolean isString(@NonNull String path) {
      return get(path) instanceof String;
   }
   
   public int getInt(@NonNull String path) {
      return getInt(path, 0);
   }
   
   public int getInt(@NonNull String path, int def) {
      Object val = get(path, def);
      return val instanceof Number ? toInt(val) : def;
   }
   
   public boolean isInt(@NonNull String path) {
      return get(path) instanceof Integer;
   }
   
   public boolean getBoolean(@NonNull String path) {
      return getBoolean(path, false);
   }
   
   public boolean getBoolean(@NonNull String path, boolean def) {
      Object val = get(path, def);
      return val instanceof Boolean ?(Boolean) val : def;
   }
   
   public boolean isBoolean(@NonNull String path) {
      return get(path) instanceof Boolean;
   }
   
   public double getDouble(@NonNull String path) {
      return getDouble(path, 0);
   }
   
   public double getDouble(@NonNull String path, double def) {
      Object val = get(path, def);
      return val instanceof Number ? toDouble(val) : def;
   }
   
   public boolean isDouble(@NonNull String path) {
      return get(path) instanceof Double;
   }
   
   public long getLong(@NonNull String path) {
      return getLong(path, 0);
   }
   
   public long getLong(@NonNull String path, long def) {
      Object val = get(path, def);
      return val instanceof Number ? toLong(val) : def;
   }
   
   public boolean isLong(@NonNull String path) {
      return get(path) instanceof Long;
   }
   
   // Java
   @NonNull
   public List<?> getList(@NonNull String path) {
      return getList(path, new ArrayList<>(0));
   }
   
   @Contract("_, !null -> !null")
   public List<?> getList(@NonNull String path, @Nullable List<?> def) {
      Object val = get(path, def);
      return(List<?>)(val instanceof List ? val : def);
   }
   
   public boolean isList(@NonNull String path) {
      Object val = get(path);
      return val instanceof List;
   }
   
   @NonNull
   public List<@Nullable String> getStringList(@NonNull String path) {
      List<?> list = getList(path);
      
      List<String> result = new ArrayList<>();
      
      for(Object obj : list)
         result.add(obj.toString());
      
      return result;
   }
   
   @NonNull
   public List<@Nullable Character> getCharacterList(@NonNull String path) {
      List<Character> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Character c -> c;
            case String str -> str.length() == 1 ? str.charAt(0) : null;
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<@Nullable Boolean> getBooleanList(@NonNull String path) {
      List<Boolean> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Boolean b -> b;
            case String str -> switch(str.toLowerCase()) {
               case "true" -> true;
               case "false" -> false;
               default -> null;
            };
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<@Nullable Double> getDoubleList(@NonNull String path) {
      List<Double> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Number num -> num.doubleValue();
            case String str -> ignoreExceptions(() -> Double.valueOf(str)).orElse(null);
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<@Nullable Float> getFloatList(@NonNull String path) {
      List<Float> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Number num -> num.floatValue();
            case String str -> ignoreExceptions(() -> Float.valueOf(str)).orElse(null);
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<@Nullable Byte> getByteList(@NonNull String path) {
      List<Byte> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Number num -> num.byteValue();
            case String str -> ignoreExceptions(() -> Byte.valueOf(str)).orElse(null);
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<@Nullable Short> getShortList(@NonNull String path) {
      List<Short> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Number num -> num.shortValue();
            case String str -> ignoreExceptions(() -> Short.valueOf(str)).orElse(null);
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<@Nullable Integer> getIntegerList(@NonNull String path) {
      List<Integer> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Number num -> num.intValue();
            case String str -> ignoreExceptions(() -> Integer.valueOf(str)).orElse(null);
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<@Nullable Long> getLongList(@NonNull String path) {
      List<Long> out = new ArrayList<>();
      
      for(Object obj : getList(path)) {
         out.add(switch(obj) {
            case Number num -> num.longValue();
            case String str -> ignoreExceptions(() -> Long.valueOf(str)).orElse(null);
            default -> null;
         });
      }
      
      return out;
   }
   
   @NonNull
   public List<Map<?, ?>> getMapList(@NonNull String path) {
      List<?> list = getList(path);
      List<Map<?, ?>> result = new ArrayList<>();
      
      for(Object object : list)
         if(object instanceof Map<?, ?> subsection)
            result.add(subsection);
      
      return result;
   }
   
   // Bukkit
   @Nullable
   public <T> T getObject(@NonNull String path, @NonNull Class<T> clazz) {
      return getObject(path, clazz, null);
   }
   
   @Contract("_, _, !null -> !null")
   public <T> T getObject(@NonNull String path, @NonNull Class<T> clazz, @Nullable T def) {
      Object val = get(path, def);
      return(clazz.isInstance(val)) ? clazz.cast(val) : def;
   }
   
   @Nullable
   public <T extends ConfigurationSerializable> T getSerializable(@NonNull String path, @NonNull Class<T> clazz) {
      return getObject(path, clazz);
   }
   
   @Contract("_, _, !null -> !null")
   public <T extends ConfigurationSerializable> T getSerializable(@NonNull String path, @NonNull Class<T> clazz, @Nullable T def) {
      return getObject(path, clazz, def);
   }
   
   @Nullable
   public Vector getVector(@NonNull String path) {
      return getSerializable(path, Vector.class);
   }
   
   @Contract("_, !null -> !null")
   public Vector getVector(@NonNull String path, @Nullable Vector def) {
      return getSerializable(path, Vector.class, def);
   }
   
   public boolean isVector(@NonNull String path) {
      return getSerializable(path, Vector.class) != null;
   }
   
   @Nullable
   public OfflinePlayer getOfflinePlayer(@NonNull String path) {
      return getSerializable(path, OfflinePlayer.class);
   }
   
   @Contract("_, !null -> !null")
   public OfflinePlayer getOfflinePlayer(@NonNull String path, @Nullable OfflinePlayer def) {
      return getSerializable(path, OfflinePlayer.class, def);
   }
   
   public boolean isOfflinePlayer(@NonNull String path) {
      return getSerializable(path, OfflinePlayer.class) != null;
   }
   
   @Nullable
   public PlayerProfile getPlayerProfile(@NonNull String path) {
      return getSerializable(path, PlayerProfile.class);
   }
   
   @Contract("_, !null -> !null")
   public PlayerProfile getPlayerProfile(@NonNull String path, @Nullable PlayerProfile def) {
      return getSerializable(path, PlayerProfile.class, def);
   }
   
   public boolean isPlayerProfile(@NonNull String path) {
      return getSerializable(path, PlayerProfile.class) != null;
   }
   
   @Nullable
   public ItemStack getItemStack(@NonNull String path) {
      return getSerializable(path, ItemStack.class);
   }
   
   @Contract("_, !null -> !null")
   public ItemStack getItemStack(@NonNull String path, @Nullable ItemStack def) {
      return getSerializable(path, ItemStack.class, def);
   }
   
   public boolean isItemStack(@NonNull String path) {
      return getSerializable(path, ItemStack.class) != null;
   }
   
   @Nullable
   public Color getColor(@NonNull String path) {
      return getSerializable(path, Color.class);
   }
   
   @Contract("_, !null -> !null")
   @Nullable
   public Color getColor(@NonNull String path, @Nullable Color def) {
      return getSerializable(path, Color.class, def);
   }
   
   public boolean isColor(@NonNull String path) {
      return getSerializable(path, Color.class) != null;
   }
   
   @Nullable
   public Location getLocation(@NonNull String path) {
      return getSerializable(path, Location.class);
   }
   
   @Contract("_, !null -> !null")
   @Nullable
   public Location getLocation(@NonNull String path, @Nullable Location def) {
      return getSerializable(path, Location.class, def);
   }
   
   public boolean isLocation(@NonNull String path) {
      return getSerializable(path, Location.class) != null;
   }
   
   @Nullable
   public ConfigSection getSection(@NonNull String path) {
      Object val = get(path, null);
      if(val != null)
         return val instanceof ConfigSection ? (ConfigSection) val : null;
      
      val = get(path, null);
      return val instanceof ConfigSection ? createSection(path) : null;
   }
   
   public boolean isConfigSection(@NonNull String path) {
      Object val = get(path);
      return val instanceof ConfigSection;
   }
   
   protected boolean isPrimitiveWrapper(@Nullable Object input) {
      return input instanceof Integer || input instanceof Boolean
        || input instanceof Character || input instanceof Byte
        || input instanceof Short || input instanceof Double
        || input instanceof Long || input instanceof Float;
   }
   
   protected void mapChildrenKeys(@NonNull Set<String> output, @NonNull ConfigSection section, boolean deep) {
      for(var entry : section.map.entrySet()) {
         output.add(createPath(section, entry.getKey(), this));
         
         if(deep && (entry.getValue().getData() instanceof ConfigSection subsection))
            mapChildrenKeys(output, subsection, true);
      }
   }
   
   protected void mapChildrenValues(@NonNull Map<String, Object> output, @NonNull ConfigSection section, boolean deep) {
      for(var entry : section.map.entrySet()) {
         // Because of the copyDefaults call potentially copying out of order, we must remove and then add in our saved order
         // This means that default values we haven't set end up getting placed first
         // See SPIGOT-4558 for an example using spigot.yml - watch subsections move around to default order
         String childPath = createPath(section, entry.getKey(), this);
         output.remove(childPath);
         output.put(childPath, entry.getValue().getData());
         
         if(deep && entry.getValue().getData() instanceof ConfigSection sec)
            mapChildrenValues(output, sec, true);
      }
   }
   
   @NonNull
   protected static String createPath(@NonNull ConfigSection section, @Nullable String key) {
      return createPath(section, key, section.getRoot());
   }
   
   @NonNull
   public static String createPath(@NonNull ConfigSection section, @Nullable String key, @Nullable ConfigSection relativeTo) {
      StringBuilder builder = new StringBuilder();
      for(ConfigSection parent = section; parent != null && parent != relativeTo; parent = parent.getParent()) {
         if(!builder.isEmpty())
            builder.insert(0, SEPARATOR);
         
         builder.insert(0, parent.getName());
      }
      
      if(key != null && !key.isEmpty()) {
         if(!builder.isEmpty())
            builder.append(SEPARATOR);
         
         builder.append(key);
      }
      
      return builder.toString();
   }
   
   @NotNull
   public List<String> getComments(@NotNull final String path) {
      final SectionPathData pathData = getSectionPathData(path);
      return pathData == null ? Collections.emptyList() : pathData.getComments();
   }
   
   @NotNull
   public List<String> getInlineComments(@NotNull final String path) {
      final SectionPathData pathData = getSectionPathData(path);
      return pathData == null ? Collections.emptyList() : pathData.getInLineComments();
   }
   
   public void setComments(@NotNull final String path, @Nullable final List<String> comments) {
      final SectionPathData pathData = getSectionPathData(path);
      if (pathData != null) {
         pathData.setComments(comments);
      }
   }
   
   public void setInlineComments(@NotNull final String path, @Nullable final List<String> comments) {
      final SectionPathData pathData = getSectionPathData(path);
      if (pathData != null) {
         pathData.setInLineComments(comments);
      }
   }
   
   @Nullable
   private SectionPathData getSectionPathData(@NotNull String path) {
      // i1 is the leading (higher) index
      // i2 is the trailing (lower) index
      int i1 = -1, i2;
      ConfigSection section = this;
      while((i1 = path.indexOf(SEPARATOR, i2 = i1 + 1)) != -1) {
         section = section.getSection(path.substring(i2, i1));
         if(section == null)
            return null;
      }
      
      String key = path.substring(i2);
      if(section == this)
         return map.get(key);
      else
         return section.getSectionPathData(key);
   }
   
   public String toString() {
      return Strings.lenientFormat(
        "%s[path='%s']", getClass().getSimpleName(), path
      );
   }
}
