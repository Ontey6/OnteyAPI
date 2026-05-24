package ontey.api.config;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * This is a {@link Config} implementation that does not save or load
 * from any source, and stores all values in memory only.
 * This is useful for temporary Configurations for providing defaults.
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/MemoryConfiguration.java">Bukkit Source</a>
 */

public class MemoryConfig extends MemorySection implements Config {
	
	protected Config defaults;
	
	protected MemoryConfigOptions options;
	
	/**
	 * Creates an empty {@link MemoryConfig} with no default values.
	 */
	
	public MemoryConfig() {
	}
	
	/**
	 * Creates an empty {@link MemoryConfig} using the specified
	 * {@link Config} as a source for all default values.
	 *
	 * @param defaults Default value provider
	 */
	
	public MemoryConfig(Config defaults) {
		this.defaults = defaults;
	}
	
	@Override
	public void addDefaults(@NonNull Map<@NonNull String, @Nullable Object> defaults) {
		for(var entry : defaults.entrySet())
			this.addDefault(entry.getKey(), entry.getValue());
	}
	
	@Override
	public void addDefaults(@NonNull Config defaults) {
		for(String key : defaults.getKeys(true))
			if(!defaults.isSection(key))
				addDefault(key, defaults.get(key));
	}
	
	@Override
	public Config getDefaults() {
		return this.defaults;
	}
	
	@Override
	public void setDefaults(@NonNull Config defaults) {
		this.defaults = defaults;
	}
	
	@Override
	public MemoryConfigOptions options() {
		if(this.options == null)
			this.options = new MemoryConfigOptions(this);
		
		return this.options;
	}
	
	@Override
	public ConfigSection getParent() {
		return null;
	}
	
	@Override
	public void addDefault(@NonNull String path, @Nullable Object value) {
		if(defaults == null)
			defaults = new MemoryConfig();
		
		defaults.set(path, value);
	}
}
