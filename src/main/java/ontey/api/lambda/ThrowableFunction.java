package ontey.api.lambda;

import java.util.function.Function;

/**
 * A {@link Function} that is allowed to throw any {@linkplain Throwable exception}
 */

@FunctionalInterface
public interface ThrowableFunction<T, R> extends ThrowingFunction<T, R, Throwable> {

}
