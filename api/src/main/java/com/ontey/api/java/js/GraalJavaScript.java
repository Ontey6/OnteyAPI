package com.ontey.api.java.js;

import com.google.common.base.Preconditions;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * To use, add these libraries to either your jar as an {@code implementation} or add it to {@code libraries} in your {@code plugin.yml}.
 * Find the latest version on <a href="https://mvnrepository.com/artifact/org.graalvm.js/js">Maven</a> (both versions of {@code js} and {@code sdk} are the same)
 * <pre>{@code
 * org.graalvm.js:js-language:<VERSION>
 * org.graalvm.sdk:graal-sdk:<VERSION>
 * }</pre>
 */

@NullMarked
public class GraalJavaScript implements JavaScript {
   
   private final Context context;
   
   private final String code;
   
   public GraalJavaScript(String line, Map<String, @Nullable Object> variables) {
      Objects.requireNonNull(line, "line must not be null");
      Objects.requireNonNull(variables, "variables must not be null");
      
      this.code = line;
      
      this.context = Context.newBuilder("js")
        .allowAllAccess(true)
        .build();
      
      variables.forEach(this::addVariable);
   }
   
   public GraalJavaScript(String line) {
      this(line, Map.of());
   }
   
   public GraalJavaScript(File javaScriptFile, Map<String, @Nullable Object> variables) {
      Objects.requireNonNull(javaScriptFile, "javaScriptFile must not be null");
      Objects.requireNonNull(variables, "variables must not be null");
      
      Preconditions.checkArgument(javaScriptFile.exists(), "javaScriptFile must exist");
      Preconditions.checkArgument(javaScriptFile.isFile(), "javaScriptFile must be a file");
      Preconditions.checkArgument(javaScriptFile.getName().endsWith(".js"), "javaScriptFile must be a js file");
      
      try {
         this.code = String.join("\n", Files.readAllLines(javaScriptFile.toPath()));
      } catch(IOException e) {
         //FileLog.saveStackTrace(e);
         e.printStackTrace();
         throw new RuntimeException(e);
      }
      
      this.context = Context.newBuilder("js")
        .allowAllAccess(true)
        .build();
      
      variables.forEach(this::addVariable);
   }
   
   public GraalJavaScript(File javaScriptFile) {
      this(javaScriptFile, Map.of());
   }
   
   public GraalJavaScript addVariable(String name, @Nullable Object value) {
      Objects.requireNonNull(name, "name");
      
      context.getBindings("js").putMember(name, value);
      return this;
   }
   
   public GraalJavaScript addVariables(Map<String, @Nullable Object> variables) {
      Objects.requireNonNull(variables, "variables");
      
      variables.forEach(this::addVariable);
      return this;
   }
   
   @Nullable
   public Object execute() throws JavaScriptException {
      try {
         Value result = context.eval("js", code);
         return result.isNull() ? null : result.as(Object.class);
      } catch(PolyglotException e) {
         throw new JavaScriptException(e);
      }
   }
   
   @Nullable
   public Object softExecute() {
      try {
         return execute();
      } catch(JavaScriptException e) {
         throw new RuntimeException(e);
      }
   }
}