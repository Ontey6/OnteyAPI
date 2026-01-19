package com.ontey.api.plugin.variable;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;

@AllArgsConstructor
@NullMarked
public class VariableFormat {
   public String format, replacementFormat;
   
   public String format(String name) {
      return format.replace(replacementFormat, name);
   }
}