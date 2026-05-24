package ontey.api.lambda;

import java.util.function.Function;

/**
 * A {@link Function} that is allowed to throw {@code <E>}.
 */

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
	
	R apply(T t) throws E;
}
