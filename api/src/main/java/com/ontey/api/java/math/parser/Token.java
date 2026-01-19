package com.ontey.api.java.math.parser;

import lombok.Getter;

@Getter
class Token {
   
   public enum Type {
       NUMBER,
       OPERATOR,
       VARIABLE,
       LEFT_PARENTHESES,
       RIGHT_PARENTHESES,
       FUNCTION_DELIMITER,
       END
   }
   
   private final Type type;
   private final String value;
   private final double numValue;
   
   public Token(Type type, String value) {
      this.type = type;
      this.value = value;
      this.numValue = 0;
   }
   
   public Token(Type type, double numValue) {
      this.type = type;
      this.value = String.valueOf(numValue);
      this.numValue = numValue;
   }
   
   @Override
   public String toString() {
      return type + "(" + value + ")";
   }
}
