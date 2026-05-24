package ontey.api.config.yaml.comment.format;

public enum YamlCommentFormat {
	
	/**
	 * DEFAULT comment format gets comments in a readable way stripping the # comment prefix,
	 * without leading and trailing spaces or blank lines and without indentation.
	 * <br>
	 * The prefix for setting new comments is:
	 * <br>  - BLOCK comments: "# ", i.e. a # followed by a space.
	 * <br>  - SIDE comments: " # ", i.e. a space followed by # with a space.
	 * <br>
	 * If when setting a comment all lines are blank or already prefixed with a comment prefix # then it does not add additional formatting.
	 */
	
	DEFAULT(YamlCommentFormatter::new),
	
	/**
	 * PRETTY comment format gets comments in a readable way stripping the # comment prefix,
	 * without leading and trailing spaces or blank lines and without indentation.
	 * <br>
	 * The prefix for setting new comments is:
	 * <br>  - BLOCK comments with 0 indentation (root keys) except first key: "\n# ", i.e. a blank line followed by # with a space.
	 * Multiline comments do not add additional blank lines.
	 * <br>  - BLOCK comments with some indentation (child keys) or first key: "# ", i.e. a # followed by a space.
	 * <br>  - SIDE comments: " # ", i.e. a space followed by # with a space.
	 * <br>
	 * If when setting a comment all lines are blank or already prefixed with a comment prefix # then it does not add additional formatting.
	 */
	
	PRETTY(PrettyYamlCommentFormatter::new),
	
	/**
	 * BLANK_LINE comment format gets comments in a readable way stripping the # comment prefix and without indentation,
	 * but it keeps trailing spaces and blank lines.
	 * <br>
	 * The prefix for setting new comments is:
	 * <br>  - BLOCK comments: "\n# ", i.e. a blank line followed by # with a space.
	 * Multiline comments do not add additional blank lines.
	 * <br>  - SIDE comments: "\n# ", i.e. a new line followed by # with a space.
	 * This will add the comment below.
	 * <br>
	 * If when setting a comment all lines are blank or already prefixed with a comment prefix # then it does not add additional formatting.
	 */
	
	BLANK_LINE(BlankLineYamlCommentFormatter::new),
	
	/**
	 * RAW comment format gets comments as they are in the file configuration,
	 * with blank lines and the comment prefix with # character,
	 * but without the indentation prefix.
	 * <br>
	 * The prefix for setting new comments is:
	 * <br>  - BLOCK comments: "# ", i.e. a # followed by a space.
	 * <br>  - SIDE comments: " # ", i.e. a space followed by # with a space.
	 * <br>
	 * If when setting a comment all lines are blank or already prefixed with a comment prefix # then it does not add additional formatting.
	 */
	
	RAW(() -> new YamlCommentFormatter().stripPrefix(false).trim(false));
	
	private final YamlCommentFormatterFactory yamlCommentFormatterFactory;
	
	private YamlCommentFormatter yamlCommentFormatter;
	
	YamlCommentFormat(YamlCommentFormatterFactory yamlCommentFormatterFactory) {
		this.yamlCommentFormatterFactory = yamlCommentFormatterFactory;
	}
	
	public static void reset() {
		// Rebuild formatters to reset any changes
		for(YamlCommentFormat format : values()) {
			if(format.yamlCommentFormatter != null) {
				format.buildCommentFormatter();
			}
		}
	}
	
	public YamlCommentFormatter commentFormatter() {
		if(this.yamlCommentFormatter == null) {
			this.buildCommentFormatter();
		}
		return this.yamlCommentFormatter;
	}
	
	private void buildCommentFormatter() {
		this.yamlCommentFormatter = this.yamlCommentFormatterFactory.commentFormatter();
	}
	
	@FunctionalInterface
	public interface YamlCommentFormatterFactory {
		
		YamlCommentFormatter commentFormatter();
	}
}
