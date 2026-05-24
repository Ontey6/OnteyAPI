package ontey.api.config.yaml.comment.format;

import lombok.NonNull;
import ontey.api.check.Checker;

public class YamlSideCommentFormatterConfig extends YamlCommentFormatterConfig {
	
	public static final String DEFAULT_SIDE_COMMENT_PREFIX = " " + DEFAULT_COMMENT_PREFIX;
	
	public YamlSideCommentFormatterConfig() {
		this(DEFAULT_SIDE_COMMENT_PREFIX);
	}
	
	public YamlSideCommentFormatterConfig(String sidePrefix) {
		super(sidePrefix);
	}
	
	public YamlSideCommentFormatterConfig(String sidePrefix, String prefixMultiline) {
		super(sidePrefix, prefixMultiline);
	}
	
	@Override
	public YamlSideCommentFormatterConfig prefix(String sidePrefix) {
		super.prefix(sidePrefix, DEFAULT_SIDE_COMMENT_PREFIX);
		return this;
	}
	
	@Override
	public YamlSideCommentFormatterConfig prefix(String prefixFirst, String prefixMultiline) {
		super.prefix(prefixFirst, prefixMultiline);
		return this;
	}
	
	@Override
	public YamlSideCommentFormatterConfig suffix(String suffixLast) {
		super.suffix(suffixLast);
		return this;
	}
	
	@Override
	public YamlSideCommentFormatterConfig suffix(String suffixLast, String suffixMultiline) {
		super.suffix(suffixLast, suffixMultiline);
		return this;
	}
	
	@Override
	public YamlSideCommentFormatterConfig trim(boolean trim) {
		super.trim(trim);
		return this;
	}
	
	@Override
	public YamlSideCommentFormatterConfig stripPrefix(boolean stripPrefix) {
		super.stripPrefix(stripPrefix);
		return this;
	}
	
	@Override
	protected void checkCommentPrefix(@NonNull String sidePrefix) {
		Checker.checkArgument(!sidePrefix.isEmpty() && Character.isWhitespace(sidePrefix.charAt(0)), "Side comment prefix must start with space");
		super.checkCommentPrefix(sidePrefix);
	}
}
