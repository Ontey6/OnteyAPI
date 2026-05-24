package ontey.api.config.yaml.comment;

import lombok.NonNull;
import ontey.api.config.comment.KeyTree;
import ontey.api.config.yaml.file.YamlConfigOptions;

public class YamlKeyTree extends KeyTree {
	
	public YamlKeyTree(@NonNull YamlConfigOptions options) {
		super(options);
	}
	
	@Override
	public YamlConfigOptions options() {
		return (YamlConfigOptions) this.options;
	}
	
	@Override
	protected KeyTree.Node createNode(KeyTree.Node parent, int indent, String key) {
		return new YamlCommentNode(parent, indent, key);
	}
	
	public class YamlCommentNode extends KeyTree.Node {
		
		YamlCommentNode(Node parent, int indent, String name) {
			super(parent, indent, name);
		}
		
		@Override
		protected KeyTree.Node add(String key, boolean priority) {
			int indent = 0;
			if(this != YamlKeyTree.this.root) {
				indent = this.indent;
				if(this.isList)
					indent += YamlKeyTree.this.options().indentList();
				else
					indent += YamlKeyTree.this.options.indent();
			}
			return this.add(indent, key, priority);
		}
		
		@Override
		public void isList(int listSize) {
			super.isList(listSize);
			
			if(this.parent != null && this.parent.isList())
				this.indent = this.parent.getIndent() + YamlKeyTree.this.options().indentList() + 2; // "- " prefix
		}
	}
}
