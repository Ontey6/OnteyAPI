package ontey.api.config;

import lombok.NonNull;
import ontey.api.check.Nullity;
import ontey.api.config.serialization.ConfigSerializable;
import ontey.api.config.serialization.ConfigSerialization;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a section of a {@link Config}.
 * <br>
 * A ConfigSection can contain multiple values and child sections, accessible by a unique path.
 *
 * @author Bukkit
 * @author Carlos Lazaro Costa
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/ConfigurationSection.java">Bukkit Source</a>
 */

public interface ConfigSection {
	
	/**
	 * Creates a full path to the given {@link ConfigSection} from its root {@link Config}.
	 * <br>
	 * Example: If a section "foo" has a child section "bar", calling {@code createPath(bar, "baz")}
	 * might return "foo.bar.baz" depending on the root's path separator.
	 * <pre>{@code
	 * ConfigSection section = config.getSection("my.section");
	 * String fullPath = ConfigSection.createPath(section, "myKey"); // e.g., "my.section.myKey"
	 * }</pre>
	 *
	 * @param section Section to create a path for.
	 * @param key Name of the specified section.
	 * @return Full path of the section from its root.
	 */
	
	@NonNull
	static String createPath(@NonNull ConfigSection section, String key) {
		return createPath(section, key, section.getRoot());
	}
	
	/**
	 * Creates a relative path to the given {@link ConfigSection} from the given relative section.
	 * <br>
	 * Example: If a section "foo.bar" is relative to "foo", calling {@code createPath(bar, "baz", foo)}
	 * might return "bar.baz".
	 * <pre>{@code
	 * ConfigSection parent = config.getSection("parent");
	 * ConfigSection child = parent.getSection("child");
	 * String relativePath = ConfigSection.createPath(child, "key", parent); // e.g., "child.key"
	 * }</pre>
	 *
	 * @param section Section to create a path for.
	 * @param key Name of the specified section.
	 * @param relativeTo Section to create the path relative to.
	 * @return Full path of the section from its root.
	 */
	
	@NonNull
	static String createPath(@NonNull ConfigSection section, String key, ConfigSection relativeTo) {
		var root = section.getRoot();
		if(root == null)
			throw new IllegalStateException("Cannot create path without a root");
		
		var separator = root.options().pathSeparator();
		
		var builder = new StringBuilder();
		for(var p = section; p != null && p != relativeTo; p = p.getParent()) {
			if(!builder.isEmpty())
				builder.insert(0, separator);
			
			builder.insert(0, p.getName());
		}
		
		if(key != null && !key.isEmpty()) {
			if(!builder.isEmpty())
				builder.append(separator);
			
			builder.append(key);
		}
		
		return builder.toString();
	}
	
	/**
	 * Gets a set containing all keys in this section.
	 * <br>
	 * If deep is set to true, then this will contain all the keys within any child {@link ConfigSection}s (and their
	 * children, etc.). These will be in a valid path notation for you to use.
	 * <br>
	 * If deep is set to false, then this will contain only the keys of any direct children, and not their own children.
	 *
	 * @param deep Whether or not to get a deep list, as opposed to a shallow list.
	 * @return Set of keys contained within this ConfigurationSection.
	 */
	
	@NonNull
	Set<@NonNull String> getKeys(boolean deep);
	
	/**
	 * Gets a Map containing all keys and their values for this section.
	 * <br>
	 * If deep is set to true, then this will contain all the keys and values within any child {@link ConfigSection}s
	 * (and their children, etc.). These keys will be in a valid path notation for you to use.
	 * <br>
	 * If deep is set to false, then this will contain only the keys and values of any direct children, and not their
	 * own children.
	 *
	 * @param deep Whether or not to get a deep list, as opposed to a shallow list.
	 * @return Map of keys and values of this section.
	 */
	
	@NonNull
	Map<@NonNull String, @NonNull Object> getValues(boolean deep);
	
	/**
	 * Gets a Map containing all keys and their values for this section.
	 * <br>
	 * If deep is set to true, then this will contain all the keys and values within any child {@link Map}s (and their
	 * children, etc.). These keys will be in a valid path notation for you to use.
	 * <br>
	 * If deep is set to false, then this will contain only the keys and values of any direct children, and not their
	 * own children.
	 * <br>
	 * This method differs from {@link #getValues(boolean)} in that child sections are returned as {@link Map}s instead
	 * of {@link ConfigSection}s.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * Map<String, Object> mapValues = section.getMapValues(false);
	 * }</pre>
	 *
	 * @param deep Whether or not to get a deep list, as opposed to a shallow list.
	 * @return Map of keys and values of this section.
	 */
	
	@NonNull
	default Map<@NonNull String, @NonNull Object> getMapValues(boolean deep) {
		return this.getValues(deep).entrySet().stream()
		  .map(entry -> {
			  var key = entry.getKey();
			  var value = entry.getValue();
			  if(value instanceof ConfigSection section)
				  return new AbstractMap.SimpleEntry<>(key, section.getMapValues(deep));
			  return new AbstractMap.SimpleEntry<>(key, value);
		  })
		  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	/**
	 * Checks if this {@link ConfigSection} contains the given path.
	 * <br>
	 * If the value for the requested path does not exist but a default value has been specified, this will return true.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * if(section.contains("path.to.value")) {
	 *    assert section.get("path.to.value") != null; // won't throw
	 * }
	 * }</pre>
	 *
	 * @param path Path to check for existence.
	 * @return True if this section contains the requested path, either via default or being set.
	 */
	
	default boolean contains(@NonNull String path) {
		return this.get(path) != null;
	}
	
	/**
	 * Checks if this {@link ConfigSection} has a value set for the given path.
	 * <br>
	 * If the value for the requested path does not exist but a default value has been specified, this will still
	 * return false.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * if(section.isSet("path.to.value")) {
	 *    assert section.get("path.to.value") != null; // won't throw
	 *    assert section.get("path.to.value", null) != null; // won't throw
	 * }
	 * }</pre>
	 *
	 * @param path Path to check for existence.
	 * @return True if this section contains the requested path, regardless of having a default.
	 */
	
	default boolean isSet(@NonNull String path) {
		if(getRoot() == null)
			return false;
		
		if(getRoot().options().copyDefaults())
			return this.contains(path);
		
		return this.get(path, null) != null;
	}
	
	/**
	 * Gets the size of this configuration section.
	 * <br>
	 * This returns the number of direct children in this section.
	 *
	 * @return Number of keys in this configuration section.
	 */
	
	int size();
	
	/**
	 * Gets if this configuration section is empty.
	 *
	 * @return True if there are no keys in this configuration section.
	 */
	
	default boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Gets the path of this {@link ConfigSection} from its root {@link Config}.
	 * <br>
	 * For any {@link Config} themselves, this will return an empty string.
	 * <br>
	 * If the section is no longer contained within its root for any reason, such as being replaced with a different
	 * value, this may return null.
	 * <br>
	 * To retrieve the single name of this section, that is, the final part of the path returned by this method,
	 * you may use {@link #getName()}.
	 *
	 * @return Path of this section relative to its root.
	 */
	
	@UnknownNullability
	String getPath();
	
	/**
	 * Gets the name of this individual {@link ConfigSection}, in the path.
	 * <br>
	 * This will always be the final part of {@link #getPath()}, unless the section is orphaned.
	 *
	 * @return Name of this section.
	 */
	
	@NonNull
	String getName();
	
	/**
	 * Gets the root {@link Config} that contains this {@link ConfigSection}.
	 * <br>
	 * For any {@link Config} themselves, this will return its own object.
	 * <br>
	 * If the section is no longer contained within its root for any reason, such as being replaced with a different
	 * value, this may return null.
	 *
	 * @return Root configuration containing this section.
	 */
	
	@UnknownNullability
	Config getRoot();
	
	/**
	 * Gets the parent {@link ConfigSection} that directly contains this {@link ConfigSection}.
	 * <br>
	 * For any {@link Config} themselves, this will return null.
	 * <br>
	 * If the section is no longer contained within its parent for any reason, such as being replaced with a different
	 * value, this may return null.
	 *
	 * @return Parent section containing this section.
	 */
	
	@Nullable
	ConfigSection getParent();
	
	/**
	 * Gets the requested Object by path.
	 * <br>
	 * If the Object does not exist but a default value has been specified, this will return the default value.
	 * If the Object does not exist and no default value was specified, this will return null.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * Object value = section.get("path.to.value");
	 * }</pre>
	 *
	 * @param path Path of the Object to get.
	 * @return Requested Object.
	 */
	
	@Nullable
	Object get(@NonNull String path);
	
	/**
	 * Gets the requested Object by path, returning a default value if not found.
	 * <br>
	 * If the Object does not exist then the specified default value will return regardless of if a default has been
	 * identified in the root {@link Config}.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * Object value = section.get("path.to.value", "default");
	 * }</pre>
	 *
	 * @param path Path of the Object to get.
	 * @param def The default value to return if the path is not found.
	 * @return Requested Object.
	 */
	
	@Contract("_, !null -> !null")
	Object get(@NonNull String path, @Nullable Object def);
	
	/**
	 * Sets the specified path to the given value.
	 * <br>
	 * If value is null, the entry will be removed. Any existing entry will be replaced, regardless of what the new
	 * value is.
	 * <br>
	 * Some implementations may have limitations on what you may store. See their individual Javadocs for details.
	 * No implementations should allow you to store {@link Config}s or {@link ConfigSection}s, please use
	 * {@link #createSection(String)} for that.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * section.set("path.to.value", "new value");
	 * section.set("path.to.remove", null);
	 * }</pre>
	 *
	 * @param path Path of the object to set.
	 * @param value New value to set the path to.
	 */
	
	void set(@NonNull String path, @Nullable Object value);
	
	/**
	 * Sets a {@link ConfigSerializable} object at the given path.
	 *
	 * @param path Path of the object to set.
	 * @param value The serializable object.
	 * @param serialize Whether to serialize the object immediately.
	 */
	
	default void setSerializable(@NonNull String path, @Nullable ConfigSerializable value, boolean serialize) {
		if(!serialize) {
			this.set(path, value);
			return;
		}
		
		if(value == null) {
			this.remove(path);
			return;
		}
		
		this.set(path, value.serialize());
	}
	
	/**
	 * Removes the specified path if it exists.
	 * <br>
	 * The entry will be removed, either a value or an entire section.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * section.remove("path.to.remove");
	 * }</pre>
	 *
	 * @param path Path of the object to remove.
	 */
	
	default void remove(@NonNull String path) {
		this.set(path, null);
	}
	
	// Primitives
	
	/**
	 * Creates an empty {@link ConfigSection} at the specified path.
	 * <br>
	 * Any value that was previously set at this path will be overwritten. If the previous value was itself a
	 * {@link ConfigSection}, it will be orphaned.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * ConfigSection newSection = section.createSection("new.section.path");
	 * }</pre>
	 *
	 * @param path Path to create the section at.
	 * @return Newly created section.
	 */
	
	@UnknownNullability
	ConfigSection createSection(@NonNull String path);
	
	/**
	 * Creates a {@link ConfigSection} at the specified path, with specified values.
	 * <br>
	 * Any value that was previously set at this path will be overwritten. If the previous value was itself a
	 * {@link ConfigSection}, it will be orphaned.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * Map<String, Object> initialValues = Map.of("key", "value");
	 * ConfigSection newSection = section.createSection("new.section.path", initialValues);
	 * }</pre>
	 *
	 * @param path Path to create the section at.
	 * @param map The values to used.
	 * @return Newly created section.
	 */
	
	@SuppressWarnings("UnusedReturnValue")
	@UnknownNullability
	default ConfigSection createSection(@NonNull String path, @NonNull Map<?, ?> map) {
		var section = this.createSection(path);
		
		if(section == null)
			return null;
		
		for(var entry : map.entrySet()) {
			if(entry.getValue() instanceof Map<?, ?> value)
				section.createSection(entry.getKey().toString(), value);
			else
				section.set(entry.getKey().toString(), entry.getValue());
		}
		
		return section;
	}
	
	/**
	 * Gets the requested String by path.
	 * <br>
	 * If the String does not exist but a default value has been specified, this will return the default value.
	 * If the String does not exist and no default value was specified, this will return null.
	 *
	 * @param path Path of the String to get.
	 * @return Requested String.
	 */
	
	@Nullable
	default String getString(@NonNull String path) {
		var def = this.getDefault(path);
		return this.getString(path, def != null ? def.toString() : null);
	}
	
	/**
	 * Gets the requested String by path, returning a default value if not found.
	 * <br>
	 * If the String does not exist then the specified default value will return regardless of if a default has been
	 * identified in the root {@link Config}.
	 *
	 * @param path Path of the String to get.
	 * @param def The default value to return if the path is not found or is not a String.
	 * @return Requested String.
	 */
	
	@Contract("_, !null -> !null")
	default String getString(@NonNull String path, @Nullable String def) {
		var val = this.get(path, def);
		return val != null ? val.toString() : null;
	}
	
	/**
	 * Checks if the specified path is a String.
	 * <br>
	 * If the path exists but is not a String, this will return false.
	 * If the path does not exist, this will return false.
	 * If the path does not exist but a default value has been specified, this will check the default value.
	 */
	
	default boolean isString(@NonNull String path) {
		return this.get(path) instanceof String;
	}
	
	/**
	 * Gets an instance of the smallest possible number type that can be parsed from the String found at the path.
	 * If the path does not exist but a default value has been specified, this will use that default value.
	 * <br>
	 * <h6>Integer Types (smallest to biggest)</h6>
	 * <ul>
	 *    <li>Byte</li>
	 *    <li>Short</li>
	 *    <li>Integer</li>
	 *    <li>Long</li>
	 *    <li>BigInteger</li>
	 * </ul>
	 *
	 * <h6>Decimal Types (smallest to biggest)</h6>
	 * <ul>
	 *    <li>Float</li>
	 *    <li>Double</li>
	 *    <li>BigDecimal</li>
	 * </ul>
	 * <br>
	 * If the path doesn't exist, this will return null.
	 * If the path doesn't exist but a default value has been specified, it will be used.
	 */
	
	@Nullable
	default Number getNumber(@NonNull String path) {
		var defaultSection = getSectionInDefaults();
		var def = defaultSection != null ? defaultSection.getNumber(path) : null;
		
		return getNumber(path, def);
	}
	
	/**
	 * Gets an instance of the smallest possible number type by parsing the String found at the path.
	 * <br>
	 * <h6>Integer Types (smallest to biggest)</h6>
	 * <ul>
	 *    <li>Byte</li>
	 *    <li>Short</li>
	 *    <li>Integer</li>
	 *    <li>Long</li>
	 *    <li>BigInteger</li>
	 * </ul>
	 *
	 * <h6>Decimal Types (smallest to biggest)</h6>
	 * <ul>
	 *    <li>Float</li>
	 *    <li>Double</li>
	 *    <li>BigDecimal</li>
	 * </ul>
	 * <br>
	 * If the path doesn't exist, this will return {@code def}.
	 */
	
	@Contract("_, !null -> !null")
	default Number getNumber(@NonNull String path, @Nullable Number def) {
		String stringValue = getString(path, def != null ? def.toString() : null);
		
		if(stringValue == null)
			return null;
		
		Number numberValue = null;
		
		if(stringValue.contains(".")) {
			for(var decimalNumberParser : Storage.decimalNumberParsers)
				if(numberValue == null)
					numberValue = decimalNumberParser.apply(stringValue);
			
			return numberValue != null ? numberValue : def;
		}
		
		for(var numberParser : Storage.numberParsers)
			if(numberValue == null)
				numberValue = tryParsingNumber(stringValue, numberParser);
		
		return numberValue != null ? numberValue : def;
	}
	
	/**
	 * Checks if {@link #getNumber(String)} and {@link #getNumber(String, Number)} can be called with the given path.
	 * This is done by checking if the largest supported integer or decimal can parse the String value at the given path.
	 * <br>
	 * If the path exists but {@link #isPrimitiveWrapper(Object) is not a primitive}, this will return false.
	 * If the path does not exist, this will return false.
	 * If the path does not exist but a default value has been specified, the default value will be checked.
	 */
	
	default boolean isNumber(@NonNull String path) {
		if(!isPrimitiveWrapper(get(path)))
			return false;
		
		String value = getString(path);
		
		if(value == null)
			return false;
		
		return testParsingNumber(value, Storage.numberParsers.getLast())
		  || testParsingNumber(value, Storage.decimalNumberParsers.getLast());
	}
	
	@Nullable
	private <T extends Number> T tryParsingNumber(@NonNull String number, @NonNull Function<@NonNull String, @NonNull T> parser) {
		try {
			return parser.apply(number);
		} catch(Exception e) {
			return null;
		}
	}
	
	private boolean testParsingNumber(@NonNull String number, @NonNull Function<@NonNull String, ? extends @NonNull Number> parser) {
		return tryParsingNumber(number, parser) != null;
	}
	
	/**
	 * Gets the requested int by path.
	 * <br>
	 * If the int does not exist but a default value has been specified, this will return the default value.
	 * If the int does not exist and no default value was specified, this will return 0.
	 *
	 * @param path Path of the int to get.
	 * @return Requested int.
	 */
	
	default int getInt(@NonNull String path) {
		var def = this.getDefault(path);
		return this.getInt(path, def instanceof Number num ? num.intValue() : 0);
	}
	
	/**
	 * Gets the requested int by path, returning a default value if not found.
	 * <br>
	 * If the int does not exist then the specified default value will return regardless of if a default has been
	 * identified in the root {@link Config}.
	 *
	 * @param path Path of the int to get.
	 * @param def The default value to return if the path is not found or is not an int.
	 * @return Requested int.
	 */
	
	default int getInt(@NonNull String path, int def) {
		return this.get(path, def) instanceof Number num ? num.intValue() : def;
	}
	
	/**
	 * Checks if the specified path is an int.
	 * <br>
	 * If the path exists but is not an int, this will return false. If the path does not exist, this will return false.
	 * If the path does not exist but a default value has been specified, this will check if that default value is an
	 * int and return appropriately.
	 *
	 * @param path Path of the int to check.
	 * @return Whether the specified path is an int.
	 */
	
	default boolean isInt(@NonNull String path) {
		return this.get(path) instanceof Integer;
	}
	
	/**
	 * Gets the requested boolean by path.
	 * <br>
	 * If the boolean does not exist but a default value has been specified, this will return the default value.
	 * If the boolean does not exist and no default value was specified, this will return false.
	 *
	 * @param path Path of the boolean to get.
	 * @return Requested boolean.
	 */
	
	default boolean getBoolean(@NonNull String path) {
		var def = this.getDefault(path);
		return this.getBoolean(path, def instanceof Boolean b && b);
	}
	
	/**
	 * Gets the requested boolean by path, returning a default value if not found.
	 * <br>
	 * If the boolean does not exist then the specified default value will return regardless of if a default has been
	 * identified in the root {@link Config}.
	 *
	 * @param path Path of the boolean to get.
	 * @param def The default value to return if the path is not found or is not a boolean.
	 * @return Requested boolean.
	 */
	
	default boolean getBoolean(@NonNull String path, boolean def) {
		return this.get(path, def) instanceof Boolean b ? b : def;
	}
	
	/**
	 * Checks if the specified path is a boolean.
	 * <br>
	 * If the path exists but is not a boolean, this will return false. If the path does not exist, this will return
	 * false. If the path does not exist but a default value has been specified, this will check if that default value
	 * is a boolean and return appropriately.
	 *
	 * @param path Path of the boolean to check.
	 * @return Whether the specified path is a boolean.
	 */
	
	default boolean isBoolean(@NonNull String path) {
		return this.get(path) instanceof Boolean;
	}
	
	/**
	 * Gets the float at the path using {@link #getNumber(String, Number)}.
	 * If the number at the path is bigger than what a float can store, returns {@link Float#MAX_VALUE}
	 * If the path doesn't exist, this will return {@code def}.
	 */
	
	default float getFloat(@NonNull String path, float def) {
		return getNumber(path, def).floatValue();
	}
	
	/**
	 * Gets the float at the path using {@link #getNumber(String)}.
	 * If the number at the path is bigger than what a float can store, returns {@link Float#MAX_VALUE}
	 * If the path doesn't exist, this will return {@code null}.
	 */
	
	default float getFloat(@NonNull String path) {
		var defaultSection = getSectionInDefaults();
		var def = defaultSection != null ? defaultSection.getFloat(path) : 1.0f;
		
		return getFloat(path, def);
	}
	
	/**
	 * Checks if {@link #getFloat(String)} and {@link #getFloat(String, float)} can be called with the given path.
	 * This is done by checking if {@link Float#parseFloat(String)} can parse the String value at the given path.
	 * <br>
	 * If the path does not exist, this will return false.
	 * If the path does not exist but a default value has been specified, the default value will be checked.
	 */
	
	default boolean isFloat(@NonNull String path) {
		var value = getString(path);
		
		return value != null && testParsingNumber(value, Float::parseFloat);
	}
	
	/**
	 * Gets the requested double by path.
	 * <br>
	 * If the double does not exist but a default value has been specified, this will return the default value.
	 * If the double does not exist and no default value was specified, this will return 0.
	 *
	 * @param path Path of the double to get.
	 * @return Requested double.
	 */
	
	default double getDouble(@NonNull String path) {
		var def = this.getDefault(path);
		return this.getDouble(path, def instanceof Number num ? num.doubleValue() : 0);
	}
	
	/**
	 * Gets the requested double by path, returning a default value if not found.
	 * <br>
	 * If the double does not exist then the specified default value will return regardless of if a default has been
	 * identified in the root {@link Config}.
	 *
	 * @param path Path of the double to get.
	 * @param def The default value to return if the path is not found or is not a double.
	 * @return Requested double.
	 */
	
	default double getDouble(@NonNull String path, double def) {
		return this.get(path, def) instanceof Number num ? num.doubleValue() : def;
	}
	
	/**
	 * Checks if the specified path is a double.
	 * <br>
	 * If the path exists but is not a double, this will return false. If the path does not exist, this will return
	 * false. If the path does not exist but a default value has been specified, this will check if that default value
	 * is a double and return appropriately.
	 *
	 * @param path Path of the double to check.
	 * @return Whether the specified path is a double.
	 */
	
	default boolean isDouble(@NonNull String path) {
		return this.get(path) instanceof Double;
	}
	
	/**
	 * Gets the requested long by path.
	 * <br>
	 * If the long does not exist but a default value has been specified, this will return the default value.
	 * If the long does not exist and no default value was specified, this will return 0.
	 *
	 * @param path Path of the long to get.
	 * @return Requested long.
	 */
	
	default long getLong(@NonNull String path) {
		var def = this.getDefault(path);
		return this.getLong(path, def instanceof Number num ? num.longValue() : 0);
	}
	
	/**
	 * Gets the requested long by path, returning a default value if not found.
	 * <br>
	 * If the long does not exist then the specified default value will return regardless of if a default has been
	 * identified in the root {@link Config}.
	 *
	 * @param path Path of the long to get.
	 * @param def The default value to return if the path is not found or is not a long.
	 * @return Requested long.
	 */
	
	default long getLong(@NonNull String path, long def) {
		return this.get(path, def) instanceof Number num ? num.longValue() : def;
	}
	
	/**
	 * Checks if the specified path is a long.
	 * <br>
	 * If the path exists but is not a long, this will return false. If the path does not exist, this will return false.
	 * If the path does not exist but a default value has been specified, this will check if that default value is a
	 * long and return appropriately.
	 *
	 * @param path Path of the long to check.
	 * @return Whether the specified path is a long.
	 */
	
	default boolean isLong(@NonNull String path) {
		return this.get(path) instanceof Long;
	}
	
	/**
	 * Gets the {@link BigInteger} at the path using {@link #getNumber(String, Number)}.
	 * If the path doesn't exist, this will return {@code def}.
	 */
	
	default BigInteger getBigInteger(@NonNull String path, @Nullable BigInteger def) {
		var number = getNumber(path, def);
		
		if(number == null)
			return null;
		
		return number instanceof BigInteger bigInteger
		  ? bigInteger
		  : new BigInteger(number.toString());
	}
	
	/**
	 * Gets the {@link BigInteger} at the path using {@link #getNumber(String, Number)}.
	 * If the path doesn't exist, this will return {@code null}.
	 */
	
	default BigInteger getBigInteger(@NonNull String path) {
		var defaultSection = getSectionInDefaults();
		var def = defaultSection != null ? defaultSection.getBigInteger(path) : null;
		
		return getBigInteger(path, def);
	}
	
	/**
	 * Checks if {@link #getBigInteger(String)} and {@link #getBigInteger(String, BigInteger)} can be called with the given path.
	 * This is done by checking if {@link BigInteger#BigInteger(String)} can parse the String value at the given path.
	 * <br>
	 * If the path does not exist, this will return false.
	 * If the path does not exist but a default value has been specified, the default value will be checked.
	 */
	
	default boolean isBigInteger(@NonNull String path) {
		var value = getString(path);
		
		return value != null && testParsingNumber(value, BigInteger::new);
	}
	
	/**
	 * Gets an entry of the enum specified by {@code clazz} using {@link Enum#valueOf}.
	 * Replaces dashes and spaces with underscores and replaces all lowercase letters with uppercase letters.
	 */
	
	@Nullable
	default <T extends Enum<T>> T getEnum(@NonNull String path, @NonNull Class<T> clazz) {
		return this.getEnum(path, clazz, getSectionInDefaults() == null ? null : getSectionInDefaults().getEnum(path, clazz));
	}
	
	/**
	 * Gets an entry of the enum specified by {@code clazz} using {@link Enum#valueOf}.
	 * Replaces dashes and spaces with underscores and replaces all lowercase letters with uppercase letters.
	 */
	
	@Contract("_, _, !null -> !null")
	default <T extends Enum<T>> T getEnum(@NonNull String path, @NonNull Class<T> clazz, @Nullable T def) {
		try {
			return getEnumEntry(getString(path, ""), clazz);
		} catch(IllegalArgumentException e) {
			return def;
		}
	}
	
	@NonNull
	private <T extends Enum<T>> T getEnumEntry(@NonNull String entry, @NonNull Class<T> clazz) throws IllegalArgumentException {
		String str = entry
		  .replace('-', '_')
		  .replace(' ', '_')
		  .toUpperCase(Locale.ENGLISH);
		
		return Enum.valueOf(clazz, str);
	}
	
	/**
	 * @return Whether the String at the path is a value of the enum specified by {@code clazz}.
	 */
	
	default <T extends Enum<T>> boolean isEnum(@NonNull String path, @NonNull Class<T> clazz) {
		return getEnum(path, clazz) != null;
	}
	
	/**
	 * Gets the requested List by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return null.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List.
	 */
	
	@Nullable
	default List<? extends @UnknownNullability Object> getList(@NonNull String path) {
		return this.getList(path, this.getDefault(path) instanceof List<?> list ? list : null);
	}
	
	// Java
	
	/**
	 * Gets the requested List by path, returning a default value if not found.
	 * <br>
	 * If the List does not exist then the specified default value will return regardless of if a default has been
	 * identified in the root {@link Config}.
	 *
	 * @param path Path of the List to get.
	 * @param def The default value to return if the path is not found or is not a List.
	 * @return Requested List.
	 */
	
	@Contract("_, !null -> !null")
	default List<? extends @UnknownNullability Object> getList(@NonNull String path, @Nullable List<?> def) {
		var val = this.get(path, def);
		return val instanceof List<?> list ? list : def;
	}
	
	/**
	 * Checks if the specified path is a List.
	 * <br>
	 * If the path exists but is not a List, this will return false. If the path does not exist, this will return false.
	 * If the path does not exist but a default value has been specified, this will check if that default value is a
	 * List and return appropriately.
	 *
	 * @param path Path of the List to check.
	 * @return Whether the specified path is a List.
	 */
	
	default boolean isList(@NonNull String path) {
		return this.get(path) instanceof List<?>;
	}
	
	@NonNull
	private List<@NonNull ?> getListOrDefault(@NonNull String path) {
		var list = this.getList(path);
		
		return list == null ? new ArrayList<>(0) : list;
	}
	
	/**
	 * Gets the requested List of String by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a String if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of String.
	 */
	
	@NonNull
	default List<@NonNull String> getStringList(@NonNull String path) {
		var list = getListOrDefault(path);
		
		var result = new ArrayList<String>();
		
		for(var object : list)
			if(object instanceof String || this.isPrimitiveWrapper(object))
				result.add(String.valueOf(object));
		
		return result;
	}
	
	/**
	 * Gets the requested List of Boolean by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Boolean if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Boolean.
	 */
	
	@NonNull
	default List<@NonNull Boolean> getBooleanList(@NonNull String path) {
		var list = getListOrDefault(path);
		
		var result = new ArrayList<Boolean>();
		
		for(var object : list) {
			result.add(switch(object) {
				case Boolean b -> b;
				case String str -> switch(str.toLowerCase()) {
					case "true" -> true;
					case "false" -> false;
					default -> null;
				};
				default -> null;
			});
		}
		
		return Nullity.filterNull(result);
	}
	
	/**
	 * Gets the requested List of Character by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Character if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Character.
	 */
	
	@NonNull
	default List<@NonNull Character> getCharacterList(@NonNull String path) {
		var list = getListOrDefault(path);
		
		var result = new ArrayList<Character>();
		
		for(var object : list) {
			result.add(switch(object) {
				case Character c -> c;
				case String str -> str.length() == 1 ? str.charAt(0) : null;
				case Number num -> (char) num.intValue();
				default -> null;
			});
		}
		
		return Nullity.filterNull(result);
	}
	
	/**
	 * Gets the requested List of Integer by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into an Integer if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Integer.
	 */
	
	@NonNull
	default List<@NonNull Integer> getIntegerList(@NonNull String path) {
		return getNumberList(path, Number::intValue, Integer::parseInt, c -> (int) c);
	}
	
	/**
	 * Gets the requested List of Double by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Double if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Double.
	 */
	
	@NonNull
	default List<@NonNull Double> getDoubleList(@NonNull String path) {
		return getNumberList(path, Number::doubleValue, Double::parseDouble, c -> (double) c);
	}
	
	/**
	 * Gets the requested List of Float by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Float if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Float.
	 */
	
	@NonNull
	default List<@NonNull Float> getFloatList(@NonNull String path) {
		return getNumberList(path, Number::floatValue, Float::parseFloat, c -> (float) c);
	}
	
	/**
	 * Gets the requested List of Long by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Long if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Long.
	 */
	
	@NonNull
	default List<@NonNull Long> getLongList(@NonNull String path) {
		return getNumberList(path, Number::longValue, Long::parseLong, c -> (long) c);
	}
	
	/**
	 * Gets the requested List of Byte by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Byte if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Byte.
	 */
	
	@NonNull
	default List<@NonNull Byte> getByteList(@NonNull String path) {
		return getNumberList(path, Number::byteValue, Byte::parseByte, c -> (byte) c.charValue());
	}
	
	/**
	 * Gets the requested List of Short by path.
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist and no default value was specified, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Short if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Short.
	 */
	
	@NonNull
	default List<@NonNull Short> getShortList(@NonNull String path) {
		return getNumberList(path, Number::shortValue, Short::parseShort, c -> (short) c.charValue());
	}
	
	@NonNull
	private <@NonNull T extends Number> List<T> getNumberList(
	  @NonNull String path,
	  @NonNull Function<@NonNull Number, T> numberConverter,
	  @NonNull Function<@NonNull String, T> stringConverter,
	  @NonNull Function<@NonNull Character, T> charConverter
	) {
		var list = getListOrDefault(path);
		
		var result = new ArrayList<T>();
		
		for(var object : list) {
			result.add(switch(object) {
				case Number num -> numberConverter.apply(num);
				case String str -> parseNumber(str, stringConverter);
				case Character c -> charConverter.apply(c);
				default -> null;
			});
		}
		
		return Nullity.filterNull(result);
	}
	
	@Nullable
	private <T extends Number> T parseNumber(@NonNull String value, @NonNull Function<@NonNull String, @NonNull T> stringConverter) {
		try {
			return stringConverter.apply(value);
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	// Bukkit
	
	/**
	 * Gets the requested List of Maps by path.
	 * <br>
	 * If the List does not exist, this will return an empty List.
	 * <br>
	 * This method will attempt to cast any values into a Map if possible, but may miss any values out if they
	 * are not compatible.
	 *
	 * @param path Path of the List to get.
	 * @return Requested List of Maps.
	 */
	
	@SuppressWarnings("unchecked")
	@NonNull
	default List<@NonNull Map<String, ?>> getMapList(@NonNull String path) {
		var list = getListOrDefault(path);
		var result = new ArrayList<Map<String, ?>>();
		
		for(var object : list)
			if(object instanceof Map<?, ?>)
				result.add((Map<String, ?>) object);
		
		return result;
	}
	
	/**
	 * Gets the requested List of enum entries
	 * <br>
	 * If the List does not exist but a default value has been specified, this will return the default value.
	 * If the List does not exist, this will return an empty List.
	 *
	 * @param clazz The enum class to get the enum entries from
	 * @see #getEnumSet(String, Class)
	 */
	
	@NonNull
	default <T extends Enum<T>> List<@NonNull T> getEnumList(@NonNull String path, @NonNull Class<T> clazz) {
		var list = this.getStringList(path);
		var result = new ArrayList<T>();
		
		for(var entry : list) {
			try {
				result.add(getEnumEntry(entry, clazz));
			} catch(Exception _) {
			}
		}
		
		return result;
	}
	
	@NonNull
	default <T extends Enum<T>> EnumSet<T> getEnumSet(@NonNull String path, @NonNull Class<T> clazz) {
		return EnumSet.copyOf(getEnumList(path, clazz));
	}
	
	/**
	 * Gets the requested object at the given path.
	 * <br>
	 * If the Object does not exist but a default value has been specified, this will return the default value.
	 * If the Object does not exist and no default value was specified, this will return null.
	 * <br>
	 * <b>Note:</b> For example {@code #getObject(path, String.class)} is <b>not</b> equivalent to
	 * {@link #getString(String) #getString(path)} because {@link #getString(String) #getString(path)} converts
	 * internally all Objects to Strings. However, {@code #getObject(path, Boolean.class)} is equivalent to
	 * {@link #getBoolean(String) #getBoolean(path)} for example.
	 *
	 * @param <T> The type of the requested object.
	 * @param path The path to the object.
	 * @param clazz The type of the requested object.
	 * @return Requested object.
	 */
	
	@Nullable
	default <T> T getObject(@NonNull String path, @NonNull Class<T> clazz) {
		Object def = getDefault(path);
		return getObject(path, clazz, clazz.isInstance(def) ? clazz.cast(def) : null);
	}
	
	/**
	 * Gets the requested object at the given path, returning a default value if not found.
	 * <br>
	 * If the Object does not exist then the specified default value will be returned regardless of if a default has
	 * been identified in the root {@link Config}.
	 * <br>
	 * <b>Note:</b> For example {@code #getObject(path, String.class, def)} is <b>not</b> equivalent to
	 * {@link #getString(String, String) #getString(path, def)} because
	 * {@link #getString(String, String) #getString(path, def)} converts internally all Objects to Strings.
	 * However, {@code #getObject(path, Boolean.class, def)} is equivalent to
	 * {@link #getBoolean(String, boolean) #getBoolean(path, def)} for example.
	 *
	 * @param <T> The type of the requested object.
	 * @param path The path to the object.
	 * @param clazz The type of the requested object.
	 * @param def The default object to return if the object is not present at the path.
	 * @return Requested object.
	 */
	
	@Contract("_, _, !null -> !null")
	default <T> T getObject(@NonNull String path, @NonNull Class<T> clazz, @Nullable T def) {
		Object val = get(path, def);
		return clazz.isInstance(val) ? clazz.cast(val) : def;
	}
	
	/**
	 * Gets the requested {@link ConfigSerializable} object at the given path.
	 * <br>
	 * If the Object does not exist but a default value has been specified, this will return the default value.
	 * If the Object does not exist and no default value was specified, this will return null.
	 *
	 * @param <T> The type of {@link ConfigSerializable}.
	 * @param path The path to the object.
	 * @param clazz The type of {@link ConfigSerializable}.
	 * @return Requested {@link ConfigSerializable} object.
	 */
	
	@SuppressWarnings("unchecked")
	@Nullable
	default <T extends ConfigSerializable> T getSerializable(@NonNull String path, @NonNull Class<T> clazz) {
		return switch(get(path)) {
			case Map<?, ?> map -> toSerializable((Map<String, ?>) map, clazz);
			case ConfigSection section -> toSerializable(section.getMapValues(true), clazz);
			case null, default -> getObject(path, clazz);
		};
	}
	
	/**
	 * Gets the requested {@link ConfigSerializable} object at the given path, returning a default value if not found.
	 * <br>
	 * If the Object does not exist then the specified default value will be returned regardless of if a default has
	 * been identified in the root {@link Config}.
	 *
	 * @param <T> The type of {@link ConfigSerializable}.
	 * @param path The path to the object.
	 * @param clazz The type of {@link ConfigSerializable}.
	 * @param def The default object to return if the object is not present at the path.
	 * @return Requested {@link ConfigSerializable} object.
	 */
	
	@SuppressWarnings("unchecked")
	@Contract("_, _, !null -> !null")
	default <T extends ConfigSerializable> T getSerializable(@NonNull String path, @NonNull Class<T> clazz, @Nullable T def) {
		return switch(get(path)) {
			case Map<?, ?> map -> toSerializable((Map<String, Object>) map, clazz);
			case ConfigSection section -> toSerializable(section.getMapValues(true), clazz);
			case null, default -> getObject(path, clazz, def);
		};
	}
	
	@SuppressWarnings("unchecked")
	@UnknownNullability
	private <T extends ConfigSerializable> T toSerializable(@NonNull Map<String, ?> map, @NonNull Class<T> clazz) {
		return (T) ConfigSerialization.deserializeObject(map, clazz);
	}
	
	/**
	 * Gets the requested ConfigurationSection by path.
	 * <br>
	 * If the ConfigurationSection does not exist but a default value has been specified, this will return the
	 * default value. If the ConfigurationSection does not exist and no default value was specified, this will return
	 * null.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * ConfigSection sub = section.getSection("path.to.section");
	 * }</pre>
	 *
	 * @param path Path of the ConfigurationSection to get.
	 * @return Requested ConfigurationSection.
	 */
	
	@Nullable
	default ConfigSection getSection(@NonNull String path) {
		var val = this.get(path, null);
		if(val != null)
			return val instanceof ConfigSection section ? section : null;
		
		val = this.get(path, this.getDefault(path));
		return val instanceof ConfigSection ? this.createSection(path) : null;
	}
	
	/**
	 * Checks if the specified path is a ConfigurationSection.
	 * <br>
	 * If the path exists but is not a ConfigurationSection, this will return false. If the path does not exist,
	 * this will return false. If the path does not exist but a default value has been specified, this will check if
	 * that default value is a ConfigurationSection and return appropriately.
	 *
	 * @param path Path of the ConfigurationSection to check.
	 * @return Whether the specified path is a ConfigurationSection.
	 */
	
	default boolean isSection(@NonNull String path) {
		return this.get(path) instanceof ConfigSection;
	}
	
	/**
	 * Gets the equivalent {@link ConfigSection} from the default {@link Config} defined in {@link #getRoot()}.
	 * <br>
	 * If the root contains no defaults, or the defaults doesn't contain a value for this path, or the value at this
	 * path is not a {@link ConfigSection} then this will return null.
	 *
	 * @return Equivalent section in root configuration.
	 */
	
	@Nullable
	default ConfigSection getSectionInDefaults() {
		var defaults = getRoot() == null ? null : getRoot().getDefaults();
		
		if(defaults != null)
			if(defaults.isSection(this.getPath()))
				return defaults.getSection(this.getPath());
		
		return null;
	}
	
	/**
	 * Sets the default value in the root at the given path as provided.
	 * <br>
	 * If no source {@link Config} was provided as a default collection, then a new {@link MemoryConfig} will be created
	 * to hold the new default value.
	 * <br>
	 * If value is null, the value will be removed from the default Configuration source.
	 * <br>
	 * If the value as returned by {@link #getSectionInDefaults()} is null, then this will create a new section at the
	 * path, replacing anything that may have existed there previously.
	 * <br>
	 * Example usage:
	 * <pre>{@code
	 * section.addDefault("path.to.default", "default value");
	 * }</pre>
	 *
	 * @param path Path of the value to set.
	 * @param value Value to set the default to.
	 */
	
	default void addDefault(@NonNull String path, @Nullable Object value) {
		if(getRoot() == null)
			throw new IllegalStateException("Cannot add default without root");
		
		if(getRoot() == this)
			throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
		
		getRoot().addDefault(ConfigSection.createPath(this, path), value);
	}
	
	@Nullable
	private Object getDefault(@NonNull String path) {
		var defaults = getRoot() == null ? null : getRoot().getDefaults();
		return defaults == null ? null : defaults.get(createPath(this, path));
	}
	
	@Contract(value = "null -> false", pure = true)
	private boolean isPrimitiveWrapper(@Nullable Object input) {
		return input instanceof Integer || input instanceof Boolean ||
		  input instanceof Character || input instanceof Byte ||
		  input instanceof Short || input instanceof Double ||
		  input instanceof Long || input instanceof Float;
	}
	
	class Storage {
		
		private static final List<Function<String, Number>> numberParsers = List.of(
		  Byte::parseByte,
		  Short::parseShort,
		  Integer::parseInt,
		  Long::parseLong,
		  BigInteger::new
		);
		
		private static final List<Function<String, Number>> decimalNumberParsers = List.of(
		  Float::parseFloat,
		  Double::parseDouble,
		  BigDecimal::new
		);
	}
}
