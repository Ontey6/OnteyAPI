package com.ontey.api.plugin.variable;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class Variable extends SimpleVariable {
   public final VariableFormat format;
   
   public Variable(String name, VariableFormat format) {
      super(name);
      Objects.requireNonNull(format, "If you don't need a format, use a SimpleVariable");
      this.format = format;
   }
   
   public String format() {
      return format.format(name);
   }
}
