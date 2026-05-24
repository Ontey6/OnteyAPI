package ontey.api.config.file;

import lombok.NonNull;
import ontey.api.config.Config;
import ontey.api.config.LoadableConfig;
import ontey.api.config.MemoryConfig;
import ontey.api.config.comment.format.CommentFormatter;
import ontey.api.config.exception.InvalidConfigException;

import java.io.*;
import java.nio.file.Files;

/**
 * This is a base class for all File based implementations of {@link Config}
 *
 * @author Bukkit
 * @author Carleslc
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/FileConfiguration.java">Bukkit Source</a>
 */

public abstract class FileConfig extends MemoryConfig implements LoadableConfig {
	
	/**
	 * Creates an empty {@link FileConfig} with no default values.
	 */
	
	public FileConfig() {
		super();
	}
	
	/**
	 * Creates an empty {@link FileConfig} using the specified
	 * {@link Config} as a source for all default values.
	 *
	 * @param defaults Default value provider
	 */
	
	public FileConfig(Config defaults) {
		super(defaults);
	}
	
	/**
	 * Saves this {@link FileConfig} to the specified location.
	 * <br>
	 * If the file does not exist, it will be created. If already exists, it
	 * will be overwritten. If it cannot be overwritten or created, an
	 * exception will be thrown.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param file File to save to.
	 * @throws IOException Thrown when the given file cannot be written to for any reason.
	 */
	
	public void save(@NonNull File file) throws IOException {
		final File parents = file.getParentFile();
		
		if(parents != null && !parents.exists() && !parents.mkdirs()) // if parent directory doesn't exist and cannot be created
			throw new IOException("Cannot create successfully all needed parent directories!");
		
		this.save(new OutputStreamWriter(Files.newOutputStream(file.toPath()), this.options().charset()));
	}
	
	/**
	 * Saves this {@link FileConfig} to the specified location.
	 * <br>
	 * If the file does not exist, it will be created. If already exists, it
	 * will be overwritten. If it cannot be overwritten or created, an
	 * exception will be thrown.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param file File to save to.
	 * @throws IOException Thrown when the given file cannot be written to for any reason.
	 */
	
	public void save(@NonNull String file) throws IOException {
		this.save(new File(file));
	}
	
	@Override
	public void save(@NonNull Writer writer) throws IOException {
		try(writer) {
			writer.write(this.saveToString());
		}
	}
	
	/**
	 * Loads this configuration from the specified file path.
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
	
	public void load(@NonNull String file) throws FileNotFoundException, IOException, InvalidConfigException {
		this.load(new File(file));
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
	
	public void load(@NonNull File file) throws FileNotFoundException, IOException, InvalidConfigException {
		load(Files.newInputStream(file.toPath()));
	}
	
	/**
	 * Loads this configuration from the specified stream.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given stream.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be
	 * thrown.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param stream Stream to load from
	 * @throws IOException Thrown when the given file cannot be read.
	 * @throws InvalidConfigException Thrown when the given file is not a valid Configuration.
	 * @see #load(Reader)
	 */
	
	public void load(@NonNull InputStream stream) throws IOException, InvalidConfigException {
		load(new InputStreamReader(stream, this.options().charset()));
	}
	
	/**
	 * Loads this configuration from the specified reader.
	 * <br>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given stream.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 *
	 * @param reader the reader to load from
	 * @throws IOException Thrown when underlying reader throws an IOException.
	 * @throws InvalidConfigException Thrown when the reader does not represent a valid Configuration.
	 */
	
	@Override
	public void load(@NonNull Reader reader) throws IOException, InvalidConfigException {
		try(BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
			final StringBuilder builder = new StringBuilder();
			String line;
			
			while((line = input.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
			
			this.loadFromString(builder.toString());
		}
	}
	
	@Override
	public FileConfigOptions options() {
		if(this.options == null) {
			this.options = new FileConfigOptions(this);
		}
		return (FileConfigOptions) this.options;
	}
	
	/**
	 * Compiles the header for this {@link FileConfig} and returns the
	 * result.
	 * <br>
	 * This will use the header from {@link #options()} {@link FileConfigOptions#header()},
	 * respecting the rules of {@link FileConfigOptions#copyHeader()}
	 * and {@link FileConfigOptions#headerFormatter()} if set.
	 *
	 * @return Compiled header
	 */
	
	public String buildHeader() {
		final FileConfigOptions options = this.options();
		
		if(!options.copyHeader()) {
			return "";
		}
		
		final Config def = this.getDefaults();
		
		if(def instanceof FileConfig defaults) {
			final String defaultsHeader = defaults.buildHeader();
			
			if(defaultsHeader != null && !defaultsHeader.isEmpty()) {
				return defaultsHeader;
			}
		}
		
		final String header = options.header();
		final CommentFormatter headerFormatter = options.headerFormatter();
		
		if(headerFormatter != null) {
			final String headerDump = headerFormatter.dump(header);
			return headerDump != null ? headerDump : "";
		}
		
		return header != null && !header.isEmpty() ? header + '\n' : "";
	}
}
