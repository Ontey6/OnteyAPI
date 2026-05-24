package ontey.api.config.yaml.file;

import lombok.NonNull;
import ontey.api.config.comment.CommentType;
import ontey.api.config.comment.Commentable;
import ontey.api.config.comment.KeyTree;
import ontey.api.config.exception.InvalidConfigException;
import ontey.api.config.file.FileConfig;
import ontey.api.config.file.FileConfigOptions;
import ontey.api.config.util.SupplierIO;
import ontey.api.config.yaml.comment.YamlCommentMapper;
import ontey.api.config.yaml.comment.format.YamlCommentFormat;
import ontey.api.config.yaml.comment.format.YamlCommentFormatter;
import ontey.api.config.yaml.comment.format.YamlHeaderFormatter;
import ontey.api.config.yaml.implementation.SimpleYamlImplementation;
import ontey.api.config.yaml.implementation.api.QuoteValue;
import ontey.api.config.yaml.implementation.api.YamlImplementation;
import ontey.api.config.yaml.implementation.api.YamlImplementationCommentable;
import ontey.api.config.yaml.implementation.snakeyaml.SnakeYamlImplementation;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;

/**
 * An extension of {@link YamlConfig} which saves all data in YAML to a configuration file
 * with the added possibility to save and manage comments.
 * Note that this implementation is not synchronized.
 *
 * @author Carlos Lázaro Costa
 */

public class YamlFile extends YamlConfig implements Commentable {
	
	/**
	 * File where data will be saved for this configuration
	 */
	
	private File configFile;
	
	/**
	 * Builds this {@link YamlFile} without any configuration file.
	 * <br>
	 * In order to save changes you will have to use one of these methods before:<br>
	 * - {@link #setConfigurationFile(File)}<br>
	 * - {@link #setConfigurationFile(String)}<br>
	 * Or set the file when saving changes with {@link #save(File)}
	 */
	
	public YamlFile() {
		super(new SimpleYamlImplementation());
	}
	
	/**
	 * Builds this {@link YamlFile} without any configuration file.
	 * <br>
	 * In order to save changes you will have to use one of these methods before:<br>
	 * - {@link #setConfigurationFile(File)}<br>
	 * - {@link #setConfigurationFile(String)}<br>
	 * Or set the file when saving changes with {@link #save(File)}
	 *
	 * @param yamlImplementation the implementation to use. Default is {@link SimpleYamlImplementation},
	 * but you can provide other like {@link SnakeYamlImplementation} or a custom implementation.
	 */
	
	public YamlFile(YamlImplementation yamlImplementation) {
		super(yamlImplementation);
	}
	
	/**
	 * Builds this {@link FileConfig} with the file specified by path.
	 *
	 * @param path location for the configuration file
	 * @throws IllegalArgumentException if path is a directory.
	 * <br>Note that if <code>IllegalArgumentException</code> is thrown then this
	 * configuration file will be <b>null</b>.
	 */
	
	public YamlFile(String path) throws IllegalArgumentException {
		this();
		this.setConfigurationFile(path);
	}
	
	/**
	 * Builds this {@link FileConfig} with a source file.
	 *
	 * @param file the configuration file
	 * @throws IllegalArgumentException if file is a directory.
	 * <br>Note that if <code>IllegalArgumentException</code> is thrown then this
	 * configuration file will be <b>null</b>.
	 */
	
	public YamlFile(File file) throws IllegalArgumentException {
		this();
		this.setConfigurationFile(file);
	}
	
	/**
	 * Builds this {@link FileConfig} with the file specified by uri.
	 *
	 * @param uri of the configuration file
	 * @throws IllegalArgumentException if file is a directory.
	 * <br>Note that if <code>IllegalArgumentException</code> is thrown then this
	 * configuration file will be <b>null</b>.
	 */
	
	public YamlFile(URI uri) throws IllegalArgumentException {
		this();
		this.setConfigurationFile(uri);
	}
	
	/**
	 * Builds this {@link FileConfig} with the file specified by url.
	 *
	 * @param url of the configuration file
	 * @throws URISyntaxException if this URL is not formatted strictly according to
	 * RFC2396 and cannot be converted to a URI.
	 * @throws IllegalArgumentException if file is a directory.
	 * <br>Note that if <code>IllegalArgumentException</code> is thrown then this
	 * configuration file will be <b>null</b>.
	 */
	
	public YamlFile(URL url) throws IllegalArgumentException, URISyntaxException {
		this(url.toURI());
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the given file.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param file Input file
	 * @param withComments if comments should be parsed
	 * @return Resulting configuration
	 * @throws IOException if configuration cannot be loaded
	 */
	
	public static YamlFile loadConfiguration(@NonNull File file, boolean withComments) throws IOException {
		return YamlFile.load(config -> {
			config.setConfigurationFile(file);
			config.load();
		}, withComments);
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the given file (without comments).
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param file Input file
	 * @return Resulting configuration
	 * @throws IOException if configuration cannot be loaded
	 * @see #loadConfiguration(File, boolean)
	 */
	
	public static YamlFile loadConfiguration(@NonNull File file) throws IOException {
		return YamlFile.loadConfiguration(file, false);
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the specified string contents.
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 *
	 * @param contents the contents to load from
	 * @param withComments if comments should be parsed
	 * @return resulting configuration
	 * @throws IOException if underlying reader throws an IOException.
	 * @throws InvalidConfigException if the contents does not represent a valid Configuration.
	 */
	
	public static YamlFile loadConfigurationFromString(String contents, boolean withComments) throws IOException {
		return YamlFile.load(config -> config.loadFromString(contents), withComments);
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the specified string contents (without comments).
	 * <br>
	 * If the file cannot be loaded for any reason, an exception will be thrown.
	 *
	 * @param contents the contents to load from
	 * @return resulting configuration
	 * @throws IOException if underlying reader throws an IOException.
	 * @throws InvalidConfigException if the contents does not represent a valid Configuration.
	 */
	
	public static YamlFile loadConfigurationFromString(String contents) throws IOException {
		return YamlFile.loadConfigurationFromString(contents, false);
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the given reader.
	 *
	 * @param readerSupplier reader supplier
	 * @param withComments if comments should be parsed
	 * @return resulting configuration
	 * @throws IOException if configuration cannot be loaded
	 */
	
	public static YamlFile loadConfiguration(@NonNull SupplierIO.Reader readerSupplier, boolean withComments) throws IOException {
		return YamlFile.load(config -> config.load(readerSupplier), withComments);
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the given reader (without comments).
	 *
	 * @param readerSupplier reader supplier
	 * @return resulting configuration
	 * @throws IOException if configuration cannot be loaded
	 */
	
	public static YamlFile loadConfiguration(@NonNull SupplierIO.Reader readerSupplier) throws IOException {
		return YamlFile.loadConfiguration(readerSupplier, false);
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the given stream.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param streamSupplier input stream supplier
	 * @param withComments if comments should be parsed
	 * @return Resulting configuration
	 * @throws IOException if configuration cannot be loaded
	 */
	
	public static YamlFile loadConfiguration(@NonNull SupplierIO.InputStream streamSupplier, boolean withComments) throws IOException {
		return YamlFile.load(config -> config.load(streamSupplier), withComments);
	}
	
	/**
	 * Creates a new {@link YamlFile}, loading from the given stream (without comments).
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @param streamSupplier input stream supplier
	 * @return Resulting configuration
	 * @throws IOException if configuration cannot be loaded
	 */
	
	public static YamlFile loadConfiguration(@NonNull SupplierIO.InputStream streamSupplier) throws IOException {
		return YamlFile.loadConfiguration(streamSupplier, false);
	}
	
	/**
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link YamlFile#loadConfiguration(SupplierIO.InputStream, boolean)}
	 */
	
	@Deprecated
	public static YamlFile loadConfiguration(@NonNull InputStream stream, boolean withComments) throws IOException {
		return YamlFile.load(config -> config.load(stream), withComments);
	}
	
	/**
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link #loadConfiguration(SupplierIO.InputStream)}
	 */
	
	@Deprecated
	public static YamlFile loadConfiguration(@NonNull InputStream stream) throws IOException {
		return YamlFile.loadConfiguration(stream, false);
	}
	
	/**
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link YamlFile#loadConfiguration(SupplierIO.Reader, boolean)}
	 */
	
	@Deprecated
	public static YamlFile loadConfiguration(@NonNull Reader reader, boolean withComments) throws IOException {
		return YamlFile.load(config -> config.load(reader), withComments);
	}
	
	/**
	 * @deprecated this method loads the entire file into memory, for larger files please use {@link #loadConfiguration(SupplierIO.Reader)}
	 */
	
	@Deprecated
	public static YamlFile loadConfiguration(@NonNull Reader reader) throws IOException {
		return YamlFile.loadConfiguration(reader, false);
	}
	
	private static YamlFile load(YamlFileLoader loader, boolean withComments) throws IOException {
		final YamlFile config = new YamlFile();
		
		config.options().useComments(withComments);
		
		loader.load(config);
		
		return config;
	}
	
	/**
	 * Saves this {@link FileConfig} to the configuration file location.
	 * <br>
	 * If the file does not exist, it will be created. If already exists, it
	 * will be overwritten. If it cannot be overwritten or created, an
	 * exception will be thrown.
	 * <br>
	 * Comments copied will be those loaded with {@link #loadWithComments()} and those added
	 * with {@link #setComment(String, String)} or {@link #setComment(String, String, CommentType)}.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @throws IOException if it hasn't been possible to save configuration file
	 * @throws IllegalArgumentException if the configuration file is not set
	 */
	
	public void save() throws IOException {
		assertConfigFileIsNonNull();
		this.save(this.configFile);
	}
	
	/**
	 * Saves this {@link YamlFile} to a string and returns it.
	 * <br>
	 * Comments copied will be those loaded with {@link #loadWithComments()} and those added
	 * with {@link #setComment(String, String)} or {@link #setComment(String, String, CommentType)}.
	 *
	 * @return String containing this configuration with comments.
	 * @throws IOException if it hasn't been possible to save configuration file
	 */
	
	public String saveToString() throws IOException {
		return super.saveToString();
	}
	
	/**
	 * Set a comment to the section or value selected by path.
	 * Comment will be indented automatically.
	 * Multi-line comments can be provided using \n character.
	 * <br>
	 * Comment format will follow the rules of {@link #options()} {@link YamlConfigOptions#commentFormatter()}.
	 *
	 * @param path path of desired section or key
	 * @param comment the comment to add, # prefix is not needed
	 * @param type either above (BLOCK) or SIDE
	 */
	
	@Override
	public void setComment(String path, String comment, CommentType type) {
		if(this.yamlImplementation instanceof YamlImplementationCommentable commentable) {
			commentable.setComment(path, comment, type);
		}
	}
	
	/**
	 * Set a block comment above the section or value selected by path.
	 * Comment will be indented automatically.
	 * Multi-line comments can be provided using \n character.
	 * <br>
	 * Comment format will follow the rules of {@link #options()} {@link YamlConfigOptions#commentFormatter()}.
	 *
	 * @param path path of desired section or key
	 * @param comment the block comment to add, # character is not needed
	 */
	
	public void setComment(String path, String comment) {
		this.setComment(path, comment, CommentType.BLOCK);
	}
	
	/**
	 * Set a comment to the section or value selected by path.
	 * Comment will be indented automatically.
	 * Multi-line comments can be provided using \n character.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormatter}.
	 *
	 * @param path path of desired section or key
	 * @param comment the comment to add, # prefix is not needed
	 * @param type either above (BLOCK) or SIDE
	 * @param yamlCommentFormatter the comment formatter to use
	 */
	
	public void setComment(String path, String comment, CommentType type, YamlCommentFormatter yamlCommentFormatter) {
		final YamlCommentFormatter defaultFormatter = this.options().commentFormatter();
		this.setCommentFormat(yamlCommentFormatter);
		this.setComment(path, comment, type);
		this.setCommentFormat(defaultFormatter);
	}
	
	/**
	 * Set a comment to the section or value selected by path.
	 * Comment will be indented automatically.
	 * Multi-line comments can be provided using \n character.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormat}.
	 *
	 * @param path path of desired section or key
	 * @param comment the comment to add, # prefix is not needed
	 * @param type either above (BLOCK) or SIDE
	 * @param yamlCommentFormat the comment format to use
	 */
	
	public void setComment(String path, String comment, CommentType type, @NonNull YamlCommentFormat yamlCommentFormat) {
		this.setComment(path, comment, type, yamlCommentFormat.commentFormatter());
	}
	
	/**
	 * Set a block comment to the section or value selected by path.
	 * Comment will be indented automatically.
	 * Multi-line comments can be provided using \n character.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormatter}.
	 *
	 * @param path path of desired section or key
	 * @param comment the block comment to add, # prefix is not needed
	 * @param yamlCommentFormatter the comment formatter to use
	 */
	
	public void setComment(String path, String comment, YamlCommentFormatter yamlCommentFormatter) {
		this.setComment(path, comment, CommentType.BLOCK, yamlCommentFormatter);
	}
	
	/**
	 * Set a block comment to the section or value selected by path.
	 * Comment will be indented automatically.
	 * Multi-line comments can be provided using \n character.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormat}.
	 *
	 * @param path path of desired section or key
	 * @param comment the block comment to add, # prefix is not needed
	 * @param yamlCommentFormat the comment format to use
	 */
	
	public void setComment(String path, String comment, YamlCommentFormat yamlCommentFormat) {
		this.setComment(path, comment, CommentType.BLOCK, yamlCommentFormat);
	}
	
	/**
	 * Set a blank line at the beginning of the block comment.
	 * If currently there is no block comment for the provided path then it sets "\n" as the block comment.
	 *
	 * @param path path of desired section or key
	 */
	
	public void setBlankLine(String path) {
		final YamlCommentFormatter defaultFormatter = this.options().commentFormatter();
		this.setCommentFormat(YamlCommentFormat.RAW);
		final String comment = this.getComment(path, CommentType.BLOCK);
		if(comment == null) {
			this.setComment(path, "\n", CommentType.BLOCK);
		} else {
			this.setComment(path, '\n' + comment, CommentType.BLOCK);
		}
		this.setCommentFormat(defaultFormatter);
	}
	
	/**
	 * Retrieve the comment of the section or value selected by path.
	 * <br>
	 * Comment format will follow the rules of {@link #options()} {@link YamlConfigOptions#commentFormatter()}.
	 *
	 * @param path path of desired section or key
	 * @param type either above (BLOCK) or SIDE
	 * @return the comment of the section or value selected by path,
	 * or null if that path does not have any comment of this type
	 */
	
	@Override
	public String getComment(String path, CommentType type) {
		if(this.yamlImplementation instanceof YamlImplementationCommentable commentable) {
			return commentable.getComment(path, type);
		}
		return null;
	}
	
	/**
	 * Retrieve the block comment of the section or value selected by path.
	 * <br>
	 * Comment format will follow the rules of {@link #options()} {@link YamlConfigOptions#commentFormatter()}.
	 *
	 * @param path path of desired section or key
	 * @return the block comment of the section or value selected by path,
	 * or null if that path does not have any comment of type block
	 */
	
	public String getComment(String path) {
		return this.getComment(path, CommentType.BLOCK);
	}
	
	/**
	 * Retrieve the comment of the section or value selected by path.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormatter}.
	 *
	 * @param path path of desired section or key
	 * @param type either above (BLOCK) or SIDE
	 * @param yamlCommentFormatter the comment formatter to use
	 * @return the comment of the section or value selected by path,
	 * or null if that path does not have any comment of this type
	 */
	
	public String getComment(String path, CommentType type, YamlCommentFormatter yamlCommentFormatter) {
		final YamlCommentFormatter defaultFormatter = this.options().commentFormatter();
		this.setCommentFormat(yamlCommentFormatter);
		final String comment = this.getComment(path, type);
		this.setCommentFormat(defaultFormatter);
		return comment;
	}
	
	/**
	 * Retrieve the comment of the section or value selected by path.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormat}.
	 *
	 * @param path path of desired section or key
	 * @param type either above (BLOCK) or SIDE
	 * @param yamlCommentFormat the comment format to use
	 * @return the comment of the section or value selected by path,
	 * or null if that path does not have any comment of this type
	 */
	
	public String getComment(String path, CommentType type, @NonNull YamlCommentFormat yamlCommentFormat) {
		return this.getComment(path, type, yamlCommentFormat.commentFormatter());
	}
	
	/**
	 * Retrieve the block comment of the section or value selected by path.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormatter}.
	 *
	 * @param path path of desired section or key
	 * @param yamlCommentFormatter the comment formatter to use
	 * @return the block comment of the section or value selected by path,
	 * or null if that path does not have any comment of type block
	 */
	
	public String getComment(String path, YamlCommentFormatter yamlCommentFormatter) {
		return this.getComment(path, CommentType.BLOCK, yamlCommentFormatter);
	}
	
	/**
	 * Retrieve the block comment of the section or value selected by path.
	 * <br>
	 * Comment format will follow the rules of the provided {@link YamlCommentFormat}.
	 *
	 * @param path path of desired section or key
	 * @param yamlCommentFormat the comment format to use
	 * @return the block comment of the section or value selected by path,
	 * or null if that path does not have any comment of type block
	 */
	
	public String getComment(String path, YamlCommentFormat yamlCommentFormat) {
		return this.getComment(path, CommentType.BLOCK, yamlCommentFormat);
	}
	
	/**
	 * Change the comment formatter to one of the defaults provided by {@link YamlCommentFormat}.
	 * <br>
	 * This will change the behavior for parsing comments with {@link #getComment(String, CommentType)}
	 * and for dumping comments with {@link #setComment(String, String, CommentType)}.
	 * If default behavior does not suit you then change the format before calling one of these methods.
	 *
	 * @param yamlCommentFormat desired format to set/dump and get/parse comments
	 */
	
	public void setCommentFormat(@NonNull YamlCommentFormat yamlCommentFormat) {
		this.setCommentFormat(yamlCommentFormat.commentFormatter());
	}
	
	/**
	 * Change the comment formatter for parsing and dumping comments.
	 * This is a shortcut to {@link #options()} {@link YamlConfigOptions#commentFormatter(YamlCommentFormatter)}.
	 * <br>
	 * This will change the behavior for parsing comments with {@link #getComment(String, CommentType)}
	 * and for dumping comments with {@link #setComment(String, String, CommentType)}.
	 *
	 * @param yamlCommentFormatter desired formatter to set/dump and get/parse comments
	 */
	
	public void setCommentFormat(YamlCommentFormatter yamlCommentFormatter) {
		this.options().commentFormatter(yamlCommentFormatter);
	}
	
	/**
	 * Gets the header of this configuration file.
	 * <br>
	 * The string format will respect the rules of the {@link #options()} {@link YamlConfigOptions#headerFormatter()}.
	 * By default, the {@link YamlHeaderFormatter} is used and the result string will not have a blank line at the end.
	 * <br>
	 * Null is a valid value which will indicate that no header is applied.
	 * The default value is null.
	 *
	 * @return header
	 */
	
	public String getHeader() {
		final YamlConfigOptions options = this.options();
		final YamlHeaderFormatter headerFormatter = options.headerFormatter();
		try {
			return headerFormatter.parse(headerFormatter.dump(options.header()));
		} catch(IOException e) {
			throw new RuntimeException("Cannot parse header", e);
		}
	}
	
	/**
	 * Sets the header that will be applied to the top of the saved output.
	 * This is a shortcut to {@link #options()} {@link YamlConfigOptions#header(String)}.
	 * <br>
	 * This header will be commented out and applied directly at the top of
	 * the generated output of this configuration file.
	 * <br>
	 * The rules of {@link #options()} {@link YamlConfigOptions#headerFormatter()} will be respected.
	 * By default, the {@link YamlHeaderFormatter} is used and the header will have a blank line written at the end of the header in the file.
	 * It is not required to include a newline at the end of the header as it will
	 * automatically be applied, but you may include one if you wish for extra spacing.
	 * <br>
	 * Null is a valid value which will indicate that no header is to be applied.
	 *
	 * @param header New header
	 */
	
	public void setHeader(String header) {
		this.options().header(header);
	}
	
	/**
	 * Gets the footer of this configuration file.
	 * This is a shortcut to {@link #getComment(String)} with null path.
	 * <br>
	 * The string format will respect the rules of the {@link #options()} {@link YamlConfigOptions#commentFormatter()}.
	 * <br>
	 * Null is a valid value which will indicate that no footer is applied.
	 * The default value is null.
	 *
	 * @return the footer comment at the end of the file
	 */
	
	public String getFooter() {
		return this.getComment(null);
	}
	
	/**
	 * Sets the footer of this configuration file.
	 * This is a shortcut to {@link #setComment(String, String)} with null path.
	 * <br>
	 * This footer will be commented out and applied at the bottom of the generated output of this configuration file.
	 * The end of the file will have a new line character '\n'.
	 * The rules of {@link #options()} {@link YamlConfigOptions#commentFormatter()} will be respected.
	 * <br>
	 * Null is a valid value which will indicate that no footer is applied.
	 *
	 * @param footer the footer comment to write at the end of the file
	 */
	
	public void setFooter(String footer) {
		this.setComment(null, footer);
	}
	
	/**
	 * Get a wrapper builder to set a value to the given path and optionally set comments.
	 * <br>
	 * This is an alternative API for the following pattern:
	 * <pre>{@code
	 * yamlFile.set("test.hello", "Hello");
	 * yamlFile.setComment("test.hello", "Block comment");
	 * yamlFile.setComment("test.hello", "Side comment", CommentType.SIDE);
	 * }</pre>
	 * You can achieve the same with:
	 * <pre>{@code
	 * yamlFile.path("test.hello")
	 *         .set("Hello")
	 *         .comment("Block comment")
	 *         .commentSide("Side comment");
	 * }</pre>
	 *
	 * @param path path of the object or configuration to set
	 * @return the wrapper builder
	 */
	
	public YamlFileWrapper path(String path) {
		return new YamlFileWrapper(this, path);
	}
	
	/**
	 * Get the comment mapper. This has access to read the comment nodes directly.
	 *
	 * @return the comment mapper or null if this configuration is loaded without comments
	 * @see #getComment(String, CommentType)
	 * @see #setComment(String, String, CommentType)
	 */
	
	public YamlCommentMapper getCommentMapper() {
		if(this.yamlImplementation instanceof YamlImplementationCommentable) {
			return ((YamlImplementationCommentable) this.yamlImplementation).getCommentMapper();
		}
		return null;
	}
	
	@Override
	public void set(@NonNull String path, Object value) {
		super.set(path, value);
		
		if(this.getCommentMapper() != null) {
			final Object innerValue = value instanceof QuoteValue ? ((QuoteValue<?>) value).value() : value;
			if(innerValue instanceof Collection) {
				this.setListNode((Collection<?>) innerValue, this.getCommentMapper().getNode(path));
			}
		}
	}
	
	protected void setListNode(Collection<?> value, KeyTree.Node node) {
		if(node != null) {
			node.isList(value.size());
			
			// Check list of lists
			int i = 0;
			for(Object element : value) {
				if(!(element instanceof Collection)) {
					return;
				}
				node = node.getElement(i++);
				setListNode((Collection<?>) element, node);
			}
		}
	}
	
	/**
	 * Loads configurations from this configuration file.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 * <br>
	 * Note that this method will not load comments of original configuration file,
	 * if needed use {@link #loadWithComments()} instead.
	 *
	 * @throws IOException if it hasn't been possible to load file
	 * @throws InvalidConfigException if there has been an error while parsing configuration file
	 * @throws FileNotFoundException if configuration file is not found
	 * @see #loadWithComments()
	 */
	
	public void load() throws InvalidConfigException, IOException {
		assertConfigFileIsNonNull();
		this.load(this.configFile);
	}
	
	/**
	 * Loads configurations from this configuration file including comments.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 * <br>
	 * <b>Use {@link #load()} instead to improve performance when configuration file has no comments or don't need it.</b>
	 *
	 * @throws IOException if it hasn't been possible to load file
	 * @throws InvalidConfigException if there has been an error while parsing configuration file
	 * @throws FileNotFoundException if configuration file is not found
	 * @see #load()
	 */
	
	public void loadWithComments() throws InvalidConfigException, IOException {
		this.options().useComments(true);
		this.load();
	}
	
	/**
	 * Loads this {@link YamlFile} from the specified reader.
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
	
	@Override
	public void load(@NonNull SupplierIO.Reader readerSupplier) throws IOException, InvalidConfigException {
		super.load(readerSupplier);
	}
	
	/**
	 * If this configuration file does not exist then it is created along with missing parent directories.
	 * <br>
	 * Otherwise loads configurations from this configuration file.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @throws IOException if it hasn't been possible to load file
	 * @throws InvalidConfigException if there has been an error while parsing configuration file
	 * @throws FileNotFoundException if configuration file is not found
	 * @see #createOrLoadWithComments()
	 */
	
	public void createOrLoad() throws IOException, InvalidConfigException {
		this.createNewFile(false);
		this.load();
	}
	
	/**
	 * If this configuration file does not exist then it is created along with missing parent directories.
	 * <br>
	 * Otherwise loads configurations from this configuration file, including comments.
	 * <br>
	 * This method will use the {@link #options()} {@link FileConfigOptions#charset() charset} encoding,
	 * which defaults to UTF8.
	 *
	 * @throws IOException if it hasn't been possible to load file
	 * @throws InvalidConfigException if there has been an error while parsing configuration file
	 * @throws FileNotFoundException if configuration file is not found
	 * @see #createOrLoad()
	 */
	
	public void createOrLoadWithComments() throws IOException, InvalidConfigException {
		this.createNewFile(false);
		this.loadWithComments();
	}
	
	/**
	 * Tests whether this configuration file exists.
	 *
	 * @return <code>true</code> if and only if this configuration file exists;
	 * <code>false</code> otherwise
	 */
	
	public boolean exists() {
		return this.configFile != null && this.configFile.exists();
	}
	
	/**
	 * Creates a new empty file atomically for this configuration file.
	 * <br>
	 * Parent directories will be created if they do not exist.
	 *
	 * @param overwrite indicates if file must be overwritten if it already exists.
	 * Note that if overwrite is set to false and there is already a file with that path file
	 * will not be created and no exception is thrown.
	 * @throws IOException if I/O error occurs creating the configuration file
	 */
	
	public void createNewFile(boolean overwrite) throws IOException {
		assertConfigFileIsNonNull();
		if(overwrite || !this.configFile.exists()) {
			try {
				final File parents = this.configFile.getParentFile();
				if(parents != null && !parents.exists() && !parents.mkdirs()) {
					throw new IOException("Cannot create successfully all needed parent directories!");
				}
				if(!this.configFile.createNewFile() && (!overwrite || !this.configFile.exists())) {
					throw new IOException("Cannot create successfully the configuration file!");
				}
			} catch(SecurityException e) {
				throw new IOException(e.getMessage(), e.getCause());
			}
		}
	}
	
	/**
	 * Creates a new empty file atomically for this configuration file if and only if it does not already exist.
	 * <br>
	 * Parent directories will be created if they do not exist.
	 *
	 * @throws IOException if I/O error occurs creating the configuration file
	 */
	
	public void createNewFile() throws IOException {
		this.createNewFile(false);
	}
	
	/**
	 * Deletes this configuration file from disk.
	 *
	 * @throws IOException if file cannot be deleted
	 */
	
	public void deleteFile() throws IOException {
		assertConfigFileIsNonNull();
		if(!this.configFile.delete()) {
			throw new IOException("Failed to delete " + this.configFile);
		}
	}
	
	/**
	 * Returns the size of this configuration file.
	 *
	 * @return the size, in bytes, of this configuration file.
	 */
	
	public long getSize() {
		return this.configFile.length();
	}
	
	/**
	 * Returns the absolute pathname string of this configuration file.
	 *
	 * @return the absolute path where configuration file is located.
	 */
	
	public String getFilePath() {
		assertConfigFileIsNonNull();
		return this.configFile.getAbsolutePath();
	}
	
	/**
	 * Returns this configuration file where data is located.
	 *
	 * @return the configuration file where this {@link FileConfig} writes.
	 */
	
	public File getFile() {
		return this.configFile;
	}
	
	/**
	 * Returns this configuration file where data is located.
	 *
	 * @return the configuration file where this {@link FileConfig} writes.
	 */
	
	public File getConfigurationFile() {
		return this.configFile;
	}
	
	/**
	 * Rebuilds this {@link FileConfig} with the file specified by path.
	 *
	 * @param path location for the configuration file
	 * @throws IllegalArgumentException if path is a directory.
	 * <br>Note that if <code>IllegalArgumentException</code> is thrown then this
	 * configuration file will be <b>null</b>.
	 */
	
	public void setConfigurationFile(@NonNull String path) throws IllegalArgumentException {
		setConfigFile(new File(path));
	}
	
	/**
	 * Rebuilds this {@link FileConfig} with the file specified by uri.
	 *
	 * @param uri of the configuration file
	 * @throws IllegalArgumentException if file is a directory.
	 * <br>Note that if <code>IllegalArgumentException</code> is thrown then this
	 * configuration file will be <b>null</b>.
	 */
	
	public void setConfigurationFile(@NonNull URI uri) throws IllegalArgumentException {
		setConfigFile(new File(uri));
	}
	
	/**
	 * Rebuilds this {@link FileConfig} with a source file.
	 *
	 * @param file the configuration file
	 * @throws IllegalArgumentException if file is a directory.
	 * <br>Note that if <code>IllegalArgumentException</code> is thrown then this
	 * configuration file will be <b>null</b>.
	 */
	
	public void setConfigurationFile(@NonNull File file) throws IllegalArgumentException {
		setConfigFile(file);
	}
	
	private void setConfigFile(File file) throws IllegalArgumentException {
		if(file.isDirectory()) {
			throw new IllegalArgumentException(file.getName() + " is a directory!");
		}
		this.configFile = file;
	}
	
	/**
	 * Copy this configuration file to another path, without deleting configuration file.
	 * If there is already a file on the other path it will be overwritten.
	 *
	 * @param path the location of the new file, including name (mustn't be a directory)
	 * @return the new copied file
	 * @throws FileNotFoundException if configuration file is not found as source to copy
	 * @throws IllegalArgumentException if path is a directory
	 * @throws IOException if there I/O error occurs copying file
	 */
	
	public File copyTo(@NonNull String path) throws FileNotFoundException, IllegalArgumentException, IOException {
		final File copy = new File(path);
		this.copyTo(copy);
		return copy;
	}
	
	/**
	 * Copy this configuration file to another file, without deleting configuration file.
	 *
	 * @param file destination file (mustn't be a directory)
	 * @throws FileNotFoundException if configuration file is not found as source to copy
	 * @throws IllegalArgumentException if path is a directory
	 * @throws IOException if there I/O error occurs copying file
	 */
	
	public void copyTo(@NonNull File file) throws FileNotFoundException, IllegalArgumentException, IOException {
		assertConfigFileIsNonNull();
		if(!this.configFile.exists()) {
			throw new FileNotFoundException(this.configFile.getName() + " is not found in " + this.configFile.getAbsolutePath());
		}
		if(file.isDirectory()) {
			throw new IllegalArgumentException(file.getAbsolutePath() + " is a directory!");
		}
		try(OutputStream fos = Files.newOutputStream(file.toPath())) {
			Files.copy(this.configFile.toPath(), fos);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Returns a representation of the already saved configuration file.
	 *
	 * @return the configuration file disk contents, or null if the file does not exist
	 * @throws IOException if configuration file cannot be read
	 */
	
	public String fileToString() throws IOException {
		if(!exists()) {
			return null;
		}
		return new String(Files.readAllBytes(this.configFile.toPath()));
	}
	
	/**
	 * Returns a representation of this configuration file.
	 *
	 * @return a representation of this configuration file
	 * <br>If something goes wrong then this string is an error message.
	 */
	
	@Override
	public String toString() {
		try {
			return this.saveToString();
		} catch(IOException e) {
			return e.getMessage();
		}
	}
	
	private void assertConfigFileIsNonNull() {
		if(this.configFile == null)
			throw new IllegalArgumentException("This configuration file is null");
	}
	
	@FunctionalInterface
	@SuppressWarnings("DuplicateThrows")
	private interface YamlFileLoader {
		
		void load(YamlFile config) throws IOException, InvalidConfigException;
	}
}
