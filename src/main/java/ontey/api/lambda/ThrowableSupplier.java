package ontey.api.lambda;

import java.util.function.Supplier;

/**
 * A {@link Supplier} that is allowed to throw any {@linkplain Throwable exception}
 */

@FunctionalInterface
public interface ThrowableSupplier<R> extends ThrowingSupplier<R, Throwable> {

}
