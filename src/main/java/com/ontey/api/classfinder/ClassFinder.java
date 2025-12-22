package com.ontey.api.classfinder;

import com.ontey.api.OnteyAPI;
import com.ontey.api.filelog.FileLog;
import com.ontey.api.plugin.OnteyPlugin;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {
   
   private static final String CLASS_SUFFIX = ".class";
   
   public static Set<Class<?>> find(JarFile source, String packageName, Class<? extends Annotation> annotation) {
      return find(source, clazz -> clazz.isAnnotationPresent(annotation) && clazz.getPackageName().startsWith(packageName));
   }
   
   public static Set<Class<?>> find(Class<?> source, String packageName, Class<? extends Annotation> annotation) {
      return find(source, clazz -> clazz.isAnnotationPresent(annotation) && clazz.getPackageName().startsWith(packageName));
   }
   
   public static Set<Class<?>> find(JarFile source, Class<? extends Annotation> annotation) {
      return find(source, clazz -> clazz.isAnnotationPresent(annotation));
   }
   
   public static Set<Class<?>> find(Class<?> source, Class<? extends Annotation> annotation) {
      return find(source, clazz -> clazz.isAnnotationPresent(annotation));
   }
   
   public static Set<Class<?>> find(JarFile source) {
      return find(source, clazz -> true);
   }
   
   public static Set<Class<?>> find(Class<?> source) {
      return find(source, clazz -> true);
   }
   
   /**
    * @param source Any class of your project so the jar file can be located.
    *               Best practice to use your {@code Main} class
    * @param requirements The requirements
    * @return All classes in the jar of the {@code source} that fit the requirement
    */
   
   public static Set<Class<?>> find(JarFile source, Function<Class<?>, Boolean> requirements) {
      Set<Class<?>> out = new HashSet<>();
      
      try(JarFile jar = source) {
         
         for(JarEntry entry : Collections.list(jar.entries())) {
            String entryName = entry.getName();
            
            if(!entryName.endsWith(CLASS_SUFFIX))
               continue;
            
            Class<?> cls = Class.forName(toClassName(entryName));
            
            if(requirements.apply(cls))
               out.add(cls);
         }
         
      } catch(MalformedURLException | ClassNotFoundException e) {
         OnteyPlugin.logger.error("Internal Error (probably): " + e.getClass().getSimpleName() + " in the ClassFilterer");
         FileLog.saveStackTrace(e);
      } catch(IOException e) {
         OnteyPlugin.logger.error("Encountered an IOException in ClassFilterer");
         FileLog.saveStackTrace(e);
      }
      
      return out;
   }
   
   public static Set<Class<?>> find(Class<?> source, Function<Class<?>, Boolean> requirements) {
      try {
         return find(getJar(source), requirements);
      } catch(URISyntaxException | IOException e) {
         OnteyPlugin.logger.error("Internal Error (probably): " + e.getClass().getSimpleName() + " in the ClassFilterer");
         FileLog.saveStackTrace(e);
      }
      
      return new HashSet<>(0);
   }
   
   public static Set<Class<?>> findWithClassloader(JarFile jarFile, ClassLoader loader, boolean initialize, Function<Class<?>, Boolean> requirements) {
      Set<Class<?>> out = new HashSet<>();
      
      try(jarFile) {
         Enumeration<JarEntry> entries = jarFile.entries();
         while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(entry.isDirectory() || !entry.getName().endsWith(CLASS_SUFFIX))
               continue;
            
            String className = toClassName(entry.getName());
            
            Class<?> clazz;
            try {
               clazz = Class.forName(className, initialize, loader);
            } catch(NoClassDefFoundError | ClassNotFoundException ignored) {
               continue;
            }
            
            if(requirements.apply(clazz))
               out.add(clazz);
         }
      } catch(IOException e) {
         throw new RuntimeException(e);
      }
      
      return out;
   }
   
   private static String toClassName(String entryName) {
      return entryName
        .replace('/', '.')
        .substring(0, entryName.length() - CLASS_SUFFIX.length());
   }
   
   private static JarFile getJar(Class<?> source) throws URISyntaxException, IOException {
      URL jarUrl = source.getProtectionDomain().getCodeSource().getLocation();
      JarURLConnection conn = (JarURLConnection) new URI("jar:" + jarUrl + "!/").toURL().openConnection();
      
      return conn.getJarFile();
   }
}
