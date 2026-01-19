package com.ontey.api.java.log;

import org.jspecify.annotations.NullMarked;

public final class Log {
   
   @NullMarked
   public static PluginLogger of(String name) {
      return new PluginLogger(name);
   }
}