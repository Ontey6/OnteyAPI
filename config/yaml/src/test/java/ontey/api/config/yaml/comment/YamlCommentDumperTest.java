package ontey.api.config.yaml.comment;

import ontey.api.config.comment.CommentType;
import ontey.api.config.comment.KeyTree;
import ontey.api.config.util.StringUtils;
import ontey.api.config.yaml.file.YamlConfig;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

class YamlCommentDumperTest {
	
	@Test
	void dump() throws IOException {
		final String content =
		  """
			 test: 'test'
			 test-2: 'test-2'
			 test-3: 'test-3 #'
			 """;
		final YamlConfig configuration = new YamlConfig();
		final YamlCommentMapper mapper = new YamlCommentMapper(configuration.options());
		mapper.setComment("test", "test comment");
		mapper.setComment("test", "test comment", CommentType.SIDE);
		mapper.setComment("test-2", "test comment");
		mapper.setComment("test-2", "test comment", CommentType.SIDE);
		mapper.setComment("test-3", "test # comment");
		mapper.setComment("test-3", "test # comment", CommentType.SIDE);
		final StringWriter output = new StringWriter();
		final YamlCommentDumper dumper = new YamlCommentDumper(mapper, (writer -> {
			for(String line : StringUtils.lines(content)) {
				writer.write(line);
				writer.write('\n');
			}
		}), output);
		
		dumper.dump();
		
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  StringUtils.stripCarriage(output.toString()),
		  new IsEqual<>(
			 """
				# test comment
				test: 'test' # test comment
				# test comment
				test-2: 'test-2' # test comment
				# test # comment
				test-3: 'test-3 #' # test # comment
				"""
		  )
		);
	}
	
	@Test
	void getNode() throws IOException {
		final YamlConfig configuration = YamlConfig.loadConfigurationFromString("test: 'test'");
		final YamlCommentMapper mapper = new YamlCommentMapper(configuration.options());
		mapper.setComment("test", "test comment");
		mapper.setComment("test", "test comment", CommentType.SIDE);
		final YamlCommentDumper dumper = new YamlCommentDumper(mapper, configuration::dump, new StringWriter());
		final KeyTree.Node testNode = dumper.getNode("test");
		
		MatcherAssert.assertThat(
		  "The indention is not 0!",
		  testNode.getIndent(),
		  new IsEqual<>(0)
		);
		MatcherAssert.assertThat(
		  "The node name is not correct!",
		  testNode.getName(),
		  new IsEqual<>("test")
		);
		MatcherAssert.assertThat(
		  "The comment's node is wrong",
		  testNode.getComment(),
		  new IsEqual<>("# test comment")
		);
		MatcherAssert.assertThat(
		  "The side comment's node is wrong",
		  testNode.getSideComment(),
		  new IsEqual<>(" # test comment")
		);
	}
}
