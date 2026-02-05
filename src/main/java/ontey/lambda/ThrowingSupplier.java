package ontey.lambda;

import java.util.function.Supplier;

/**
 * A {@link Supplier} that is allowed to throw any {@linkplain Throwable exception}
 */

@FunctionalInterface
public interface ThrowingSupplier<T> {
   T get() throws Throwable;
}
