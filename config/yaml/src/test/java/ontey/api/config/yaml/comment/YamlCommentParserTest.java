package ontey.api.config.yaml.comment;

import ontey.api.config.comment.CommentType;
import ontey.api.config.yaml.comment.format.YamlCommentFormat;
import ontey.api.config.yaml.file.YamlConfig;
import ontey.api.config.yaml.file.YamlConfigOptions;
import ontey.api.config.yaml.file.YamlFile;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

final class YamlCommentParserTest {
	
	@Test
	void parse() throws IOException {
		final StringReader reader = new StringReader(YamlCommentReaderTest.COMMENT);
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = configuration.options();
		
		final YamlCommentParser parser = new YamlCommentParser(options, reader);
		parser.parse();
		
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("te'st"),
		  new IsEqual<>("test comment")
		);
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("test-section"),
		  new IsEqual<>("test-section # comment # character\n  - multiline comment # character")
		);
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("test-section", CommentType.SIDE),
		  new IsEqual<>("comment # character")
		);
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("test-section.test", CommentType.SIDE),
		  new IsEqual<>("comment # character")
		);
		
		options.commentFormatter().stripPrefix(false);
		
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("test-section"),
		  new IsEqual<>("# test-section # comment # character\n#   - multiline comment # character")
		);
		
		options.commentFormatter().trim(false);
		
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("test-section"),
		  new IsEqual<>("\n# test-section # comment # character\n#   - multiline comment # character ")
		);
		
		options.commentFormatter().stripPrefix(true);
		
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("test-section"),
		  new IsEqual<>("\ntest-section # comment # character\n  - multiline comment # character ")
		);
		
		YamlCommentFormat.reset();
	}
	
	@Test
	void parseTag() throws IOException {
		final StringReader reader = new StringReader("tag: !!comment ' # not a comment'\n");
		final YamlConfig configuration = new YamlConfig();
		
		final YamlCommentParser parser = new YamlCommentParser(configuration.options(), reader);
		parser.parse();
		
		MatcherAssert.assertThat(
		  "Comments are wrong!",
		  parser.getComment("tag", CommentType.SIDE),
		  new IsNull<>()
		);
	}
	
	@Test
	void testExplicitStyle() throws IOException {
		YamlFile yamlFile = YamlFile.loadConfigurationFromString("""
		  # block comment
		  ? - key 1 # comment 1
		    - key 2 # comment 2
		  : - list value # comment 3""", true);
		
		String output = """
		  # block comment
		  # comment 1
		  # comment 2
		  '[key 1, key 2]':
		    - list value # comment 3
		  """;
		
		MatcherAssert.assertThat(
		  "Couldn't parse the comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
		
		MatcherAssert.assertThat(
		  "Couldn't parse the comments!",
		  yamlFile.getComment("[key 1, key 2]", CommentType.BLOCK),
		  new IsEqual<>("block comment\ncomment 1\ncomment 2")
		);
		
		MatcherAssert.assertThat(
		  "Couldn't parse the comments!",
		  yamlFile.getComment("[key 1, key 2][0]", CommentType.SIDE),
		  new IsEqual<>("comment 3")
		);
		
		yamlFile = YamlFile.loadConfigurationFromString("# block comment\n? blank\n?\n:", true);
		
		MatcherAssert.assertThat(
		  "Couldn't parse the comments!",
		  yamlFile.getCommentMapper().getComment("blank"),
		  new IsEqual<>("block comment")
		);
		
		MatcherAssert.assertThat(
		  "Couldn't parse the comments!",
		  yamlFile.getCommentMapper().getComment(""),
		  new IsNull<>()
		);
		
		yamlFile = YamlFile.loadConfigurationFromString(
		  """
			 # block comment
			 ? | # comment 1
			     key
			 : - list value # comment 2""", true);
		
		output = """
		  # block comment
		  # comment 1
		  ? |
		    key
		  :   - list value # comment 2
		  """;
		
		MatcherAssert.assertThat(
		  "Couldn't parse the comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
	}
}
