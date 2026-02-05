package ontey.log;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.logging.Logger;

public class NamedLogger {
   
   private static final Logger LOGGER = Logger.getLogger("Minecraft");
   
   @NonNull
   private final String prefix;
   
   @Setter
   @Getter
   private boolean debug = false;
   
   public NamedLogger(@NonNull String name) {
      this.prefix = "[" + name + "]";
   }
   
   public void info(@NonNull String @NonNull ... messages) {
      for(String message : messages)
         LOGGER.info(prefix + " " + message);
   }
   
   public void warn(@NonNull String @NonNull ... messages) {
      for(String message : messages)
         LOGGER.warning(prefix + " " + message);
   }
   
   public void error(@NonNull String @NonNull ... messages) {
      for(String message : messages)
         LOGGER.severe(prefix + " " + message);
   }
   
   public void debug(@NonNull String @NonNull ... messages) {
      if(debug)
         for(String message : messages)
            LOGGER.info(prefix + " " + message);
   }
}
