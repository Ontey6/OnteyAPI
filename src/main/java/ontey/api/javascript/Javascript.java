package ontey.api.javascript;

import lombok.Getter;
import lombok.NonNull;
import ontey.api.check.Checker;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * Represents a JavaScript script.
 * Uses GraalJS to run JavaScript code on Java's VM.
 * Therefore, The JavaScript can access all of Java's classes (can be turned off)
 * Accessing works like this:
 * <pre>{@code
 * const ArrayList = Java.type('java.util.ArrayList')
 *
 * var list = new ArrayList()
 * list.add("Hello")
 * list.add("World!")
 *
 * var String = Java.type("java.lang.String")
 *
 * console.log(String.join(", ", list))
 * // Hello, World!
 * }</pre>
 * <p>
 * To use, add these libraries to either your jar as an {@code implementation} or
 * add it to {@code libraries} in your {@code plugin.yml} and {@code compileOnly} dependency.
 * Find the latest version on <a href="https://mvnrepository.com/artifact/org.graalvm.js/js">Maven</a>
 *
 * <pre>{@code
 * org.graalvm.js:js-language:25.0.3
 * org.graalvm.sdk:graal-sdk:25.0.3
 * }</pre>
 */

public class Javascript {
	
	@Getter
	private final Context context;
	
	/**
	 * Creates a new JavaScript executor.
	 *
	 * @param context The context
	 * @throws IllegalStateException If the file doesn't exist, is a directory, or doesn't end with {@code .js}.
	 */
	
	public Javascript(@NonNull Context context) {
		this.context = context;
	}
	
	/**
	 * Creates a new JavaScript executor.
	 *
	 * @param context The context
	 * @param variables Variables that are added after initializing code and context
	 */
	
	public Javascript(@NonNull Context context, @NonNull Map<@NonNull String, @NonNull Object> variables) {
		this.context = context;
		
		addVariables(variables);
	}
	
	/**
	 * Creates a new JavaScript executor.
	 * Uses a context that allows the script all access.
	 *
	 * @param variables Variables that are added after initializing code and context
	 */
	
	public Javascript(@NonNull Map<@NonNull String, @NonNull Object> variables) {
		this();
		
		addVariables(variables);
	}
	
	/**
	 * Creates a new JavaScript executor.
	 * Uses a context that allows the script all access.
	 *
	 * @throws IllegalStateException If the file doesn't exist, is a directory, or doesn't end with {@code .js}.
	 */
	
	public Javascript() {
		this(
		  Context.newBuilder("js")
			 .allowAllAccess(true)
			 .build()
		);
	}
	
	/**
	 * Adds a variable/binding into the script's global scope.
	 */
	
	@NonNull
	public Javascript addVariable(@NonNull String name, @NonNull Object value) {
		context.getBindings("js").putMember(name, value);
		return this;
	}
	
	/**
	 * Adds multiple variables/bindings into the script's global scope.
	 */
	
	@NonNull
	public Javascript addVariables(@NonNull Map<@NonNull String, @NonNull Object> variables) {
		variables.forEach(this::addVariable);
		return this;
	}
	
	/**
	 * Adds a class to the script's global scope.
	 */
	
	public Javascript addClass(@NonNull String name, @NonNull Class<?> clazz) {
		context.getBindings("js").putMember(name, clazz);
		return this;
	}
	
	/**
	 * Adds a class to the script's global scope.
	 */
	
	public Javascript addClasses(@NonNull Map<@NonNull String, @NonNull Class<?>> classes) {
		classes.forEach(this::addClass);
		return this;
	}
	
	/**
	 * Adds a class to the script's global scope.
	 * Don't use for arrays.
	 * Uses the class' {@linkplain Class#getSimpleName() simple name} as the name.
	 * If the class is nested, uses the name of the inner class.
	 * Example: {@code Configuration.ConfigurationOptions} -> {@code ConfigurationOptions}.
	 * Please only use this method when specifying the name manually with {@code MyClass.class},
	 * otherwise use {@link #addClass(String, Class)}.
	 */
	
	public Javascript addClass(@NonNull Class<?> clazz) {
		Checker.checkState(!clazz.isArray(), "Class can't be an array! Give it a custom name with addClass(String, Class)");
		String name = clazz.getSimpleName();
		if(name.contains("$"))
			name = name.substring(name.lastIndexOf('$') + 1);
		
		return addClass(name, clazz);
	}
	
	/**
	 * Evaluates the given code and returns the result.
	 * If the code doesn't return anything, {@code null} is returned.
	 *
	 * @param code The JavaScript code to evaluate.
	 * @return The return value of the JavaScript code.
	 * @throws JavaScriptException If the code can't be evaluated by the JavaScript engine.
	 */
	
	@Nullable
	public Object eval(String code) throws JavaScriptException {
		try {
			Value result = context.eval("js", code);
			return result.isNull() ? null : result.as(Object.class);
		} catch(PolyglotException e) {
			throw new JavaScriptException(e);
		}
	}
	
	/**
	 * Evaluates the given code and returns the result.
	 * If the code doesn't return anything, {@code null} is returned.
	 *
	 * @param javaScriptFile The file containing the JavaScript code to evaluate.
	 * Has to exist, not be a directory and end with {@code .js}.
	 * @return The return value of the JavaScript code.
	 * @throws JavaScriptException If the code can't be evaluated by the JavaScript engine.
	 * @throws IOException If the file can't be read.
	 * @throws IllegalStateException If the file doesn't exist, is a directory, or doesn't end with {@code .js}.
	 */
	
	@Nullable
	public Object eval(File javaScriptFile) throws JavaScriptException, IOException, IllegalStateException {
		Checker.checkState(javaScriptFile.exists(), "javaScriptFile must exist");
		Checker.checkState(javaScriptFile.isFile(), "javaScriptFile must be a file");
		Checker.checkState(javaScriptFile.getName().endsWith(".js"), "javaScriptFile must be a js file");
		
		String code = String.join("\n", Files.readAllLines(javaScriptFile.toPath()));
		return eval(code);
	}
}