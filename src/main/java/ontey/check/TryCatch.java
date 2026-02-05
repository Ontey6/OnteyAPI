package ontey.check;

import lombok.NonNull;
import ontey.lambda.ExceptionWrapper;
import ontey.lambda.ThrowingRunnable;
import ontey.lambda.ThrowingSupplier;
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
   
   public static void wrapExceptions(@NonNull ExceptionWrapper wrapperFunction, @NonNull ThrowingRunnable runnable) {
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
   
   public static <T> T wrapExceptions(@NonNull ExceptionWrapper wrapperFunction, @NonNull ThrowingSupplier<T> supplier) {
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
   
   public static void wrapExceptions(@NonNull ThrowingRunnable runnable) {
      wrapExceptions(RuntimeException::new, runnable);
   }
   
   /**
    * Wraps all thrown exceptions in the {@code supplier} in a {@link RuntimeException}.
    * Also returns something.
    *
    * @param supplier The code to be executed and return value of this method
    */
   
   public static <T> T wrapExceptions(@NonNull ThrowingSupplier<T> supplier) {
      return wrapExceptions(RuntimeException::new, supplier);
   }
   
   /**
    * Wraps all checked exceptions (All that don't extend {@link RuntimeException}) in the {@code runnable} in a specified subclass of {@link RuntimeException}.
    *
    * @param wrapperFunction Should return a {@link RuntimeException} in which {@linkplain Exception exceptions} that occur will be wrapped in. Usage: {@code RuntimeException::new}
    * @param runnable The code to be executed
    */
   
   public static void wrapCheckedExceptions(@NonNull ExceptionWrapper wrapperFunction, @NonNull ThrowingRunnable runnable) {
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
   
   public static <T> T wrapCheckedExceptions(@NonNull ExceptionWrapper wrapperFunction, @NonNull ThrowingSupplier<T> supplier) {
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
   
   public static void wrapCheckedExceptions(@NonNull ThrowingRunnable runnable) {
      wrapExceptions(RuntimeException::new, runnable);
   }
   
   /**
    * Wraps checked exceptions (All that don't extend {@link RuntimeException}) in the {@code supplier} in a {@link RuntimeException}.
    * Also returns something.
    *
    * @param supplier The code to be executed and return value of this method
    */
   
   public static <T> T wrapCheckedExceptions(@NonNull ThrowingSupplier<T> supplier) {
      return wrapExceptions(RuntimeException::new, supplier);
   }
   
   /**
    * Ignores all thrown exceptions in the {@code runnable}.
    *
    * @param runnable The code to be executed
    */
   
   public static void ignoreExceptions(@NonNull ThrowingRunnable runnable) {
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
   public static <T> T ignoreExceptions(@Nullable T def, @NonNull ThrowingSupplier<@Nullable T> supplier) {
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
   
   public static <T> Optional<T> ignoreExceptions(@NonNull ThrowingSupplier<@Nullable T> supplier) {
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
   
   public static void ignoreCheckedExceptions(@NonNull ThrowingRunnable runnable) throws RuntimeException {
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
   public static <T> T ignoreCheckedExceptions(@Nullable T def, @NonNull ThrowingSupplier<@Nullable T> supplier) throws RuntimeException {
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
   
   public static <T> Optional<T> ignoreCheckedExceptions(@NonNull ThrowingSupplier<@Nullable T> supplier) throws RuntimeException {
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
   
   public static void ignoreElseWrapExceptions(@NonNull Collection<@NonNull Class<? extends Throwable>> ignoredExceptions, @NonNull ExceptionWrapper wrapperFunction, ThrowingRunnable runnable) throws RuntimeException {
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
   
   public static void ignoreElseWrapExceptions(Collection<Class<? extends Throwable>> ignoredExceptions, ThrowingRunnable runnable) throws RuntimeException {
      ignoreElseWrapExceptions(ignoredExceptions, RuntimeException::new, runnable);
   }
   
   /**
    * Ignores the {@code ignoredExceptions}.
    * If an exception that is not in the {@code ignoredExceptions} and is not a {@link RuntimeException}
    * is thrown, it is wrapped in a {@link RuntimeException} specified by the {@code wrapperFunction}.
    *
    * @param supplier The code to be executed
    */
   
   public static <T> Optional<T> ignoreElseWrapExceptions(Collection<Class<? extends Throwable>> ignoredExceptions, ExceptionWrapper wrapperFunction, ThrowingSupplier<T> supplier) throws RuntimeException {
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
   
   public static <T> Optional<T> ignoreElseWrapExceptions(Collection<Class<? extends Throwable>> ignoredExceptions, ThrowingSupplier<T> supplier) throws RuntimeException {
      return ignoreElseWrapExceptions(ignoredExceptions, RuntimeException::new, supplier);
   }
}
