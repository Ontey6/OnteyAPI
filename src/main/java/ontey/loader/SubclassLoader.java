package ontey.loader;

import lombok.NonNull;
import ontey.classfinder.ClassFinder;
import ontey.classfinder.FinderDetails;
import ontey.lambda.ThrowingFunction;
import ontey.plugin.OnteyPlugin;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;

/**
 * Can create subclasses of {@link #superClass} with customized
 * Constructors and then run an {@link #action} with the created object.
 *
 * @param <T> The type of the {@link #superClass}.
 */

public class SubclassLoader<T> extends Loader {
   
   private final FinderDetails details;
   
   private final Class<T> superClass;
   
   private final Consumer<T> action;
   
   private final Set<ConstructionStrategy<T>> constructionStrategies;
   
   /**
    * Constructs a new Loader.
    *
    * @param plugin Used for allowing constructors to use the plugin.
    * @param details Needed to find the classes using the {@link ClassFinder}
    * @param superClass The class to find/create subclasses of
    * @param action The action to run on every created object
    */
   
   public SubclassLoader(@NonNull OnteyPlugin plugin, @NonNull FinderDetails details, @NonNull Class<T> superClass, @NonNull Consumer<T> action) {
      this(details, superClass, action);
      
      addResolver(c -> c.newInstance(plugin), OnteyPlugin.class);
   }
   
   /**
    * Constructs a new Loader.
    *
    * @param details Needed to find the classes using the {@link ClassFinder}
    * @param superClass The class to find/create subclasses of
    * @param action The action to run on every created object
    */
   
   public SubclassLoader(@NonNull FinderDetails details, @NonNull Class<T> superClass, @NonNull Consumer<T> action) {
      this.details = details;
      this.superClass = superClass;
      this.action = action;
      this.constructionStrategies = new HashSet<>(Set.of(
        new ConstructionStrategy<>(Constructor::newInstance)
      ));
   }
   
   /**
    * Adds a constructor addition.
    *
    * @param function The function used to create the class
    * @param params The params the constructor needs to have.
    *               Should match the function's {@link Constructor#newInstance} call's params.
    */
   
   public SubclassLoader<T> addResolver(@NonNull ThrowingFunction<Constructor<T>, T> function, @NonNull Class<?>... params) {
      constructionStrategies.add(new ConstructionStrategy<>(function, params));
      return this;
   }
   
   /**
    * Adds a constructor addition.
    *
    * @param arguments The arguments that the new instance is created with.
    */
   
   public SubclassLoader<T> addResolver(@NonNull Object... arguments) {
      Class<?>[] params = new Class[arguments.length];
      
      for(int i = 0; i < arguments.length; i++)
         params[i] = arguments[i].getClass();
      
      return addResolver(c -> c.newInstance(arguments), params);
   }
   
   /**
    * Loads this loader.
    * <h1>Finding the classes</h1>
    * First finds the subclasses of {@link #superClass} using the {@link ClassFinder}.
    * Skips classes that are annotated with {@link NotLoaded}.
    * <h1>Object Construction</h1>
    * Then creates a new object using {@link #construct}.
    * There has to be at least one valid constructor, otherwise throws an {@link IllegalStateException}.
    * <h2>With Preferred Constructor</h2>
    * Constructors annotated with {@link PreferredConstructor} will gain precedence.
    * If that constructor is not valid (is not a construction addition of this loader), it will throw an {@link IllegalStateException}.
    * If multiple constructors are annotated, also throws an {@link IllegalStateException}.
    *
    * @throws IllegalStateException <ul>If:</ul>
    *                               <li>There is no valid constructor</li>
    *                               <li>There are multiple preferred constructors</li>
    *                               <li>The preferred constructor is not valid</li>
    */
   
   public void load() {
      for(Class<? extends T> clazz : ClassFinder.findSubClasses(details, superClass)) {
         if(clazz.isAnnotationPresent(NotLoaded.class))
            continue;
         
         action.accept(construct(clazz, constructionStrategies));
      }
   }
}
