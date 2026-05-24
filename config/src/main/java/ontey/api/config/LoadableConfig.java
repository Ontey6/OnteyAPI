package ontey.api.config;

import ontey.api.config.exception.InvalidConfigException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface LoadableConfig {
	
	/**
	 * Loads this configuration from the specified string.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given string.
	 * <br>
	 * If the string is invalid in any way, an exception will be thrown.
	 *
	 * @param contents Contents of a Configuration to load.
	 * @throws IOException if contents can't be read.
	 * @throws InvalidConfigException if the specified string is invalid.
	 */
	
	void loadFromString(String contents) throws IOException, InvalidConfigException;
	
	/**
	 * Saves this configuration to a string, and returns it.
	 *
	 * @return a String containing this configuration.
	 * @throws IOException when the contents cannot be written for any reason.
	 */
	
	String saveToString() throws IOException;
	
	/**
	 * Loads this configuration from the specified reader.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given string.
	 * <br>
	 * If the contents are invalid in any way, an exception will be thrown.
	 *
	 * @param reader Reader of a Configuration to load.
	 * @throws IOException if reader throws an IOException.
	 * @throws InvalidConfigException if the specified configuration is invalid.
	 */
	
	void load(Reader reader) throws IOException, InvalidConfigException;
	
	/**
	 * Saves this configuration to a writer.
	 *
	 * @param writer where to save this configuration
	 * @throws IOException when the contents cannot be written for any reason.
	 */
	
	void save(Writer writer) throws IOException;
}
