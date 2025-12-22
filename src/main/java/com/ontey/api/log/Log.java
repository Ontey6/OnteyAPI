package com.ontey.api.log;

public final class Log {
   public static PluginLogger of(String name) {
      return new PluginLogger(name);
   }
}