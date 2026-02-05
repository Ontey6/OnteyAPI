package ontey.js;

import lombok.NonNull;
import ontey.check.Checker;
import ontey.check.TryCatch;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

/**
 * To use, add these libraries to either your jar as an {@code implementation} or
 * add it to {@code libraries} in your {@code plugin.yml}. Find the latest version
 * on <a href="https://mvnrepository.com/artifact/org.graalvm.js/js">Maven</a>
 *
 * <pre>{@code
 * org.graalvm.js:js-language:25.0.1
 * org.graalvm.sdk:graal-sdk:25.0.1
 * }</pre>
 */

public class GraalJavaScript implements JavaScript {
   
   private final Context context;
   
   private final String code;
   
   public GraalJavaScript(@NonNull String line, @NonNull Map<@NonNull String, @Nullable Object> variables) {
      this.code = line;
      
      this.context = Context.newBuilder("js")
        .allowAllAccess(true)
        .build();
      
      addVariables(variables);
   }
   
   public GraalJavaScript(@NonNull String line) {
      this(line, Map.of());
   }
   
   public GraalJavaScript(@NonNull File javaScriptFile, Map<@NonNull String, @Nullable Object> variables) {
      Checker.checkArgument(javaScriptFile.exists(), "javaScriptFile must exist");
      Checker.checkArgument(javaScriptFile.isFile(), "javaScriptFile must be a file");
      Checker.checkArgument(javaScriptFile.getName().endsWith(".js"), "javaScriptFile must be a js file");
      
      this.code = TryCatch.wrapCheckedExceptions(() -> String.join("\n", Files.readAllLines(javaScriptFile.toPath())));
      
      this.context = Context.newBuilder("js")
        .allowAllAccess(true)
        .build();
      
      addVariables(variables);
   }
   
   public GraalJavaScript(@NonNull File javaScriptFile) {
      this(javaScriptFile, Map.of());
   }
   
   @NonNull
   public JavaScript addVariable(@NonNull String name, @Nullable Object value) {
      context.getBindings("js").putMember(name, value);
      return this;
   }
   
   @NonNull
   public JavaScript addVariables(@NonNull Map<@NonNull String, @Nullable Object> variables) {
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