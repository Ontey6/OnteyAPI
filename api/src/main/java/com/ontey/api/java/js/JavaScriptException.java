package com.ontey.api.java.js;

/**
 * A unified {@link Exception} that combines all
 */

public class JavaScriptException extends Exception {
   
   @java.io.Serial
   private static final long serialVersionUID = -5342524326765752343L;
   
   public JavaScriptException() {
      super();
   }
   
   public JavaScriptException(Throwable cause) {
      super(cause);
   }
   
   public JavaScriptException(String message,  Throwable cause) {
      super(message, cause);
   }
   
   public JavaScriptException(String message) {
      super(message);
   }
}
