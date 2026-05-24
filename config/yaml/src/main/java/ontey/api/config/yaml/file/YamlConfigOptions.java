package ontey.api.config.yaml.file;

import lombok.Getter;
import ontey.api.check.Checker;
import ontey.api.config.comment.format.CommentFormatter;
import ontey.api.config.file.FileConfigOptions;
import ontey.api.config.yaml.comment.format.YamlCommentFormat;
import ontey.api.config.yaml.comment.format.YamlCommentFormatter;
import ontey.api.config.yaml.comment.format.YamlHeaderFormatter;
import ontey.api.config.yaml.implementation.api.QuoteStyle;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Various settings for controlling the input and output of a {@link YamlConfig}
 *
 * @author Bukkit
 * @author Carleslc
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/YamlConfigurationOptions.java">Bukkit Source</a>
 */

public class YamlConfigOptions extends FileConfigOptions {
	
	/**
	 * Defines what {@link QuoteStyle} should each type use for its values.
	 */
	
	private final QuoteStyleDefaults quoteStyleDefaults = new QuoteStyleDefaults();
	
	/**
	 * Additional indentation for list elements.
	 */
	
	private int indentList = 2;
	
	/**
	 * Comment formatter used to format comments.
	 */
	
	private YamlCommentFormatter commentFormatter;
	
	/**
	 * A flag that indicates if the configuration file should parse comments.
	 */
	
	private boolean useComments = false;
	
	protected YamlConfigOptions(YamlConfig configuration) {
		super(configuration);
		
		this.headerFormatter(new YamlHeaderFormatter());
	}
	
	@Override
	public YamlConfig configuration() {
		return (YamlConfig) super.configuration();
	}
	
	@Override
	public YamlConfigOptions copyDefaults(boolean value) {
		super.copyDefaults(value);
		return this;
	}
	
	@Override
	public YamlConfigOptions pathSeparator(char value) {
		super.pathSeparator(value);
		return this;
	}
	
	@Override
	public YamlConfigOptions charset(Charset charset) {
		super.charset(charset);
		return this;
	}
	
	@Override
	public YamlConfigOptions header(String header) {
		super.header(header);
		return this;
	}
	
	@Override
	public YamlConfigOptions copyHeader(boolean value) {
		super.copyHeader(value);
		return this;
	}
	
	@Override
	public YamlConfigOptions headerFormatter(CommentFormatter headerFormatter) {
		Checker.checkArgument(headerFormatter instanceof YamlHeaderFormatter, "The header formatter must inherit YamlHeaderFormatter");
		super.headerFormatter(headerFormatter);
		return this;
	}
	
	@Override
	public YamlHeaderFormatter headerFormatter() {
		return (YamlHeaderFormatter) super.headerFormatter();
	}
	
	/**
	 * Sets how much spaces should be used to indent each line.
	 * <br>
	 * The minimum value this may be is 2, and the maximum is 9.
	 *
	 * @param value New indent
	 * @return This object, for chaining
	 */
	
	@Override
	public YamlConfigOptions indent(int value) {
		Checker.checkArgument(value >= 2, "Indent must be at least 2 characters");
		Checker.checkArgument(value <= 9, "Indent cannot be greater than 9 characters");
		
		super.indent(value);
		
		return this;
	}
	
	/**
	 * Gets how much spaces should be used to indent each list element, in addition to the line indent.
	 *
	 * @return the list elements indentation
	 */
	
	public int indentList() {
		return this.indentList;
	}
	
	/**
	 * Sets how much spaces should be used to indent each list element, in addition to the line indent.
	 * <br>
	 * The minimum value this may be is 0, and the maximum is the same as the {@link YamlConfigOptions#indent()}.
	 *
	 * @param value New list indentation
	 * @return This object, for chaining
	 */
	
	public YamlConfigOptions indentList(int value) {
		Checker.checkArgument(value >= 0, "List indent must be at least 0 characters");
		Checker.checkArgument(value <= this.indent(), "List indent cannot be greater than the indent");
		
		this.indentList = value;
		
		return this;
	}
	
	/**
	 * Gets the comment formatter used to format comments.
	 * <br>
	 * The default comment formatter is {@link YamlCommentFormat#DEFAULT}, which comment prefix is "# ", i.e. a # followed by a space.
	 *
	 * @return the comment formatter
	 */
	
	public YamlCommentFormatter commentFormatter() {
		if(this.commentFormatter == null) {
			this.commentFormatter = YamlCommentFormat.DEFAULT.commentFormatter();
		}
		return this.commentFormatter;
	}
	
	/**
	 * Sets the comment formatter to be used to format comments.
	 * <br>
	 * If unset, the default comment formatter prefix is "# ", i.e. a # followed by a space.
	 *
	 * @param commentFormatter the comment formatter to use
	 * @return This object, for chaining
	 */
	
	public YamlConfigOptions commentFormatter(YamlCommentFormatter commentFormatter) {
		this.commentFormatter = commentFormatter;
		return this;
	}
	
	/**
	 * Sets if parsing comments is needed.
	 * <br>If you don't use comments in your configuration file keep this disabled to improve parsing performance.</p>
	 * Default is false.
	 * <br>
	 * With {@link YamlFile} it is updated automatically when you load a file with comments or set new comments programmatically.
	 *
	 * @param useComments if parsing comments is needed
	 * @return This object, for chaining
	 */
	
	public YamlConfigOptions useComments(boolean useComments) {
		this.useComments = useComments;
		return this;
	}
	
	/**
	 * Indicates if parsing comments is enabled.
	 * <br>If you don't use comments in your configuration file keep this disabled to improve parsing performance.</p>
	 * Default is false.
	 * <br>
	 * With {@link YamlFile} it is updated automatically when you load a file with comments or set new comments programmatically.
	 *
	 * @return This object, for chaining
	 * @see #useComments(boolean)
	 */
	
	public boolean useComments() {
		return this.useComments;
	}
	
	/**
	 * Get the quote style default options.
	 * <br>
	 * You can change the default quote style globally or for specific value types.
	 *
	 * @return the quote style default options
	 * @see QuoteStyle#PLAIN
	 * @see QuoteStyle#SINGLE
	 * @see QuoteStyle#DOUBLE
	 */
	
	public QuoteStyleDefaults quoteStyleDefaults() {
		return this.quoteStyleDefaults;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof YamlConfigOptions that)) return false;
		if(!super.equals(o)) return false;
		return indentList == that.indentList && Objects.equals(commentFormatter, that.commentFormatter);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), indentList, commentFormatter);
	}
	
	/**
	 * Options to configure the default {@link QuoteStyle} for each type.
	 */
	
	public static final class QuoteStyleDefaults {
		
		private final Map<Class<?>, QuoteStyle> typeQuoteStyles = new HashMap<>();
		
		/**
		 * -- GETTER --
		 * Get the default quote style.
		 * This style is applied to all values.
		 *
		 * @return the default quote style
		 */
		
		@Getter
		private QuoteStyle defaultQuoteStyle = QuoteStyleDefaults.defaultQuoteStyle();
		
		private QuoteStyleDefaults() {
		}
		
		private static QuoteStyle defaultQuoteStyle() {
			return QuoteStyle.PLAIN;
		}
		
		/**
		 * Set the default quote style.
		 * This style is applied to all values.
		 *
		 * @return This object, for chaining
		 */
		
		public QuoteStyleDefaults setDefaultQuoteStyle(QuoteStyle defaultQuoteStyle) {
			if(defaultQuoteStyle == null) {
				defaultQuoteStyle = QuoteStyleDefaults.defaultQuoteStyle();
			}
			this.defaultQuoteStyle = defaultQuoteStyle;
			return this;
		}
		
		/**
		 * Set the default quote style for a specific type.
		 * <br>
		 * This style is applied to values which class is the specified class or is a child of that class.
		 * <br>
		 * Example:
		 * <br><pre>{@code
		 * options.setQuoteStyle(String.class, QuoteStyle.DOUBLE);
		 * yamlConfig.set("key", "This string will be set with double quote style");
		 * }</pre>
		 * <br>
		 * Set quoteStyle to null to set new values again with the default quote style.
		 *
		 * @param valueClass the specific class to override default quote style
		 * @param quoteStyle the quote style to apply
		 * @return This object, for chaining
		 */
		
		public QuoteStyleDefaults setQuoteStyle(Class<?> valueClass, QuoteStyle quoteStyle) {
			if(quoteStyle == null) {
				this.typeQuoteStyles.remove(valueClass);
			} else {
				this.typeQuoteStyles.put(valueClass, quoteStyle);
			}
			return this;
		}
		
		/**
		 * Get the quote style to apply to a specific type.
		 * <br>
		 * If it was not explicitly set using the {@link #setQuoteStyle(Class, QuoteStyle)} method
		 * then the {@link #getDefaultQuoteStyle()} is returned.
		 *
		 * @param valueClass the type class
		 * @return the quote style to apply to the specified class
		 */
		
		public QuoteStyle getQuoteStyle(Class<?> valueClass) {
			final QuoteStyle quoteStyle = this.getExplicitQuoteStyleInstanceOf(valueClass);
			return quoteStyle != null ? quoteStyle : this.getDefaultQuoteStyle();
		}
		
		/**
		 * Get the overridden quote styles for every specific type set.
		 *
		 * @return the quote styles to apply to every specified class
		 */
		
		public Map<Class<?>, QuoteStyle> getQuoteStyles() {
			return this.typeQuoteStyles;
		}
		
		/**
		 * Get the specific quote style explicitly set to apply to a specific type,
		 * or to an inherited class of that type.
		 * <br>
		 * If neither the valueClass nor a superclass of that type was explicitly set
		 * using the {@link #setQuoteStyle(Class, QuoteStyle)} method then null is returned.
		 *
		 * @param valueClass the specific class to override default quote style
		 * @return the quote style to apply to the specified class
		 */
		
		QuoteStyle getExplicitQuoteStyleInstanceOf(Class<?> valueClass) {
			QuoteStyle quoteStyle = this.typeQuoteStyles.get(valueClass);
			if(quoteStyle == null && valueClass != null) {
				for(Class<?> superClass : this.typeQuoteStyles.keySet()) {
					if(superClass.isAssignableFrom(valueClass)) {
						return this.typeQuoteStyles.get(superClass);
					}
				}
			}
			return quoteStyle;
		}
	}
}
