package ontey.api.lambda;

import java.util.function.Supplier;

/**
 * A {@link Supplier} that is allowed to throw {@code <E>}.
 */

@FunctionalInterface
public interface ThrowingSupplier<R, E extends Throwable> {
	
	R get() throws E;
}
