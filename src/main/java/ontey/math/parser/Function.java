package ontey.math.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class Function {
   
   @Getter
   @NonNull
   private final String name;
   
   @Getter
   private final int arity;
   
   @NonNull
   private final java.util.function.Function<double @NonNull [], @NonNull Double> operation;
   
   ///**
   // * TODO
   // * Format for function: add(x; y; z) = x + y + z
   // * @param function
   // * @return
   // */
   //
   //public static Function fromString(String function) {
   //   return null;
   //}
   
   public double apply(double @NonNull ... args) {
      if(args.length != arity)
         throw new MathParserException("Function " + name + " expects " + arity + " arguments, got " + args.length);
      
      return operation.apply(args);
   }
   
   @Override
   public String toString() {
      return name + "(" + arity + " args)";
   }
}
