package ontey.file;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//TODO add documentation

@AllArgsConstructor(onConstructor_ = @ApiStatus.Internal)
public final class Files {
   
   private final JavaPlugin plugin;
   
   @NonNull
   public List<String> getStringList(@NonNull ConfigurationSection config, @NonNull String path) {
      return getStringList(config, path, new ArrayList<>(0));
   }
   
   @Contract("_, _, !null -> !null")
   public List<String> getStringList(@NonNull ConfigurationSection config, @NonNull String path, @Nullable List<String> def) {
      List<?> list = config.getList(path);
      if(list == null)
         return def;
      
      List<String> out = new ArrayList<>(list.size());
      for(Object obj : list)
         out.add(obj == null ? "" : obj.toString());
      
      return out;
   }
   
   @NonNull
   public List<@Nullable String> getListable(@NonNull ConfigurationSection config, @NonNull String path) {
      return getListable(config, path, new ArrayList<>(0));
   }
   
   @Contract("_, _, !null -> !null")
   public List<@Nullable String> getListable(@NonNull ConfigurationSection config, @NonNull String path, @Nullable List<@Nullable String> fallback) {
      if(!config.isSet(path))
         return fallback;
      
      return config.isString(path)
        ? singletonList(config.getString(path, ""))
        : getStringList(config, path);
   }
   
   // Weird that this is in here...
   @NonNull
   public <T> List<@Nullable T> singletonList(@Nullable T t) {
      List<T> out = new ArrayList<>(1);
      out.add(t);
      return out;
   }
   
   @Contract("_, _, !null -> !null")
   public <T> T getOrDefault(@NonNull ConfigurationSection config, @NonNull String path, @Nullable T fallback) {
      try {
         if(config.get(path) == null)
            return fallback;
         // noinspection unchecked
         return (T) config.get(path);
      } catch(ClassCastException e) {
         return fallback;
      }
   }
   
   @Nullable
   public String getMessage(@NonNull ConfigurationSection config, @NonNull String path) {
      return getMessage(config, path, null);
   }
   
   @Contract("_, _, !null -> !null")
   public String getMessage(@NonNull ConfigurationSection config, @NonNull String path, @Nullable String def) {
      if(config.isString(path))
         return config.getString(path);
      
      List<String> listable = getListable(config, path, null);
      
      if(listable == null)
         return def;
      
      return String.join("\n", getListable(config, path, null));
   }
   
   @NonNull
   public File[] getYamlFiles(@NonNull File dir) {
      File[] yamlFiles = dir.listFiles((d, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
      File[] dirs = dir.listFiles(File::isDirectory);
      
      if(dirs == null || dirs.length == 0)
         return yamlFiles != null ? yamlFiles : new File[0];
      
      File[] out = yamlFiles != null ? yamlFiles : new File[0];
      
      for(File directory : dirs)
         out = concatArrays(out, getYamlFiles(directory));
      
      return out;
   }
   
   @NonNull
   private <T> T[] concatArrays(@NonNull T[] first, @NonNull T[] second) {
      int len = first.length + second.length;
      T[] out = (T[]) new Object[len];
      
      System.arraycopy(first, 0, out, 0, first.length);
      System.arraycopy(second, 0, out, first.length, second.length);
      
      return out;
   }
}