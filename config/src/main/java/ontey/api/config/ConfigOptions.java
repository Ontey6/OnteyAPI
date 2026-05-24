package ontey.api.config;

import ontey.api.check.Checker;
import ontey.api.config.util.StringUtils;

import java.util.Objects;

/**
 * Various settings for controlling the input and output of a {@link Config}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/ConfigurationOptions.java">Source</a>
 */

public class ConfigOptions {
	
	private final Config config;
	
	private char pathSeparator = '.';
	
	private boolean copyDefaults = true;
	
	private int indent = 2;
	
	protected ConfigOptions(Config config) {
		this.config = config;
	}
	
	/**
	 * Returns the {@link Config} that this object is responsible for.
	 *
	 * @return Parent configuration
	 */
	
	public Config configuration() {
		return this.config;
	}
	
	/**
	 * Gets the char that will be used to separate {@link
	 * ConfigSection}s
	 * <br>
	 * This value does not affect how the {@link Config} is stored,
	 * only in how you access the data. The default value is '.'.
	 *
	 * @return Path separator
	 */
	
	public char pathSeparator() {
		return this.pathSeparator;
	}
	
	/**
	 * Sets the char that will be used to separate {@link
	 * ConfigSection}s
	 * <br>
	 * This value does not affect how the {@link Config} is stored,
	 * only in how you access the data. The default value is '.'
	 *
	 * @param value Path separator
	 * @return This object, for chaining
	 */
	
	public ConfigOptions pathSeparator(char value) {
		Checker.checkArgument(value != '\\', value + " is used for escaping and cannot be a path separator");
		Checker.checkArgument(value != '[' && value != ']', value + " is used for indexing and cannot be a path separator");
		this.pathSeparator = value;
		StringUtils.setSeparator(value);
		return this;
	}
	
	/**
	 * Checks if the {@link Config} should copy values from its default
	 * {@link Config} directly.
	 * <br>
	 * If this is true, all values in the default Configuration will be
	 * directly copied, making it impossible to distinguish between values
	 * that were set and values that are provided by default. As a result,
	 * {@link ConfigSection#contains(String)} will always
	 * return the same value as {@link
	 * ConfigSection#isSet(String)}. The default value is
	 * true.
	 *
	 * @return Whether defaults are directly copied
	 */
	
	public boolean copyDefaults() {
		return this.copyDefaults;
	}
	
	/**
	 * Sets if the {@link Config} should copy values from its default
	 * {@link Config} directly.
	 * <br>
	 * If this is true, all values in the default Configuration will be
	 * directly copied, making it impossible to distinguish between values
	 * that were set and values that are provided by default. As a result,
	 * {@link ConfigSection#contains(String)} will always
	 * return the same value as {@link
	 * ConfigSection#isSet(String)}. The default value is
	 * true.
	 *
	 * @param value Whether defaults are directly copied
	 * @return This object, for chaining
	 */
	
	public ConfigOptions copyDefaults(boolean value) {
		this.copyDefaults = value;
		return this;
	}
	
	/**
	 * Gets how much spaces should be used to indent each line.
	 *
	 * @return How much to indent by
	 */
	
	public int indent() {
		return this.indent;
	}
	
	/**
	 * Sets how much spaces should be used to indent each line.
	 *
	 * @param value New indent
	 * @return This object, for chaining
	 */
	
	public ConfigOptions indent(int value) {
		this.indent = value;
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof ConfigOptions that)) return false;
		return indent == that.indent &&
		  pathSeparator == that.pathSeparator &&
		  copyDefaults == that.copyDefaults &&
		  Objects.equals(config, that.config);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(indent, pathSeparator, copyDefaults, config);
	}
}
