package ontey.api.lambda;

import java.util.function.Consumer;

/**
 * A {@link Consumer} that is allowed to throw any {@linkplain Throwable exception}
 */

@FunctionalInterface
public interface ThrowableConsumer<T> extends ThrowingConsumer<T, Throwable> {

}