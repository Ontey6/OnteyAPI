package com.ontey.api.java.js;

import com.google.common.base.Preconditions;
import com.ontey.api.OnteyAPI;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * To use, add these libraries to either your jar as an {@code implementation} or add it to {@code libraries} in your {@code plugin.yml}.
 * Find the latest version on <a href="https://mvnrepository.com/artifact/org.openjdk.nashorn/nashorn-core">Maven</a>
 * <pre>{@code
 * org.openjdk.nashorn:nashorn-core:<VERSION>
 * }</pre>
 */

@NullMarked
public class NashornJavaScript implements JavaScript {
   
   private static final ScriptEngineManager manager = new ScriptEngineManager();
   
   private static final ScriptEngineFactory engineFactory;
   
   private final ScriptEngine engine;
   
   private final Bindings variables;
   
   private final String code;
   
   public NashornJavaScript(String line, Map<@Nullable String, Object> variables) {
      Objects.requireNonNull(line, "line must not be null");
      Objects.requireNonNull(variables, "bindings must not be null");
      
      this.code = line;
      
      manager.registerEngineName("Nashorn", engineFactory);
      this.engine = manager.getEngineByName("Nashorn");
      
      this.variables = engine.getBindings(ScriptContext.ENGINE_SCOPE);
      this.variables.clear();
      this.variables.putAll(variables);
   }
   
   public NashornJavaScript(String line) {
      this(line, Map.of());
   }
   
   public NashornJavaScript(File javaScriptFile, Map<@Nullable String, Object> variables) {
      Objects.requireNonNull(javaScriptFile, "javaScriptFile must not be null");
      Objects.requireNonNull(variables, "bindings must not be null");
      Preconditions.checkArgument(javaScriptFile.exists(), "javaScriptFile must exist");
      Preconditions.checkArgument(javaScriptFile.isFile(), "javaScriptFile must be a file");
      Preconditions.checkArgument(javaScriptFile.getName().endsWith(".js"), "javaScriptFile must be a js file");
      
      manager.registerEngineName("Nashorn", engineFactory);
      this.engine = manager.getEngineByName("Nashorn");
      this.variables = engine.getBindings(ScriptContext.ENGINE_SCOPE);
      this.variables.clear();
      this.variables.putAll(variables);
      try {
         this.code = String.join("\n", Files.readAllLines(javaScriptFile.toPath()));
      } catch(IOException e) {
         //FileLog.saveStackTrace(e);
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }
   
   public NashornJavaScript(File javaScriptFile) {
      this(javaScriptFile, Map.of());
   }
   
   static {
      try {
         engineFactory = (ScriptEngineFactory) Class.forName("org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory")
           .getConstructor()
           .newInstance();
      } catch(ReflectiveOperationException e) {
         OnteyAPI.logger.error("OpenJDK Nashorn couldn't be loaded, implementation is missing: Nashorn script engine factory could not be instantiated");
         //FileLog.saveStackTrace(e);
         e.printStackTrace();
         throw new RuntimeException("Nashorn script engine factory could not be instantiated");
      }
   }
   
   public JavaScript addVariable(String name, @Nullable Object value) {
      Objects.requireNonNull(name, "name");
      variables.put(name, value);
      return this;
   }
   
   public JavaScript addVariables(Map<String, @Nullable Object> bindings) {
      Objects.requireNonNull(bindings, "bindings");
      this.variables.putAll(bindings);
      return this;
   }
   
   @Nullable
   public Object execute() throws JavaScriptException {
      try {
         return engine.eval(code);
      } catch(ScriptException e) {
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