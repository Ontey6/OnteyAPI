package ontey.command;

import lombok.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Specifies the name of a {@link CommandRegisterer} or {@link MiscCommandRegisterer}
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandName {
   
   /**
    * The name of this command
    */
   
   @NonNull
   String value();
}
