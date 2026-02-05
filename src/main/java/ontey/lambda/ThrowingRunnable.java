package ontey.lambda;

/**
 * A {@link Runnable} that is allowed to throw any {@linkplain Throwable exception}
 */

@FunctionalInterface
public interface ThrowingRunnable {
   void run() throws Throwable;
}
