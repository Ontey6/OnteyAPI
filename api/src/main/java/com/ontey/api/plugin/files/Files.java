package com.ontey.api.plugin.files;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO add documentation

@NullMarked
public final class Files {
   
   public static List<String> getStringList(ConfigurationSection config, String path) {
      return getStringList(config, path, new ArrayList<>(0));
   }
   
   @Contract("_, _, !null -> !null")
   public static @UnknownNullability List<String> getStringList(ConfigurationSection config, String path, @Nullable List<String> def) {
      Objects.requireNonNull(config, "config");
      Objects.requireNonNull(path, "path");
      
      List<? extends @Nullable Object> list = config.getList(path);
      if(list == null)
         return def;
      
      List<String> out = new ArrayList<>(list.size());
      for(Object obj : list)
         out.add(obj == null ? "" : obj.toString());
      
      return out;
   }
   
   public static List<@Nullable String> getListable(ConfigurationSection config, String path) {
      return getListable(config, path, new ArrayList<>(0));
   }
   
   @Contract("_, _, !null -> !null")
   public static @UnknownNullability List<@Nullable String> getListable(ConfigurationSection config, String path, @Nullable List<@Nullable String> fallback) {
      Objects.requireNonNull(config, "config");
      Objects.requireNonNull(path, "path");
      
      if(!config.isSet(path))
         return fallback;
      return config.isString(path)
        ? singletonList(config.getString(path, ""))
        : getStringList(config, path);
   }
   
   // Weird that this is in here...
   public static <T> List<@Nullable T> singletonList(@Nullable T t) {
      List<@Nullable T> out = new ArrayList<>(1);
      out.add(t);
      return out;
   }
   
   @Contract("_, _, !null -> !null")
   public static @UnknownNullability <T> T getOrDefault(ConfigurationSection config, String path, @Nullable T fallback) {
      Objects.requireNonNull(config, "config");
      Objects.requireNonNull(path, "path");
      
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
   public static String getMessage(ConfigurationSection config, String path) {
      return getMessage(config, path, null);
   }
   
   @Contract("_, _, !null -> !null")
   public static @Nullable String getMessage(ConfigurationSection config, String path, @Nullable String def) {
      Objects.requireNonNull(config, "config");
      Objects.requireNonNull(path, "path");
      
      if(config.isString(path))
         return config.getString(path);
      
      List<@Nullable String> listable = Files.getListable(config, path, null);
      
      if(listable == null)
         return def;
      
      return String.join("\n", Files.getListable(config, path, null));
   }
   
   private static File[] getYamlFiles(File dir) {
      Objects.requireNonNull(dir, "dir");
      
      File[] yamlFiles = dir.listFiles((d, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
      File[] dirs = dir.listFiles(File::isDirectory);
      
      if(dirs == null || dirs.length == 0)
         return yamlFiles != null ? yamlFiles : new File[0];
      
      File[] out = yamlFiles != null ? yamlFiles : new File[0];
      
      for(File directory : dirs)
         out = concatArrays(out, getYamlFiles(directory));
      
      return out;
   }
   
   private static <T> T[] concatArrays(T[] first, T[] second) {
      int len = first.length + second.length;
      T[] out = (T[]) new Object[len];
      
      System.arraycopy(first, 0, out, 0, first.length);
      System.arraycopy(second, 0, out, first.length, second.length);
      
      return out;
   }
}