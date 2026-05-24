package ontey.api.config.yaml.comment.format;

import lombok.NonNull;
import ontey.api.config.comment.CommentType;
import ontey.api.config.comment.KeyTree;

/**
 * {@link YamlCommentFormat#PRETTY} formatter
 */

public class PrettyYamlCommentFormatter extends YamlCommentFormatter {
	
	public PrettyYamlCommentFormatter() {
		this(new YamlCommentFormatterConfig());
	}
	
	public PrettyYamlCommentFormatter(YamlCommentFormatterConfig blockFormatter) {
		this(blockFormatter, new YamlSideCommentFormatterConfig());
	}
	
	public PrettyYamlCommentFormatter(YamlCommentFormatterConfig blockFormatter, YamlSideCommentFormatterConfig sideFormatter) {
		super(blockFormatter, sideFormatter);
		this.stripPrefix(true).trim(true);
	}
	
	@Override
	public String dump(String comment, @NonNull CommentType type, KeyTree.Node node) {
		if(type == CommentType.BLOCK && node != null && node.getIndent() == 0 && !node.isFirstNode()) { // Block comment for root keys except the first key
			final YamlCommentFormatterConfig blockCommentFormatterConfiguration = this.formatterConfiguration(CommentType.BLOCK);
			final String defaultPrefixFirst = blockCommentFormatterConfiguration.prefixFirst();
			final String defaultPrefixMultiline = blockCommentFormatterConfiguration.prefixMultiline();
			
			// Prepend default first prefix with a blank line
			blockCommentFormatterConfiguration.prefix('\n' + defaultPrefixFirst, defaultPrefixMultiline);
			
			final String dump = super.dump(comment, type, node);
			
			// Reset first prefix to default
			blockCommentFormatterConfiguration.prefix(defaultPrefixFirst, defaultPrefixMultiline);
			
			return dump;
		}
		return super.dump(comment, type, node);
	}
}
