package ontey.loader;

import lombok.NonNull;
import ontey.plugin.OnteyPlugin;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SingleClassLoader<T> extends Loader {
   
   private final Class<T> clazz;
   
   private final Consumer<T> action;
   
   private final Set<ConstructionStrategy<T>> constructionStrategies;
   
   public SingleClassLoader(@NonNull Class<T> clazz, @NonNull Consumer<@NonNull T> action) {
      this.clazz = clazz;
      this.action = action;
      this.constructionStrategies = new HashSet<>(Set.of(
        new ConstructionStrategy<>(Constructor::newInstance)
      ));
   }
   
   public SingleClassLoader(@NonNull OnteyPlugin plugin, @NonNull Class<T> clazz, @NonNull Consumer<@NonNull T> action) {
      this.clazz = clazz;
      this.action = action;
      this.constructionStrategies = new HashSet<>(Set.of(
        new ConstructionStrategy<>(Constructor::newInstance),
        new ConstructionStrategy<>(c -> c.newInstance(plugin), OnteyPlugin.class)
      ));
   }
   
   @Override
   public void load() {
      action.accept(construct(clazz, constructionStrategies));
   }
}
