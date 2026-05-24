package ontey.api.lambda;

/**
 * A {@link Runnable} that is allowed to throw any {@linkplain Throwable exception}
 */

@FunctionalInterface
public interface ThrowableRunnable extends ThrowingRunnable<Throwable> {

}
