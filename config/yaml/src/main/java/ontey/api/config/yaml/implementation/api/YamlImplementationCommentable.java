package ontey.api.config.yaml.implementation.api;

import ontey.api.config.comment.CommentType;
import ontey.api.config.comment.Commentable;
import ontey.api.config.yaml.comment.YamlCommentMapper;
import ontey.api.config.yaml.file.YamlConfigOptions;

/**
 * A YAML implementation capable of processing comments.
 */

public abstract class YamlImplementationCommentable implements YamlImplementation, Commentable {
	
	/**
	 * A comment mapper to add comments to sections or values
	 **/
	
	protected YamlCommentMapper yamlCommentMapper;
	
	/**
	 * Configuration options for loading and dumping YAML.
	 */
	
	protected YamlConfigOptions options;
	
	@Override
	public void setComment(String path, String comment, CommentType type) {
		if(this.yamlCommentMapper != null) {
			this.yamlCommentMapper.setComment(path, comment, type);
		}
	}
	
	@Override
	public String getComment(String path, CommentType type) {
		if(this.yamlCommentMapper == null) {
			return null;
		}
		return this.yamlCommentMapper.getComment(path, type);
	}
	
	/**
	 * Get the comment mapper to get or set comments.
	 *
	 * @return the comment mapper or null if parsing comments is not enabled
	 */
	
	public YamlCommentMapper getCommentMapper() {
		return this.yamlCommentMapper;
	}
	
	@Override
	public void configure(YamlConfigOptions options) {
		this.options = options;
	}
}
