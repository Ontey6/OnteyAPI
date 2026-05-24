package ontey.api.config;

/**
 * Various settings for controlling the input and output of a {@link MemoryConfig}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/ConfigurationOptions.java">Bukkit Source</a>
 */

public class MemoryConfigOptions extends ConfigOptions {
	
	protected MemoryConfigOptions(MemoryConfig configuration) {
		super(configuration);
	}
	
	@Override
	public MemoryConfig configuration() {
		return (MemoryConfig) super.configuration();
	}
	
	@Override
	public MemoryConfigOptions pathSeparator(char value) {
		super.pathSeparator(value);
		return this;
	}
	
	@Override
	public MemoryConfigOptions copyDefaults(boolean value) {
		super.copyDefaults(value);
		return this;
	}
}
