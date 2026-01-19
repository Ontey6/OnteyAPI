package com.ontey.api.java.math.parser;

import lombok.*;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NullMarked
public class Variable {
   
   private final String name;
   
   @Setter
   private double value;
   
   public static Variable of(String name, double value) {
      Objects.requireNonNull(name, "name");
      
      return new Variable(name, value);
   }
}
