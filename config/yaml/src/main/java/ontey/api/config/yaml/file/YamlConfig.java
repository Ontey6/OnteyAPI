package ontey.api.config.yaml.file;

import lombok.NonNull;
import ontey.api.config.Config;
import ontey.api.config.ConfigSection;
import ontey.api.config.exception.InvalidConfigException;
import ontey.api.config.file.FileConfig;
import ontey.api.config.file.FileConfigOptions;
import ontey.api.config.util.StringUtils;
import ontey.api.config.util.SupplierIO;
import ontey.api.config.yaml.comment.format.YamlHeaderFormatter;
import ontey.api.config.yaml.implementation.api.QuoteStyle;
import ontey.api.config.yaml.implementation.api.QuoteValue;
import ontey.api.config.yaml.implementation.api.YamlImplementation;
import ontey.api.config.yaml.implementation.snakeyaml.SnakeYamlImplementation;

import java.io.*;
import java.nio.file.Files;

/**
 * An implementation of {@link Config} which saves the configuration in YAML.
 * Note that this implementation is not synchronized.
 *
 * @author Bukkit
 * @author Carleslc
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/YamlConfiguration.java">Bukkit Source</a>
 */

public class YamlConfig extends FileConfig {
	
	protected YamlImplementation yamlImplementation;
	
	/**
	 * Creates an empty {@link YamlConfig}.
	 */
	
	public YamlConfig() {
		this((Config) null);
	}
	
	/**
	 * Creates an empty {@link YamlConfig} using the specified
	 * {@link Config} as a source for all default values.
	 *
	 * @param defaults default values
	 */
	
	public YamlConfig(Config defaults) {
		this(defaults, new SnakeYamlImplementation());
	}
	
	public YamlConfig(YamlImplementation yamlImplementation) {
		this(null, yamlImplementation);
	}
	
	public YamlConfig(Config defaults, YamlImplementation yamlImplementation) {
		super(defaults);
		this.setImplementation(yamlImplementation);
	}
	
	/**
	 * Creates a new {@link YamlConfig}, loading from the given reader.
	 *
	 * @param readerSupplier a function providing the reader to load from (new instance)
	 * @return resulting configuration
	 * @throws IOException if configuration can't be loaded
	 */
	
	public static YamlConfig loadConfiguration(@NonNull SupplierIO.Reader readerSupplier) throws IOException {
		return YamlConfig.load(config -> config.load(readerSupplier));
	}
	
	/**
	 * Creates a new {@link YamlConfig}, loading from the given file.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param file Input file
	 * @return Resulting configuration
	 * @throws IOException if configuration can't be loaded
	 */
	
	public static YamlConfig loadConfiguration(@NonNull File file) throws IOException {
		return YamlConfig.load(config -> config.load(file));
	}
	
	/**
	 * Creates a new {@link YamlConfig}, loading from the specified string contents.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 *
	 * @param contents the contents to load from
	 * @throws IOException Thrown when underlying reader throws an IOException.
	 * @throws InvalidConfigException Thrown when the contents don't represent a valid Configuration.
	 */
	
	public static YamlConfig loadConfigurationFromString(String contents) throws IOException {
		return YamlConfig.load(config -> config.loadFromString(contents));
	}
	
	/**
	 * Creates a new {@link YamlConfig}, loading from the given input stream.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 *
	 * @param streamSupplier a function providing the stream to load from (new instance)
	 * @return resulting configuration
	 * @throws InvalidConfigException if input stream is not a valid Configuration.
	 * @throws IOException if configuration can't be loaded
	 */
	
	public static YamlConfig loadConfiguration(@NonNull SupplierIO.InputStream streamSupplier) throws IOException {
		return YamlConfig.load(config -> config.load(streamSupplier));
	}
	
	/**
	 * Creates a new {@link YamlConfig}, loading from the given stream.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param stream Input stream
	 * @return Resulting configuration
	 * @throws IOException if configuration can't be loaded
	 * @see #loadConfiguration(SupplierIO.InputStream)
	 * @see #loadConfiguration(SupplierIO.Reader)
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link #load(SupplierIO.InputStream)}
	 */
	
	@Deprecated
	public static YamlConfig loadConfiguration(@NonNull InputStream stream) throws IOException {
		return YamlConfig.load(config -> config.load(stream));
	}
	
	/**
	 * Creates a new {@link YamlConfig}, loading from the given reader.
	 *
	 * @param reader input reader
	 * @return resulting configuration
	 * @throws IOException if configuration can't be loaded
	 * @see #loadConfiguration(SupplierIO.Reader)
	 * @see #loadConfiguration(SupplierIO.InputStream)
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link #load(SupplierIO.Reader)}
	 */
	
	@Deprecated
	public static YamlConfig loadConfiguration(@NonNull Reader reader) throws IOException {
		return YamlConfig.load(config -> config.load(reader));
	}
	
	private static YamlConfig load(YamlConfigurationLoader loader) throws IOException {
		final YamlConfig config = new YamlConfig();
		
		loader.load(config);
		
		return config;
	}
	
	public YamlImplementation getImplementation() {
		return this.yamlImplementation;
	}
	
	public void setImplementation(@NonNull YamlImplementation yamlImplementation) {
		this.yamlImplementation = yamlImplementation;
		this.yamlImplementation.configure(this.options());
	}
	
	/**
	 * Save the configuration values including the header to a string.
	 *
	 * @throws IOException when the contents cannot be written for any reason
	 */
	
	@Override
	public String saveToString() throws IOException {
		final StringWriter stringWriter = new StringWriter();
		
		this.save(stringWriter);
		
		return StringUtils.stripCarriage(stringWriter.toString());
	}
	
	/**
	 * Save the configuration values including the header.
	 *
	 * @param writer where to save this configuration
	 * @throws IOException when the contents cannot be written for any reason
	 * @see #saveToString()
	 */
	
	@Override
	public void save(@NonNull Writer writer) throws IOException {
		try(writer) {
			writer.write(this.buildHeader());
			this.dump(writer);
		}
	}
	
	/**
	 * Dump the configuration values without the header to a string.
	 *
	 * @throws IOException when the contents cannot be written for any reason
	 * @see #saveToString()
	 */
	
	public String dump() throws IOException {
		return this.yamlImplementation.dump(this);
	}
	
	/**
	 * Dump the configuration values without the header.
	 *
	 * @param writer where to save this configuration
	 * @throws IOException when the contents cannot be written for any reason
	 * @see #save(Writer)
	 */
	
	public void dump(@NonNull Writer writer) throws IOException {
		this.yamlImplementation.dump(writer, this);
	}
	
	/**
	 * Loads this {@link YamlConfig} from the specified reader.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given stream.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 *
	 * @param readerSupplier a function providing the reader to load from (new instance)
	 * @throws IOException Thrown when underlying reader throws an IOException.
	 * @throws InvalidConfigException Thrown when the reader does not represent a valid Configuration.
	 */
	
	public void load(@NonNull SupplierIO.Reader readerSupplier) throws IOException, InvalidConfigException {
		this.loadHeader(readerSupplier.get());
		
		this.yamlImplementation.load(readerSupplier, this);
	}
	
	protected void loadHeader(Reader reader) throws IOException {
		final YamlConfigOptions options = this.options();
		final YamlHeaderFormatter headerFormatter = options.headerFormatter();
		boolean customStripPrefix = headerFormatter.stripPrefix();
		
		headerFormatter.stripPrefix(false);
		options.header(headerFormatter.parse(reader)); // save header with prefix to dump it as is
		
		headerFormatter.stripPrefix(customStripPrefix); // restore the custom strip prefix for the following calls to parse
	}
	
	/**
	 * Loads this configuration from the specified file.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given file.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be
	 * thrown.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param file File to load from.
	 * @throws FileNotFoundException Thrown when the given file cannot be opened.
	 * @throws IOException Thrown when the given file cannot be read.
	 * @throws InvalidConfigException Thrown when the given file is not a valid Configuration.
	 */
	
	@Override
	public void load(@NonNull File file) throws FileNotFoundException, IOException, InvalidConfigException {
		load(() -> Files.newInputStream(file.toPath()));
	}
	
	/**
	 * Loads this {@link YamlConfig} from the specified string.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given stream.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 *
	 * @param contents the contents to load from
	 * @throws IOException Thrown when underlying reader throws an IOException.
	 * @throws InvalidConfigException Thrown when the contents don't represent a valid Configuration.
	 */
	
	@Override
	public void loadFromString(@NonNull String contents) throws IOException {
		this.load(() -> new StringReader(contents));
	}
	
	/**
	 * Loads this configuration from the specified stream.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given stream.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param streamSupplier a function providing the stream to load from (new instance)
	 * @throws IOException Thrown when the given file cannot be read.
	 * @throws InvalidConfigException Thrown when the given file is not a valid Configuration.
	 * @see #load(SupplierIO.Reader)
	 */
	
	public void load(@NonNull SupplierIO.InputStream streamSupplier) throws IOException, InvalidConfigException {
		load(() -> new InputStreamReader(streamSupplier.get(), this.options().charset()));
	}
	
	/**
	 * @see #loadConfiguration(SupplierIO.InputStream)
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link #load(SupplierIO.InputStream)}
	 */
	
	@Override
	@Deprecated
	@SuppressWarnings("DuplicateThrows")
	public void load(@NonNull InputStream stream) throws IOException, InvalidConfigException {
		super.load(stream);
	}
	
	/**
	 * @see #loadConfiguration(SupplierIO.Reader)
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link #load(SupplierIO.Reader)}
	 */
	
	@Override
	@Deprecated
	@SuppressWarnings("DuplicateThrows")
	public void load(@NonNull Reader reader) throws IOException, InvalidConfigException {
		super.load(reader);
	}
	
	/**
	 * Sets the specified path to the given value.
	 * <br>
	 * The value will be represented with the specified quote style in the configuration file.
	 * <br>
	 * Any existing entry will be replaced, regardless of what the new value is.
	 * <br>
	 * Null value is valid and will not remove the key, this is different to {@link #set(String, Object)}.
	 * Instead, a null value will be written as a YAML empty null value.
	 * <br>
	 * Some implementations may have limitations on what you may store. See
	 * their individual Javadocs for details. No implementations should allow
	 * you to store {@link Config}s or {@link ConfigSection}s,
	 * please use {@link #createSection(String)} for that.
	 *
	 * @param path Path of the object to set.
	 * @param value New value to set the path to.
	 * @param quoteStyle The quote style to use.
	 */
	
	public void set(String path, Object value, QuoteStyle quoteStyle) {
		this.set(path, new QuoteValue<>(value, quoteStyle));
	}
	
	/**
	 * Sets the specified path to the given value.
	 * <br>
	 * If value is null, the entry will be removed. Any existing entry will be
	 * replaced, regardless of what the new value is.
	 * <br>
	 * Some implementations may have limitations on what you may store. See
	 * their individual Javadocs for details. No implementations should allow
	 * you to store {@link Config}s or {@link ConfigSection}s,
	 * please use {@link #createSection(String)} for that.
	 *
	 * @param path Path of the object to set.
	 * @param value New value to set the path to.
	 */
	
	@Override
	public void set(@NonNull String path, Object value) {
		if(value != null && !(value instanceof QuoteValue)) {
			final QuoteStyle quoteStyle = this.options().quoteStyleDefaults().getExplicitQuoteStyleInstanceOf(value.getClass());
			if(quoteStyle != null) {
				this.set(path, value, quoteStyle);
				return;
			}
		}
		super.set(path, value);
	}
	
	@Override
	public Object get(@NonNull String path, Object def) {
		Object object = super.get(path, def);
		
		if(object instanceof QuoteValue) {
			object = ((QuoteValue<?>) object).value();
		}
		
		return object;
	}
	
	@Override
	public YamlConfigOptions options() {
		if(this.options == null) {
			this.options = new YamlConfigOptions(this);
		}
		return (YamlConfigOptions) this.options;
	}
	
	@FunctionalInterface
	private interface YamlConfigurationLoader {
		
		void load(YamlConfig config) throws IOException;
	}
}
