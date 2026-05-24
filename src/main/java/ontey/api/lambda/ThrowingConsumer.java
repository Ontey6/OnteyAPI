package ontey.api.lambda;

import java.util.function.Consumer;

/**
 * A {@link Consumer} that is allowed to throw {@code <E>}.
 */

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {
	
	void accept(T t) throws E;
}
