package com.ontey.api.files;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Files {
   
   public static List<String> getList(ConfigurationSection config, String path) {
      List<?> list = config.getList(path);
      if (list == null || list.isEmpty())
         return new ArrayList<>();
      
      List<String> out = new ArrayList<>();
      for (Object obj : list)
         out.add(obj == null ? "" : obj.toString());
      return out;
   }
   
   public static List<String> getListable(ConfigurationSection config, String path) {
      return config.isSet(path) && !config.isList(path)
        ? singletonList(config.getString(path, ""))
        : getList(config, path);
   }
   
   public static List<String> getListable(ConfigurationSection config, String path, List<String> fallback) {
      if(!config.isSet(path))
         return fallback;
      return config.isString(path)
        ? singletonList(config.getString(path, ""))
        : getList(config, path);
   }
   
   private static <T> List<T> singletonList(T t) {
      List<T> out = new ArrayList<>(1);
      out.add(t);
      return out;
   }
   
   @Contract("_, _, !null -> !null")
   public static <T> T getOrDefault(ConfigurationSection config, String path, T fallback) {
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
   public static String getMessage(ConfigurationSection config, String path, String def) {
      List<String> listable = Files.getListable(config, path, null);
      
      if(listable == null)
         return def;
      
      return String.join("\n", Files.getListable(config, path, null));
   }
   
   private static File[] getYamlFiles(File dir) {
      File[] yamlFiles = dir.listFiles((d, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
      File[] dirs = dir.listFiles(File::isDirectory);
      
      if(dirs == null || dirs.length == 0)
         return yamlFiles != null ? yamlFiles : new File[0];
      
      File[] out = yamlFiles != null ? yamlFiles : new File[0];
      
      for(File directory : dirs)
         out = concat(out, getYamlFiles(directory));
      
      return out;
   }
   
   private static <T> T[] concat(T[] first, T[] second) {
      int len = first.length + second.length;
      T[] out = (T[]) new Object[len];
      
      System.arraycopy(first, 0, out, 0, first.length);
      System.arraycopy(second, 0, out, first.length, second.length);
      
      return out;
   }
}