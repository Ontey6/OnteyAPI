package ontey.js;

import lombok.NonNull;
import ontey.OnteyAPI;
import ontey.check.Checker;
import ontey.check.TryCatch;
import org.jetbrains.annotations.Nullable;

import javax.script.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

/**
 * To use, add these libraries to either your jar as an {@code implementation} or add it to {@code libraries} in your {@code plugin.yml}.
 * Find the latest version on <a href="https://mvnrepository.com/artifact/org.openjdk.nashorn/nashorn-core">Maven</a>
 * <pre>{@code
 * org.openjdk.nashorn:nashorn-core:15.7
 * }</pre>
 */

public class NashornJavaScript implements JavaScript {
   
   private static final ScriptEngineManager manager = new ScriptEngineManager();
   
   private static final ScriptEngineFactory engineFactory;
   
   private final ScriptEngine engine;
   
   private final Bindings variables;
   
   private final String code;
   
   public NashornJavaScript(@NonNull String line, @NonNull Map<@Nullable String, Object> variables) {
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
   
   public NashornJavaScript(@NonNull File javaScriptFile, @NonNull Map<@NonNull String, @NonNull Object> variables) {
      Checker.checkArgument(javaScriptFile.exists(), "javaScriptFile must exist");
      Checker.checkArgument(javaScriptFile.isFile(), "javaScriptFile must be a file");
      Checker.checkArgument(javaScriptFile.getName().endsWith(".js"), "javaScriptFile must be a js file");
      
      manager.registerEngineName("Nashorn", engineFactory);
      this.engine = manager.getEngineByName("Nashorn");
      this.variables = engine.getBindings(ScriptContext.ENGINE_SCOPE);
      this.variables.clear();
      this.variables.putAll(variables);
      this.code = TryCatch.wrapCheckedExceptions(() -> String.join("\n", Files.readAllLines(javaScriptFile.toPath())));
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
         throw new RuntimeException("Nashorn script engine factory could not be instantiated", e);
      }
   }
   
   @NonNull
   public JavaScript addVariable(@NonNull String name, @Nullable Object value) {
      Objects.requireNonNull(name, "name");
      variables.put(name, value);
      return this;
   }
   
   @NonNull
   public JavaScript addVariables(@NonNull Map<String, @Nullable Object> bindings) {
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
      return TryCatch.ignoreCheckedExceptions(this::execute);
   }
}