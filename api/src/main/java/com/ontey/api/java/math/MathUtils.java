package com.ontey.api.java.math;

import org.jetbrains.annotations.ApiStatus;

public class MathUtils {
   
   public static double add(double x, double y) {
      return x + y;
   }
   
   public static double subtract(double x, double y) {
      return x - y;
   }
   
   public static double multiply(double x, double y) {
      return x * y;
   }
   
   public static double divide(double x, double y) {
      return x / y;
   }
   
   public static double modulo(double x, double y) {
      return x % y;
   }
   
   public static double pow(double x, double y) {
      return Math.pow(x, y);
   }
   
   @ApiStatus.Experimental
   private static double factorial(double x) {
      //return x!;
      return 0;
   }
}
