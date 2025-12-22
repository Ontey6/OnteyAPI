package com.ontey.api.variable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VariableFormat {
   public String format, replacementFormat;
   
   public String format(String name) {
      return format.replace(replacementFormat, name);
   }
}