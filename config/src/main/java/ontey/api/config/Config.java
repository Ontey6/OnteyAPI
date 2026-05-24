package ontey.api.config;

import lombok.NonNull;

import java.util.Map;

/**
 * Represents a source of configurable options and settings
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/Configuration.java">Bukkit Source</a>
 */

public interface Config extends ConfigSection {
	
	/**
	 * Sets the default value of the given path as provided.
	 * <br>
	 * If no source {@link Config} was provided as a default
	 * collection, then a new {@link MemoryConfig} will be created to
	 * hold the new default value.
	 * <br>
	 * If value is null, the value will be removed from the default
	 * Configuration source.
	 *
	 * @param path Path of the value to set.
	 * @param value Value to set the default to.
	 */
	
	@Override
	void addDefault(@NonNull String path, Object value);
	
	/**
	 * Sets the default values of the given paths as provided.
	 * <br>
	 * If no source {@link Config} was provided as a default
	 * collection, then a new {@link MemoryConfig} will be created to
	 * hold the new default values.
	 *
	 * @param defaults A map of Path/Values to add to defaults.
	 */
	
	void addDefaults(Map<String, Object> defaults);
	
	/**
	 * Sets the default values of the given paths as provided.
	 * <br>
	 * If no source {@link Config} was provided as a default
	 * collection, then a new {@link MemoryConfig} will be created to
	 * hold the new default value.
	 * <br>
	 * This method will not hold a reference to the specified Configuration,
	 * nor will it automatically update if that Configuration ever changes. If
	 * you require this, you should set the default source with {@link
	 * #setDefaults(Config)}.
	 *
	 * @param defaults A configuration holding a list of defaults to copy.
	 * @throws IllegalArgumentException Thrown if defaults is this.
	 */
	
	void addDefaults(Config defaults);
	
	/**
	 * Gets the source {@link Config} for this configuration.
	 * <br>
	 * If no configuration source was set, but default values were added, then
	 * a {@link MemoryConfig} will be returned. If no source was set
	 * and no defaults were set, then this method will return null.
	 *
	 * @return Configuration source for default values, or null if none exist.
	 */
	
	Config getDefaults();
	
	/**
	 * Sets the source of all default values for this {@link Config}.
	 * <br>
	 * If a previous source was set, or previous default values were defined,
	 * then they will not be copied to the new source.
	 *
	 * @param defaults New source of default values for this configuration.
	 * @throws IllegalArgumentException Thrown if defaults is this.
	 */
	
	void setDefaults(Config defaults);
	
	/**
	 * Gets the {@link ConfigOptions} for this {@link Config}.
	 * <br>
	 * All setters through this method are chainable.
	 *
	 * @return Options for this configuration
	 */
	
	ConfigOptions options();
}
