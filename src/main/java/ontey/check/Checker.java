package ontey.check;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class used to check various conditions
 */

public class Checker {
   
   private Checker() {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Requires {@code expression} to be true, otherwise
    * throws an {@link IllegalArgumentException} with the
    * {@code errorMessage} String which is formatted using
    * {@link String#format String.format(errorMessage, arguments}.
    *
    * @param expression The expression that is required to be true
    * @param errorMessage The error message sent if the {@code expression} is false
    * @param arguments The arguments used to format the {@code errorMessage}
    */
   
   public static void checkArgument(boolean expression, @NonNull String errorMessage, @NonNull Object @NonNull ... arguments) {
      if(!expression)
         throw new IllegalArgumentException(errorMessage.formatted(arguments));
   }
   
   /**
    * Requires {@code expression} to be true, otherwise throws an
    * {@link IllegalArgumentException} with the {@code errorMessage} String.
    *
    * @param expression The expression that is required to be true
    * @param errorMessage The error message sent if the {@code expression} is false
    */
   
   public static void checkArgument(boolean expression, @NonNull String errorMessage) {
      if(!expression)
         throw new IllegalArgumentException(errorMessage);
   }
   
   /**
    * Requires {@code expression} to be true, otherwise throws an {@link IllegalArgumentException}.
    *
    * @param expression The expression that is required to be true
    */
   
   public static void checkArgument(boolean expression) {
      if(!expression)
         throw new IllegalArgumentException();
   }
   
   /**
    * Requires the input to be any number from {@code min} to {@code max}.
    * Otherwise, throws an {@link IndexOutOfBoundsException} with
    * the {@code errorMessage} String which is formatted using
    * {@link String#format String.format(errorMessage, arguments}.
    *
    * @param min The minimum
    * @param max The maximum
    * @param input The {@code int} to be tested
    * @param errorMessage The error message sent if the {@code expression} is false
    * @param arguments The arguments used to format the {@code errorMessage}
    */
   
   public static void checkBounds(int min, int max, int input, @NonNull String errorMessage, @NonNull Object @NonNull ... arguments) {
      checkArgument(min <= max, "Argument max has to be higher equal min!");
      
      if(input < min || input > max)
         throw new IndexOutOfBoundsException(errorMessage.formatted(arguments));
   }
   
   /**
    * Requires the input to be any number from {@code min} to {@code max}.
    * Otherwise, throws an {@link IndexOutOfBoundsException} with
    * the {@code errorMessage} String.
    *
    * @param min The minimum
    * @param max The maximum
    * @param input The {@code int} to be tested
    * @param errorMessage The error message sent if the {@code expression} is false
    */
   
   public static void checkBounds(int min, int max, int input, @NonNull String errorMessage) {
      checkArgument(min <= max, "Argument max has to be higher equal min!");
      
      if(input < min || input > max)
         throw new IndexOutOfBoundsException(errorMessage);
   }
   
   /**
    * Requires the input to be any number from {@code min} to {@code max}.
    * Otherwise, throws an {@link IndexOutOfBoundsException}.
    *
    * @param min The minimum
    * @param max The maximum
    * @param input The {@code int} to be tested
    */
   
   public static void checkBounds(int min, int max, int input) {
      checkArgument(min <= max, "Argument max has to be higher equal min!");
      
      if(input < min || input > max)
         throw new IndexOutOfBoundsException();
   }
   
   /**
    * Asserts that {@code obj} is not null.
    * Otherwise, throws an {@link NullPointerException} with
    * the {@code errorMessage} String which is formatted using
    * {@link String#format String.format(errorMessage, arguments}.
    *
    * @param obj The object tested for nullity
    * @param errorMessage The error message sent if the {@code expression} is false
    * @param arguments The arguments used to format the {@code errorMessage}
    * @return {@code obj}
    */
   
   public static <T> T checkNonNull(@Nullable T obj, @NonNull String errorMessage, @NonNull Object @NonNull ... arguments) {
      if(obj == null)
         throw new NullPointerException(errorMessage.formatted(arguments));
      
      return obj;
   }
   
   /**
    * Asserts that {@code obj} is not null.
    * Otherwise, throws an {@link NullPointerException} with
    * the {@code errorMessage} String.
    *
    * @param obj The object tested for nullity
    * @param errorMessage The error message sent if the {@code expression} is false
    * @return {@code obj}
    */
   
   public static <T> T checkNonNull(@Nullable T obj, @NonNull String errorMessage) {
      if(obj == null)
         throw new NullPointerException(errorMessage);
      
      return obj;
   }
   
   /**
    * Asserts that {@code obj} is not null.
    * Otherwise, throws an {@link NullPointerException}.
    *
    * @param obj The object tested for nullity
    * @return {@code obj}
    */
   
   public static <T> T checkNonNull(@Nullable T obj) {
      if(obj == null)
         throw new NullPointerException();
      
      return obj;
   }
}
