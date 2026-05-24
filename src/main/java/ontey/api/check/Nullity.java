package ontey.api.check;

import lombok.NonNull;
import ontey.api.lambda.ThrowingFunction;
import ontey.api.lambda.ThrowingSupplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * A class that provides methods about nullity
 * <br>
 * <b>Experimental</b>: names are not good, will be changed
 */

@ApiStatus.Experimental
public class Nullity {
	
	/**
	 * Checks {@code obj} for nullity. Never throws.
	 *
	 * @param obj The object to be tested for nullity
	 * @param def The default if {@code obj} is null
	 * @param <T> the return type. {@code obj} and {@code def} have to be this type.
	 * @return {@code obj} if {@code obj} is not null and {@code def} if {@code obj} is null.
	 */
	
	@Contract(value = "_, !null -> !null; !null, _ -> !null", pure = true)
	public static <T> T nonNullOr(@Nullable T obj, @Nullable T def) {
		return obj != null ? obj : def;
	}
	
	/**
	 * Checks {@code obj} for nullity and if non-null, returns the result of the converter.
	 * Otherwise, returns {@code def}.
	 * Replaces ternary like: {@code obj == null ? null : obj.getValue()} with {@code Nullity.nonNull(obj, obj::getValue, null)}
	 * Never throws.
	 *
	 * @param obj The object to be tested for nullity
	 * @param def The default if {@code obj} is null
	 * @param <T> the return type. {@code obj} and {@code def} have to be this type.
	 * @return {@code obj} if {@code obj} is not null and {@code def} if {@code obj} is null.
	 */
	
	@Contract(value = "null, _, null -> null; null, _, _ -> param3", pure = true)
	public static <T, O, E extends Throwable> O nonNullOr(@Nullable T obj, @NonNull ThrowingFunction<@NonNull T, @Nullable O, E> converter, @Nullable O def) throws E {
		return obj != null ? converter.apply(obj) : def;
	}
	
	/**
	 * Checks {@code obj} for nullity and if non-null, returns the result of the converter.
	 * Otherwise, returns {@code null}.
	 * Replaces ternary like: {@code obj == null ? null : obj.getValue()} with {@code Nullity.nonNull(obj, obj::getValue)}
	 * Never throws.
	 *
	 * @param obj The object to be tested for nullity
	 * @param <T> The input type.
	 * @param <O> The return type.
	 * @param converter Converts an object of the input type to one of the return type.
	 * @return {@code obj} if {@code obj} is not null and {@code def} if {@code obj} is null.
	 */
	
	@Contract(pure = true)
	public static <T, O, E extends Throwable> O nonNullOr(@Nullable T obj, @NonNull ThrowingFunction<@NonNull T, @Nullable O, E> converter) throws E {
		return nonNullOr(obj, converter, null);
	}
	
	/**
	 * Checks {@code obj} for nullity and if non-null, returns the result of the getter.
	 * Otherwise, returns {@code null}.
	 * Replaces ternary like: {@code obj == null ? null : obj.getValue()} with {@code Nullity.nonNull(obj, obj::getValue)}
	 * Never throws.
	 *
	 * @param obj The object to be tested for nullity
	 * @param <T> The return type. {@code obj} and {@code def} have to be this type.
	 * @return {@code obj} if {@code obj} is not null and {@code def} if {@code obj} is null.
	 */
	
	@Contract(value = "null, _, null -> null; null, _, _ -> param3", pure = true)
	public static <T, E extends Throwable> T nonNullOr(@Nullable T obj, @NonNull ThrowingSupplier<@Nullable T, E> getter, @Nullable T def) throws E {
		return obj != null ? getter.get() : def;
	}
	
	/**
	 * Checks {@code obj} for nullity and if non-null, returns the result of the getter.
	 * Otherwise, returns {@code null}.
	 * Replaces ternary like: {@code obj == null ? null : obj.getValue()} with {@code Nullity.nonNull(obj, obj::getValue)}
	 * Never throws.
	 *
	 * @param obj The object to be tested for nullity
	 * @param <T> the return type. {@code obj} and {@code def} have to be this type.
	 * @return {@code obj} if {@code obj} is not null and {@code def} if {@code obj} is null.
	 */
	
	@Contract(pure = true)
	public static <T, E extends Throwable> T nonNullOr(@Nullable T obj, @NonNull ThrowingSupplier<@Nullable T, E> getter) throws E {
		return nonNullOr(obj, getter, null);
	}
	
	/**
	 * Checks {@code obj} for nullity. Throws if it is null
	 *
	 * @param obj The object to be tested for nullity
	 * @param <T> the return type.
	 * @return {@code obj}.
	 */
	
	@Contract("null -> fail; !null -> param1")
	public static <T> T nonNull(@Nullable T obj) {
		if(obj == null)
			throw new NullPointerException();
		
		return obj;
	}
	
	/**
	 * Mainly a lambda util.
	 * Shortens {@code obj -> obj == null} to {@code Checker::isNull}
	 * and makes it more readable as there is no repeated {@code obj}.
	 *
	 * @return Whether {@code } obj is null
	 */
	
	@Contract(value = "null -> true; !null -> false", pure = true)
	public static boolean isNull(Object obj) {
		return obj == null;
	}
	
	/**
	 * Removes all null elements from {@link Iterable input}.
	 *
	 * @return {@link Iterable input} after all null values are removed.
	 */
	
	@NonNull
	@Contract(mutates = "param1")
	public static <@NonNull T, @NonNull O extends Iterable<T>> O filterNull(@NonNull O input) {
		var itr = input.iterator();
		while(itr.hasNext())
			if(isNull(itr.next()))
				itr.remove();
		
		return input;
	}
}
