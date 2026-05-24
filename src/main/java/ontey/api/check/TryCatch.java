package ontey.api.check;

import lombok.NonNull;
import ontey.api.lambda.ExceptionProvider;
import ontey.api.lambda.ThrowableRunnable;
import ontey.api.lambda.ThrowableSupplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

/**
 * A utility class for making clumsy try/catch statements smaller
 */

public class TryCatch {
	
	/**
	 * Wraps all thrown exceptions in the {@code runnable} in a specified subclass of {@link RuntimeException}.
	 *
	 * @param wrapperFunction Should return a {@link RuntimeException} in which {@linkplain Exception exceptions} that occur will be wrapped in. Usage: {@code RuntimeException::new}
	 * @param runnable The code to be executed
	 */
	
	public static void wrapExceptions(@NonNull ExceptionProvider wrapperFunction, @NonNull ThrowableRunnable runnable) {
		try {
			runnable.run();
		} catch(Throwable e) {
			throw wrapperFunction.apply(e);
		}
	}
	
	/**
	 * Wraps all thrown exceptions in the {@code supplier} in a specified subclass of {@link RuntimeException}.
	 * Also returns something.
	 *
	 * @param wrapperFunction Should return a {@link RuntimeException} in which {@linkplain Exception exceptions} that occur will be wrapped in. Usage: {@code RuntimeException::new}
	 * @param supplier The code to be executed and return value of this method
	 */
	
	public static <T> T wrapExceptions(@NonNull ExceptionProvider wrapperFunction, @NonNull ThrowableSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch(Throwable e) {
			throw wrapperFunction.apply(e);
		}
	}
	
	/**
	 * Wraps all thrown exceptions in the {@code runnable} in a {@link RuntimeException}.
	 *
	 * @param runnable The code to be executed
	 */
	
	public static void wrapExceptions(@NonNull ThrowableRunnable runnable) {
		wrapExceptions(RuntimeException::new, runnable);
	}
	
	/**
	 * Wraps all thrown exceptions in the {@code supplier} in a {@link RuntimeException}.
	 * Also returns something.
	 *
	 * @param supplier The code to be executed and return value of this method
	 */
	
	public static <T> T wrapExceptions(@NonNull ThrowableSupplier<T> supplier) {
		return wrapExceptions(RuntimeException::new, supplier);
	}
	
	/**
	 * Wraps all checked exceptions (All that don't extend {@link RuntimeException}) in the {@code runnable} in a specified subclass of {@link RuntimeException}.
	 *
	 * @param wrapperFunction Should return a {@link RuntimeException} in which {@linkplain Exception exceptions} that occur will be wrapped in. Usage: {@code RuntimeException::new}
	 * @param runnable The code to be executed
	 */
	
	public static void wrapCheckedExceptions(@NonNull ExceptionProvider wrapperFunction, @NonNull ThrowableRunnable runnable) {
		try {
			runnable.run();
		} catch(Throwable e) {
			if(e instanceof final RuntimeException re)
				throw re;
			
			throw wrapperFunction.apply(e);
		}
	}
	
	/**
	 * Wraps all checked exceptions (All that don't extend {@link RuntimeException}) in the {@code supplier} in a specified subclass of {@link RuntimeException}.
	 * Also returns something.
	 *
	 * @param wrapperFunction Should return a {@link RuntimeException} in which {@linkplain Exception exceptions} that occur will be wrapped in. Usage: {@code RuntimeException::new}
	 * @param supplier The code to be executed and return value of this method
	 */
	
	public static <T> T wrapCheckedExceptions(@NonNull ExceptionProvider wrapperFunction, @NonNull ThrowableSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch(Throwable e) {
			if(e instanceof final RuntimeException re)
				throw re;
			
			throw wrapperFunction.apply(e);
		}
	}
	
	/**
	 * Wraps all checked exceptions (All that don't extend {@link RuntimeException}) in the {@code runnable} in a {@link RuntimeException}.
	 *
	 * @param runnable The code to be executed
	 */
	
	public static void wrapCheckedExceptions(@NonNull ThrowableRunnable runnable) {
		wrapExceptions(RuntimeException::new, runnable);
	}
	
	/**
	 * Wraps checked exceptions (All that don't extend {@link RuntimeException}) in the {@code supplier} in a {@link RuntimeException}.
	 * Also returns something.
	 *
	 * @param supplier The code to be executed and return value of this method
	 */
	
	public static <T> T wrapCheckedExceptions(@NonNull ThrowableSupplier<T> supplier) {
		return wrapExceptions(RuntimeException::new, supplier);
	}
	
	/**
	 * Ignores all thrown exceptions in the {@code runnable}.
	 *
	 * @param runnable The code to be executed
	 */
	
	public static void ignoreExceptions(@NonNull ThrowableRunnable runnable) {
		try {
			runnable.run();
		} catch(Throwable ignored) {
		
		}
	}
	
	/**
	 * Ignores all thrown exceptions in the {@code supplier}.
	 * Also returns something.
	 *
	 * @param supplier The code to be executed and return value of this method
	 */
	
	@Contract("!null, _ -> !null")
	public static <T> T ignoreExceptions(@Nullable T def, @NonNull ThrowableSupplier<@Nullable T> supplier) {
		try {
			return supplier.get();
		} catch(Throwable ignored) {
			return def;
		}
	}
	
	/**
	 * Ignores all thrown exceptions in the {@code supplier}.
	 * Also returns something.
	 *
	 * @param supplier The code to be executed and return value of this method
	 */
	
	public static <T> Optional<T> ignoreExceptions(@NonNull ThrowableSupplier<@Nullable T> supplier) {
		try {
			return Optional.ofNullable(supplier.get());
		} catch(Throwable ignored) {
			return Optional.empty();
		}
	}
	
	/**
	 * Ignores all checked exceptions (All that don't extend {@link RuntimeException}) in the {@code runnable}.
	 *
	 * @param runnable The code to be executed
	 */
	
	public static void ignoreCheckedExceptions(@NonNull ThrowableRunnable runnable) throws RuntimeException {
		try {
			runnable.run();
		} catch(Throwable e) {
			if(e instanceof final RuntimeException re)
				throw re;
		}
	}
	
	/**
	 * Ignores all checked exceptions (All that don't extend {@link RuntimeException}) in the {@code supplier}.
	 * Also returns something.
	 *
	 * @param supplier The code to be executed and return value of this method
	 */
	
	@Contract("!null, _ -> !null")
	public static <T> T ignoreCheckedExceptions(@Nullable T def, @NonNull ThrowableSupplier<@Nullable T> supplier) throws RuntimeException {
		try {
			return supplier.get();
		} catch(Throwable e) {
			if(e instanceof final RuntimeException re)
				throw re;
			
			return def;
		}
	}
	
	/**
	 * Ignores all checked exceptions (All that don't extend {@link RuntimeException}) in the {@code supplier}.
	 * Also returns something.
	 *
	 * @param supplier The code to be executed and return value of this method
	 */
	
	public static <T> Optional<T> ignoreCheckedExceptions(@NonNull ThrowableSupplier<@Nullable T> supplier) throws RuntimeException {
		try {
			return Optional.ofNullable(supplier.get());
		} catch(Throwable e) {
			if(e instanceof final RuntimeException re)
				throw re;
			
			return Optional.empty();
		}
	}
	
	/**
	 * Ignores the {@code ignoredExceptions}.
	 * If an exception that is not in the {@code ignoredExceptions} and is not a {@link RuntimeException}
	 * is thrown, it is wrapped in a {@link RuntimeException} specified by the {@code wrapperFunction}.
	 *
	 * @param runnable The code to be executed
	 */
	
	public static void ignoreElseWrapExceptions(@NonNull Collection<@NonNull Class<? extends Throwable>> ignoredExceptions, @NonNull ExceptionProvider wrapperFunction, ThrowableRunnable runnable) throws RuntimeException {
		try {
			runnable.run();
		} catch(Throwable e) {
			if(!ignoredExceptions.contains(e.getClass()))
				throw wrapperFunction.apply(e);
			
			if(e instanceof final RuntimeException re)
				throw re;
			
			// else: do nothing
		}
	}
	
	/**
	 * Ignores the {@code ignoredExceptions}.
	 * If an exception that is not in the {@code ignoredExceptions} and is not a {@link RuntimeException}
	 * is thrown, it is wrapped in a {@link RuntimeException}.
	 *
	 * @param runnable The code to be executed
	 */
	
	public static void ignoreElseWrapExceptions(Collection<Class<? extends Throwable>> ignoredExceptions, ThrowableRunnable runnable) throws RuntimeException {
		ignoreElseWrapExceptions(ignoredExceptions, RuntimeException::new, runnable);
	}
	
	/**
	 * Ignores the {@code ignoredExceptions}.
	 * If an exception that is not in the {@code ignoredExceptions} and is not a {@link RuntimeException}
	 * is thrown, it is wrapped in a {@link RuntimeException} specified by the {@code wrapperFunction}.
	 *
	 * @param supplier The code to be executed
	 */
	
	public static <T> Optional<T> ignoreElseWrapExceptions(Collection<Class<? extends Throwable>> ignoredExceptions, ExceptionProvider wrapperFunction, ThrowableSupplier<T> supplier) throws RuntimeException {
		try {
			return Optional.ofNullable(supplier.get());
		} catch(Throwable e) {
			if(!ignoredExceptions.contains(e.getClass()))
				throw wrapperFunction.apply(e);
			
			if(e instanceof final RuntimeException re)
				throw re;
			
			return Optional.empty();
		}
	}
	
	/**
	 * Ignores the {@code ignoredExceptions}.
	 * If an exception that is not in the {@code ignoredExceptions} and is not a {@link RuntimeException}
	 * is thrown, it is wrapped in a {@link RuntimeException}.
	 *
	 * @param supplier The code to be executed
	 */
	
	public static <T> Optional<T> ignoreElseWrapExceptions(Collection<Class<? extends Throwable>> ignoredExceptions, ThrowableSupplier<T> supplier) throws RuntimeException {
		return ignoreElseWrapExceptions(ignoredExceptions, RuntimeException::new, supplier);
	}
}
