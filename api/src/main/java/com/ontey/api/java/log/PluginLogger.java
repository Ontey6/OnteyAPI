package com.ontey.api.java.log;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;

import java.util.logging.Logger;

@NullMarked
public class PluginLogger {
   
   private static final Logger LOGGER = Logger.getLogger("Minecraft");
   
   private final String prefix;
   
   @Setter @Getter
   private boolean debug = false;
   
   public PluginLogger(String name) {
      this.prefix = "[" + name + "]";
   }
   
   public void info(String... messages) {
      for(String message : messages)
         LOGGER.info(prefix + " " + message);
   }
   
   public void warn(String... messages) {
      for(String message : messages)
         LOGGER.warning(prefix + " " + message);
   }
   
   public void error(String... messages) {
      for(String message : messages)
         LOGGER.severe(prefix + " " + message);
   }
   
   public void debug(String... messages) {
      if(debug)
         for(String message : messages)
            LOGGER.info(prefix + " " + message);
   }
}
