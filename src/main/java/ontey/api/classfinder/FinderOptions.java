package ontey.api.classfinder;

import lombok.Builder;
import lombok.NonNull;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Extra criteria for a {@link ClassFinder} operation that classes.
 * These have to apply to in order to be included in the result.
 */

@Builder
public final class FinderOptions {
	
	/**
	 * The default options
	 */
	
	public static final FinderOptions DEFAULT = FinderOptions.builder().build();
	
	/**
	 * If non-null, only gets classes that are nested in that package. the classes package name needs to start with this package
	 */
	
	@Nullable
	@Pattern("[a-zA-Z_][a-zA-Z0-9_]+?(\\.[a-zA-Z_][a-zA-Z0-9_]+?)+?")
	// first any text, then an arbitrary amount of "." and then any text
	public final String packageName;
	
	/**
	 * Whether to initialize classes.
	 * Initializing calls the classes {@code static} block and loads static fields.
	 */
	
	@Builder.Default
	public final boolean initialize = false;
	
	/**
	 * Whether to include anonymous classes.
	 * If false, they will be skipped completely.
	 */
	
	@Builder.Default
	public final boolean includeAnonymous = false;
	
	/**
	 * An additional condition that has to be true
	 */
	
	@Builder.Default
	@NonNull
	public final Predicate<Class<?>> addition = _ -> true;
	
	/**
	 * @return The conditions for a class to be included in the result.
	 */
	
	@NonNull
	public Predicate<Class<?>> predicate() {
		return clazz ->
		  (packageName == null || clazz.getPackageName().startsWith(packageName))
			 && initialize
			 && includeAnonymous
			 && addition.test(clazz);
	}
}
