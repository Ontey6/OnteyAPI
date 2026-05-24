package ontey.api.config.file;

import ontey.api.config.Config;
import ontey.api.config.MemoryConfig;
import ontey.api.config.MemoryConfigOptions;
import ontey.api.config.comment.format.CommentFormatter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Various settings for controlling the input and output of a {@link FileConfig}
 *
 * @author Bukkit
 * @author Carlos Lazaro Costa
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/FileConfigurationOptions.java">Bukkit Source</a>
 */

public class FileConfigOptions extends MemoryConfigOptions {
	
	private Charset charset = StandardCharsets.UTF_8;
	
	private String header = null;
	
	private boolean copyHeader = true;
	
	private CommentFormatter headerFormatter;
	
	protected FileConfigOptions(MemoryConfig configuration) {
		super(configuration);
	}
	
	@Override
	public FileConfig configuration() {
		return (FileConfig) super.configuration();
	}
	
	@Override
	public FileConfigOptions pathSeparator(char value) {
		super.pathSeparator(value);
		return this;
	}
	
	@Override
	public FileConfigOptions copyDefaults(boolean value) {
		super.copyDefaults(value);
		return this;
	}
	
	public Charset charset() {
		return this.charset;
	}
	
	public FileConfigOptions charset(Charset charset) {
		this.charset = charset;
		return this;
	}
	
	public boolean isUnicode() {
		return this.charset.name().startsWith("UTF");
	}
	
	/**
	 * Gets the header that will be applied to the top of the saved output.
	 * <br>
	 * This header will be commented out.
	 * <br>
	 * Null is a valid value which will indicate that no header is to be
	 * applied. The default value is null.
	 *
	 * @return Header
	 */
	
	public String header() {
		return this.header;
	}
	
	/**
	 * Sets the header that will be applied to the top of the saved output.
	 * <br>
	 * This header will be commented out and applied directly at the top of
	 * the generated output of the {@link FileConfig}. It is not
	 * required to include a newline at the end of the header as it will
	 * automatically be applied, but you may include one if you wish for extra
	 * spacing.
	 * <br>
	 * Null is a valid header which will indicate that no header is to be applied.
	 *
	 * @param header New header
	 * @return This object, for chaining
	 */
	
	public FileConfigOptions header(String header) {
		this.header = header;
		return this;
	}
	
	/**
	 * Gets whether the header should be copied from a default source.
	 * <br>
	 * If this is true, if a default {@link FileConfig} is passed to
	 * {@link
	 * FileConfig#setDefaults(Config)}
	 * then upon saving it will use the header from that config, instead of
	 * the one provided here.
	 * <br>
	 * If no default is set on the configuration, or the default is not of
	 * type FileConfiguration, or that config has no header ({@link #header()}
	 * returns null) then the header specified in this configuration will be
	 * used.
	 * <br>
	 * Defaults to true.
	 *
	 * @return Whether to copy the header
	 */
	
	public boolean copyHeader() {
		return this.copyHeader;
	}
	
	/**
	 * Sets whether the header should be copied from a default source.
	 * <br>
	 * If this is true, if a default {@link FileConfig} is passed to
	 * {@link
	 * FileConfig#setDefaults(Config)}
	 * then upon saving it will use the header from that config, instead of
	 * the one provided here.
	 * <br>
	 * If no default is set on the configuration, or the default is not of
	 * type FileConfiguration, or that config has no header ({@link #header()}
	 * returns null) then the header specified in this configuration will be
	 * used.
	 * <br>
	 * Defaults to true.
	 *
	 * @param value Whether to copy the header
	 * @return This object, for chaining
	 */
	
	public FileConfigOptions copyHeader(boolean value) {
		this.copyHeader = value;
		return this;
	}
	
	/**
	 * Gets the header format used for parsing and dumping the header.
	 *
	 * @return The header formatter
	 */
	
	public CommentFormatter headerFormatter() {
		return this.headerFormatter;
	}
	
	/**
	 * Sets the header format used for parsing and dumping the header.
	 *
	 * @param headerFormatter The header formatter
	 * @return This object, for chaining
	 */
	
	public FileConfigOptions headerFormatter(CommentFormatter headerFormatter) {
		this.headerFormatter = headerFormatter;
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof FileConfigOptions that)) return false;
		if(!super.equals(o)) return false;
		return copyHeader == that.copyHeader &&
		  Objects.equals(charset, that.charset) &&
		  Objects.equals(header, that.header) &&
		  Objects.equals(headerFormatter, that.headerFormatter);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), charset, header, copyHeader, headerFormatter);
	}
}
