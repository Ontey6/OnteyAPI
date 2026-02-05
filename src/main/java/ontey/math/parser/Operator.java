package ontey.math.parser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.BiFunction;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Operator {
   
   @Getter
   @NonNull
   private final String name;
   
   @Getter
   private final int precedence;
   
   @Getter
   private final boolean rightAssociative;
   
   @NonNull
   private final BiFunction<@NonNull Double, @NonNull Double, @NonNull Double> operation;
   
   @NonNull
   public static Operator of(@NonNull String name, int precedence, boolean rightAssociative, @NonNull BiFunction<@NonNull Double, @NonNull Double, @NonNull Double> operation) {
      return new Operator(name, precedence, rightAssociative, operation);
   }
   
   @NonNull
   public static Operator of(@NonNull String name, int precedence, @NonNull BiFunction<@NonNull Double, @NonNull Double, @NonNull Double> operation) {
      return of(name, precedence, false, operation);
   }
   
   @NonNull
   public static Operator of(@NonNull String name, @NonNull BiFunction<@NonNull Double, @NonNull Double, @NonNull Double> operation) {
      return of(name, 1, false, operation);
   }
   
   public double eval(double left, double right) {
      return operation.apply(left, right);
   }
}
