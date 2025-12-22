package com.ontey.api.variable;

import java.util.Objects;

public class SimpleVariable {
   public final String name;
   
   public SimpleVariable(String name) {
      Objects.requireNonNull(name);
      this.name = name;
   }
}
