package ontey.api.loader;

import ontey.api.classfinder.ClassFinder;
import ontey.api.classfinder.FinderDetails;

import java.util.function.Consumer;

//TODO Make a new, better Loader API
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class Loaders {
	
	public static <T> Runnable createSubclassLoader(FinderDetails details, Class<T> clazz, Consumer<Class<? extends T>> action) {
		return () -> {
			for(Class<? extends T> impl : ClassFinder.findSubClasses(details, clazz))
				if(impl.isAnnotationPresent(AutoRegistered.class))
					action.accept(impl);
		};
	}
}
