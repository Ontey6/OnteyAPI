package ontey.js;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a JavaScript script that can be compiled and run on runtime
 */

public interface JavaScript {
   
   /**
    * Adds a variable to the JS global scope.
    */
   
   @NonNull
   JavaScript addVariable(@NonNull String name, @Nullable Object value);
   
   /**
    * Adds variables to the JS global scope in bulk.
    */
   
   @NonNull
   JavaScript addVariables(@NonNull Map<String, @Nullable Object> variables);
   
   default JavaScript addUtils() {
      return addVariables(JavaScriptUtils.getStatics());
   }
   
   /**
    * Executes the JavaScript code.
    */
   
   @Nullable
   Object execute() throws JavaScriptException;
   
   /**
    * Executes the JavaScript code without checked exceptions.
    */
   
   @Nullable
   Object softExecute();
}
