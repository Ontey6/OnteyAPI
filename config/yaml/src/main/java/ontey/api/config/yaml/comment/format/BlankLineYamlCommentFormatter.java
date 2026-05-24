package ontey.api.config.yaml.comment.format;

import lombok.NonNull;
import ontey.api.config.comment.CommentType;
import ontey.api.config.comment.KeyTree;
import ontey.api.config.util.StringUtils;

/**
 * {@link YamlCommentFormat#BLANK_LINE} formatter
 */

public class BlankLineYamlCommentFormatter extends YamlCommentFormatter {
	
	public BlankLineYamlCommentFormatter() {
		this(new YamlCommentFormatterConfig());
	}
	
	public BlankLineYamlCommentFormatter(YamlCommentFormatterConfig blockFormatter) {
		this(blockFormatter, new YamlSideCommentFormatterConfig());
	}
	
	public BlankLineYamlCommentFormatter(YamlCommentFormatterConfig blockFormatter, YamlSideCommentFormatterConfig sideFormatter) {
		super(blockFormatter, sideFormatter);
		this.stripPrefix(true).trim(false);
		blockFormatter.prefix('\n' + blockFormatter.prefixFirst(), blockFormatter.prefixMultiline());
	}
	
	@Override
	public String dump(String comment, @NonNull CommentType type, KeyTree.Node node) {
		if(type == CommentType.SIDE) {
			final String defaultPrefixFirst = sideFormatter.prefixFirst();
			final String blankLineSideFirstPrefix = '\n' + StringUtils.stripIndentation(defaultPrefixFirst);
			sideFormatter.prefix(blankLineSideFirstPrefix, sideFormatter.prefixMultiline());
			final String dump = super.dump(comment, type, node);
			sideFormatter.prefix(defaultPrefixFirst, sideFormatter.prefixMultiline());
			return dump;
		}
		return super.dump(comment, type, node);
	}
}
