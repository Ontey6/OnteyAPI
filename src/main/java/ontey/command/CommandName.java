package ontey.command;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the name of a {@link CommandRegisterer} or {@link MiscCommandRegisterer}
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandName {
   
   /**
    * The name of this command
    */
   
   @NonNull
   String value();
}
