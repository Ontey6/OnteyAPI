package ontey.classfinder;

import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A util to find classes in {@link JarFile JarFiles}.
 * By default, doesn't load classes, so if a class extends a class that doesn't exist on runtime, that is not a problem.
 */

public class ClassFinder {
   
   private static final String CLASS_SUFFIX = ".class";
   
   /**
    * @return A {@linkplain Set set} of all {@linkplain Class classes} In the {@code detail}'s {@link JarFile JAR}.
    */
   
   @NonNull
   public static Set<@NonNull Class<?>> findAll(@NonNull FinderDetails details) {
      return find(details, clazz -> true);
   }
   
   /**
    * @return A {@linkplain Set set} of all {@linkplain Class classes} In the {@code detail}'s {@link JarFile JAR} that are annotated with the {@code annotation}.
    */
   
   @NonNull
   public static Set<@NonNull Class<?>> findAnnotated(@NonNull FinderDetails details, @NonNull FinderOptions options, @NonNull Class<? extends Annotation> annotation) {
      return find(details, options.predicate().and(clazz -> clazz.isAnnotationPresent(annotation)));
   }
   
   /**
    * @return A {@linkplain Set set} of all {@linkplain Class classes} In the {@code detail}'s {@link JarFile JAR} that are annotated with the {@code annotation}.
    */
   
   @NonNull
   public static Set<@NonNull Class<?>> findAnnotated(@NonNull FinderDetails details, @NonNull Class<? extends Annotation> annotation) {
      return find(details, clazz -> clazz.isAnnotationPresent(annotation));
   }
   
   /**
    * @return A {@linkplain Set set} of all {@linkplain Class classes} In the {@code detail}'s {@link JarFile JAR} that are subclasses of {@code cls}
    */
   
   @NonNull
   public static <T> Set<@NonNull Class<? extends T>> findSubClasses(@NonNull FinderDetails details, @NonNull FinderOptions options, Class<T> cls) {
      return findSubClasses(details, options.predicate().and(clazz -> cls.isAssignableFrom(clazz) && !cls.equals(clazz)));
   }
   
   /**
    * @return A {@linkplain Set set} of all {@linkplain Class classes} In the {@code detail}'s {@link JarFile JAR} that are subclasses of {@code cls}
    */
   
   @NonNull
   public static <T> Set<@NonNull Class<? extends T>> findSubClasses(@NonNull FinderDetails details, @NonNull Class<T> cls) {
      return findSubClasses(details, clazz -> cls.isAssignableFrom(clazz) && !cls.equals(clazz));
   }
   
   /**
    * @return A {@linkplain Set set} of all {@linkplain Class classes} In the {@code detail}'s {@link JarFile JAR} that fit the requirements.
    */
   
   @NonNull
   public static Set<@NonNull Class<?>> find(@NonNull FinderDetails details, @NonNull FinderOptions options, @NonNull Predicate<@NonNull Class<?>> requirements) {
      Set<Class<?>> out = new HashSet<>();
      
      //noinspection resource
      Enumeration<JarEntry> entries = details.source().entries();
      while(entries.hasMoreElements()) {
         JarEntry entry = entries.nextElement();
         if(entry.isDirectory() || !entry.getName().endsWith(CLASS_SUFFIX))
            continue;
         
         String className = toClassName(entry.getName());
         
         Class<?> clazz;
         try {
            clazz = Class.forName(className, options.initialize, details.loader());
         } catch(NoClassDefFoundError | ClassNotFoundException e) {
            continue;
         }
         
         if(!options.includeAnonymous && clazz.isAnonymousClass())
            continue;
         
         if(requirements.test(clazz))
            out.add(clazz);
      }
      
      return out;
   }
   
   /**
    * @return A {@linkplain Set set} of all {@linkplain Class classes} In the {@code detail}'s {@link JarFile JAR} that fit the requirements.
    */
   
   public static Set<@NonNull Class<?>> find(@NonNull FinderDetails details, @NonNull Predicate<@NonNull Class<?>> requirements) {
      return find(details, FinderOptions.DEFAULT, requirements);
   }
   
   //
   // Utils
   //
   
   /**
    * <strong>Internal</strong>.
    * Finds all classes that match the {@code requirements}, casts all to the required type and returns a {@linkplain Set set} of subclasses of {@code <T>}.
    */
   
   @SuppressWarnings("unchecked")
   private static <T> Set<Class<? extends T>> findSubClasses(FinderDetails details, Predicate<Class<?>> requirements) {
      var ts = find(details, requirements);
      Set<Class<? extends T>> out = new HashSet<>(ts.size());
      
      for(Class<?> t : ts)
         out.add((Class<? extends T>) t);
      
      return out;
   }
   
   /**
    * Makes a file-path, e.g. {@code ontey/classfinder/ClassFinder.java} into
    * a Class name {@code ontey.classfinder.ClassFinder}.
    *
    * @return A class name that - if the filePath points to a class - can
    *         be made into one using {@link Class#forName(String) Class.forName}.
    */
   
   private static String toClassName(String filePath) {
      return filePath
        .replace('/', '.')
        .substring(0, filePath.length() - CLASS_SUFFIX.length());
   }
}
