package ontey.api.config.yaml.implementation.snakeyaml;

import ontey.api.config.comment.KeyTree;
import ontey.api.config.comment.format.CommentFormatterConfig;
import ontey.api.config.util.StringUtils;
import ontey.api.config.yaml.comment.YamlCommentMapper;
import ontey.api.config.yaml.comment.YamlCommentParser;
import ontey.api.config.yaml.comment.format.YamlCommentFormatterConfig;
import ontey.api.config.yaml.file.YamlConfigOptions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.nodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SnakeYamlCommentMapper extends YamlCommentMapper {
	
	protected KeyTree.Node currentNode;
	
	protected String sideCommentPrefix;
	
	protected boolean headerParsed = false;
	
	protected SnakeYamlCommentMapper(YamlConfigOptions options) {
		super(options);
		this.setCommentPrefix(options);
	}
	
	protected void setCommentPrefix(YamlConfigOptions options) {
		final CommentFormatterConfig sideFormatterConfiguration = options.commentFormatter()
		  .formatterConfiguration(ontey.api.config.comment.CommentType.SIDE);
		this.sideCommentPrefix = sideFormatterConfiguration.prefixFirst(YamlCommentFormatterConfig.COMMENT_INDICATOR);
		
		if(this.sideCommentPrefix.endsWith(" ")) {
			this.sideCommentPrefix = this.sideCommentPrefix.substring(0, this.sideCommentPrefix.length() - 1);
		}
	}
	
	protected KeyTree.Node track(KeyTree.Node parent, String name, Node key, Node value) {
		final int indent = key.getStartMark().getColumn();
		
		final KeyTree.Node commentNode = this.track(parent, indent, name);
		
		this.trackBlockComment(commentNode, key.getBlockComments());
		
		this.trackSideComment(commentNode, this.getSideCommentNode(key, value).getInLineComments());
		
		if(value instanceof SequenceNode) {
			commentNode.isList(((SequenceNode) value).getValue().size());
		}
		
		return commentNode;
	}
	
	protected KeyTree.Node trackElement(KeyTree.Node parent, String name, Node key, int elementIndex) {
		final KeyTree.Node commentNode = this.track(parent, name, key, null);
		
		if(elementIndex >= 0) {
			commentNode.setElementIndex(elementIndex);
		}
		
		return commentNode;
	}
	
	protected KeyTree.Node track(KeyTree.Node parent, int indent, String key) {
		if(parent == null) {
			parent = this.keyTree.findParent(indent);
		}
		this.currentNode = parent.add(indent, key);
		return this.currentNode;
	}
	
	protected void trackBlockComment(KeyTree.Node node, List<CommentLine> comments) {
		if(node != null && comments != null && !comments.isEmpty()) {
			final String indent = StringUtils.indentation(node.getIndent());
			final String commentPrefix = indent + YamlCommentFormatterConfig.COMMENT_INDICATOR;
			
			String blockComment = this.getComment(comments, commentPrefix, commentPrefix);
			
			if(blockComment != null) {
				if(!this.headerParsed) {
					// Remove header from first key comment
					blockComment = YamlCommentParser.removeHeader(blockComment, this.options());
					this.headerParsed = true;
				}
				this.setRawComment(node, blockComment, ontey.api.config.comment.CommentType.BLOCK);
			}
		}
	}
	
	protected void trackSideComment(KeyTree.Node node, List<CommentLine> comments) {
		if(node != null && comments != null && !comments.isEmpty()) {
			String multilineCommentPrefix = null;
			
			if(comments.size() > 1) {
				final String indent = StringUtils.indentation(node.getIndent());
				multilineCommentPrefix = indent + YamlCommentFormatterConfig.COMMENT_INDICATOR;
			}
			
			final String sideComment = this.getComment(comments, this.sideCommentPrefix, multilineCommentPrefix);
			
			if(sideComment != null) {
				this.setRawComment(node, sideComment, ontey.api.config.comment.CommentType.SIDE);
			}
		}
	}
	
	protected void trackFooter(MappingNode node) {
		if(node != null) {
			final List<CommentLine> comments = node.getEndComments();
			
			if(comments != null && !comments.isEmpty()) {
				final String commentPrefix = YamlCommentFormatterConfig.COMMENT_INDICATOR;
				
				final String footerComment = this.getComment(comments, commentPrefix, commentPrefix);
				
				if(footerComment != null) {
					final KeyTree.Node footerNode = this.track(this.getKeyTree().getRoot(), 0, null);
					
					if(footerNode != null) {
						footerNode.setComment(footerComment);
					}
				}
			}
		}
	}
	
	protected String getComment(List<CommentLine> commentLines, String firstCommentPrefix, String multilineCommentPrefix) {
		final StringBuilder commentBuilder = new StringBuilder();
		
		final Iterator<CommentLine> it = commentLines.iterator();
		boolean hasNext = it.hasNext();
		boolean last;
		
		if(hasNext) {
			CommentLine commentLine = it.next();
			
			hasNext = it.hasNext();
			last = !hasNext;
			
			this.appendLine(commentBuilder, firstCommentPrefix, commentLine, last);
			
			while(hasNext) {
				commentLine = it.next();
				hasNext = it.hasNext();
				last = !hasNext;
				this.appendLine(commentBuilder, multilineCommentPrefix, commentLine, last);
			}
		}
		
		return commentBuilder.toString();
	}
	
	protected void appendLine(StringBuilder commentBuilder, String commentPrefix, CommentLine commentLine, boolean last) {
		if(commentLine.getCommentType() != CommentType.BLANK_LINE) {
			commentBuilder.append(commentPrefix).append(commentLine.getValue());
		}
		if(!last) {
			commentBuilder.append('\n');
		}
	}
	
	protected List<CommentLine> getCommentLines(String comment, CommentType commentType) {
		if(comment == null) {
			return null;
		}
		
		final List<CommentLine> commentLines = new ArrayList<>();
		
		final String[] lines = StringUtils.lines(comment, false);
		
		for(int i = 0; i < lines.length; i++) {
			String line = StringUtils.stripIndentation(lines[i]);
			
			boolean isBlank = line.isEmpty();
			
			if(line.startsWith(YamlCommentFormatterConfig.COMMENT_INDICATOR)) {
				line = line.substring(1);
			} else if(isBlank) {
				if(i == 0 && commentType == CommentType.IN_LINE) {
					continue; // side comment below
				}
				line = "\n";
			}
			
			CommentType lineCommentType = commentType;
			
			if(isBlank) {
				lineCommentType = CommentType.BLANK_LINE;
			} else if(i > 0 && commentType == CommentType.IN_LINE) {
				lineCommentType = CommentType.BLOCK;
			}
			
			commentLines.add(new CommentLine(null, null, line, lineCommentType));
		}
		
		return commentLines;
	}
	
	protected void setComments(KeyTree.Node node, Node key, Node value) {
		if(node != null) {
			key.setBlockComments(this.getCommentLines(node.getComment(), CommentType.BLOCK));
			
			this.getSideCommentNode(key, value).setInLineComments(this.getCommentLines(node.getSideComment(), CommentType.IN_LINE));
		}
	}
	
	protected void setFooter(MappingNode node) {
		if(node != null) {
			final KeyTree.Node footerNode = this.getNode(null);
			
			if(footerNode != null) {
				node.setEndComments(this.getCommentLines(footerNode.getComment(), CommentType.BLOCK));
			}
		}
	}
	
	protected Node getSideCommentNode(Node key, Node value) {
		Node sideCommentNode = key;
		
		if(value instanceof ScalarNode) {
			DumperOptions.ScalarStyle scalarStyle = ((ScalarNode) value).getScalarStyle();
			if(scalarStyle != DumperOptions.ScalarStyle.LITERAL && scalarStyle != DumperOptions.ScalarStyle.FOLDED) {
				sideCommentNode = value;
			}
		} else if(value != null && !(value instanceof CollectionNode)) {
			sideCommentNode = value;
		}
		
		return sideCommentNode;
	}
	
	protected void clearCurrentNodeIfNoComments() {
		super.clearNodeIfNoComments(this.currentNode);
		this.currentNode = null;
	}
	
	@Override
	protected KeyTree.Node getPriorityNode(String path) {
		return super.getPriorityNode(path);
	}
}
