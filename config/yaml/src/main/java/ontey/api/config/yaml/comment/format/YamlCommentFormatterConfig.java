package ontey.api.config.yaml.comment.format;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ontey.api.check.Checker;
import ontey.api.config.comment.format.CommentFormatterConfig;
import ontey.api.config.util.StringUtils;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class YamlCommentFormatterConfig extends CommentFormatterConfig {
	
	public static final String COMMENT_INDICATOR = "#";
	
	public static final String DEFAULT_COMMENT_PREFIX = COMMENT_INDICATOR + " ";
	
	private boolean stripPrefix = false;
	
	private boolean trim = true;
	
	public YamlCommentFormatterConfig() {
		this(DEFAULT_COMMENT_PREFIX);
	}
	
	public YamlCommentFormatterConfig(String prefix) {
		this.prefix(prefix);
	}
	
	public YamlCommentFormatterConfig(String prefix, String prefixMultiline) {
		this.prefix(prefix, prefixMultiline);
	}
	
	@Override
	public YamlCommentFormatterConfig prefix(String prefix) {
		checkCommentPrefix(prefix);
		super.prefix(prefix, prefix);
		return this;
	}
	
	@Override
	public YamlCommentFormatterConfig prefix(String prefixFirst, String prefixMultiline) {
		checkCommentPrefix(prefixFirst);
		checkCommentPrefixMultiline(prefixMultiline);
		super.prefix(prefixFirst, prefixMultiline);
		return this;
	}
	
	@Override
	public YamlCommentFormatterConfig suffix(String suffixLast) {
		checkCommentSuffix(suffixLast);
		super.suffix(suffixLast);
		return this;
	}
	
	@Override
	public YamlCommentFormatterConfig suffix(String suffixLast, String suffixMultiline) {
		checkCommentSuffix(suffixLast);
		checkCommentSuffixMultiline(suffixMultiline);
		super.suffix(suffixLast, suffixMultiline);
		return this;
	}
	
	public YamlCommentFormatterConfig stripPrefix(boolean stripPrefix) {
		this.stripPrefix = stripPrefix;
		return this;
	}
	
	public boolean stripPrefix() {
		return this.stripPrefix;
	}
	
	public YamlCommentFormatterConfig trim(boolean trim) {
		this.trim = trim;
		return this;
	}
	
	public boolean trim() {
		return this.trim;
	}
	
	protected void checkCommentPrefix(@NonNull String commentPrefix) {
		final String[] prefixLines = StringUtils.lines(commentPrefix, false);
		final int lastLineIndex = prefixLines.length - 1;
		
		for(int i = 0; i <= lastLineIndex; i++) {
			final String line = prefixLines[i].trim();
			if(i == lastLineIndex && !line.startsWith(COMMENT_INDICATOR)) {
				throw new IllegalArgumentException("Last prefix line must be optional space followed by a " + COMMENT_INDICATOR);
			} else if(!(line.isEmpty() || line.startsWith(COMMENT_INDICATOR))) {
				throw new IllegalArgumentException("All comment prefix lines must be blank or optional space followed by a " + COMMENT_INDICATOR);
			}
		}
	}
	
	protected void checkCommentPrefixMultiline(String commentPrefix) {
		this.checkCommentPrefix(commentPrefix);
	}
	
	protected void checkCommentSuffix(@NonNull String commentSuffix) {
		Checker.checkArgument(
		  StringUtils.allLinesArePrefixedOrBlank(StringUtils.afterNewLine(commentSuffix), COMMENT_INDICATOR),
		  "All comment suffix lines must be blank or optional space followed by a " + COMMENT_INDICATOR
		);
	}
	
	protected void checkCommentSuffixMultiline(String commentSuffix) {
		this.checkCommentSuffix(commentSuffix);
	}
}
