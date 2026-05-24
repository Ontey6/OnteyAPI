package ontey.api.lambda;

/**
 * A {@link Runnable} that is allowed to throw {@code <E>}.
 */

@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {
	
	void run() throws E;
}
