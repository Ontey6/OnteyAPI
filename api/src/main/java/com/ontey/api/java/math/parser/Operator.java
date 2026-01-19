package com.ontey.api.java.math.parser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiFunction;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NullMarked
public class Operator {
   
   @Getter
   private final String name;
   
   @Getter
   private final int precedence;
   
   @Getter
   private final boolean rightAssociative;
   
   private final BiFunction<Double, Double, Double> operation;
   
   public static Operator of(String name, int precedence, boolean rightAssociative, BiFunction<Double, Double, Double> operation) {
      return new Operator(name, precedence, rightAssociative, operation);
   }
   
   public static Operator of(String name, int precedence, BiFunction<Double, Double, Double> operation) {
      return of(name, precedence, false, operation);
   }
   
   public static Operator of(String name, BiFunction<Double, Double, Double> operation) {
      return of(name, 1, false, operation);
   }
   
   public double eval(double left, double right) {
      return operation.apply(left, right);
   }
}
