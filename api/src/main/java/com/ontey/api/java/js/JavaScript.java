package com.ontey.api.java.js;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * Represents a JavaScript script that can be compiled and run on runtime
 */

@NullMarked
public interface JavaScript {
   
   /**
    * Adds a variable to the JS global scope.
    */
   
   JavaScript addVariable(String name, @Nullable Object value);
   
   /**
    * Adds variables to the JS global scope in bulk.
    */
   
   JavaScript addVariables(Map<String, @Nullable Object> variables);
   
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
