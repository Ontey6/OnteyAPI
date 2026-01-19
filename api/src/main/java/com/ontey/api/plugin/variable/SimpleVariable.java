package com.ontey.api.plugin.variable;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class SimpleVariable {
   public final String name;
   
   public SimpleVariable(String name) {
      Objects.requireNonNull(name);
      this.name = name;
   }
}
