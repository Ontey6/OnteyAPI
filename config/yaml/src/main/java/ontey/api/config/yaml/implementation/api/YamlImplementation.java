package ontey.api.config.yaml.implementation.api;

import ontey.api.config.ConfigSection;
import ontey.api.config.exception.InvalidConfigException;
import ontey.api.config.util.SupplierIO;
import ontey.api.config.yaml.file.YamlConfigOptions;

import java.io.*;

/**
 * A YAML implementation to load/parse and dump/save a {@link ConfigSection}.
 */

public interface YamlImplementation {
	
	/**
	 * Load YAML to a configuration section.
	 *
	 * @param reader a reader of YAML contents to load
	 * @param section the configuration to fill with the contents
	 * @throws IOException if contents can't be read.
	 * @throws InvalidConfigException if contents is not a valid YAML configuration
	 */
	
	void load(Reader reader, ConfigSection section) throws IOException, InvalidConfigException;
	
	/**
	 * Load YAML to a configuration section.
	 *
	 * @param readerSupplier a function providing a reader of YAML contents to load
	 * @param section the configuration to fill with the contents
	 * @throws IOException if contents can't be read.
	 * @throws InvalidConfigException if contents is not a valid YAML configuration
	 */
	
	default void load(SupplierIO.Reader readerSupplier, ConfigSection section) throws IOException, InvalidConfigException {
		this.load(readerSupplier.get(), section);
	}
	
	/**
	 * Load YAML to a configuration section.
	 *
	 * @param contents a YAML string with contents to load
	 * @param section the configuration to fill with the contents
	 * @throws IOException if contents can't be read.
	 * @throws InvalidConfigException if contents is not a valid YAML string
	 */
	
	default void load(String contents, ConfigSection section) throws IOException, InvalidConfigException {
		this.load(new StringReader(contents), section);
	}
	
	/**
	 * Dump section values to YAML.
	 *
	 * @param writer writer to dump values
	 * @param section section with values to dump
	 */
	
	void dump(Writer writer, ConfigSection section) throws IOException;
	
	/**
	 * Dump section values to a YAML string.
	 *
	 * @param section section with values to dump
	 * @return the values as a valid YAML string
	 */
	
	default String dump(ConfigSection section) throws IOException {
		final StringWriter stringWriter = new StringWriter();
		
		this.dump(stringWriter, section);
		
		return stringWriter.toString();
	}
	
	/**
	 * Apply the configuration options to this implementation.
	 *
	 * @param options YAML options
	 */
	
	void configure(YamlConfigOptions options);
}
