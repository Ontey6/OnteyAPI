package ontey.loader;

import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Loads class(es) based on different criteria.
 * Usually constructs them.
 */

public abstract class Loader {
   
   public abstract void load();
   
   protected <T> T construct(@NonNull Class<? extends T> clazz, Set<ConstructionStrategy<T>> constructionStrategies) {
      Constructor<?> preferred = null;
      ConstructionStrategy<T> preferredAddition = null;
      Map<Constructor<?>, ConstructionStrategy<T>> validConstructors = new HashMap<>();
      
      for(var c : clazz.getConstructors()) {
         boolean addedCheck = false;
         boolean isPreferred = c.isAnnotationPresent(PreferredConstructor.class);
         
         for(var addition : constructionStrategies) {
            if(Arrays.equals(c.getParameterTypes(), addition.params())) {
               validConstructors.put(c, addition);
               if(isPreferred) {
                  if(preferred != null)
                     throw new IllegalStateException("More than one @PreferredConstructor constructors in " + clazz);
                  preferred = c;
                  preferredAddition = addition;
                  addedCheck = true;
               }
            }
         }
         
         if(!addedCheck && isPreferred)
            throw new IllegalStateException("A constructor annotated with @PreferredConstructor is not valid in " + clazz);
      }
      
      if(preferred == null) {
         if(validConstructors.isEmpty())
            throw new IllegalStateException("No valid constructor in " + clazz);
         
         var entry = getFirst(validConstructors);
         
         preferred = entry.getKey();
         preferredAddition = entry.getValue();
      }
      
      try {
         return preferredAddition.function().apply((Constructor<T>) preferred);
      } catch(Throwable e) {
         throw new RuntimeException(e);
      }
   }
   
   private static <T, R> Map.Entry<T, R> getFirst(Map<T, R> map) {
      return map.entrySet().iterator().next();
   }
}
