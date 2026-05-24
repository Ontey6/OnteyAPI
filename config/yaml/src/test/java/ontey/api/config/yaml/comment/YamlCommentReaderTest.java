package ontey.api.config.yaml.comment;

import ontey.api.config.comment.KeyTree;
import ontey.api.config.yaml.file.YamlConfig;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsTrue;

import java.io.IOException;
import java.io.StringReader;

final class YamlCommentReaderTest {
	
	static final String COMMENT = """
	  #test comment
	  'te''st': 'test'
	  
	  # test-section # comment # character
	  #   - multiline comment # character\s
	  test-section: # comment # character
	    test: 'test # character' # comment # character
	  """;
	
	@Test
	void isBlank() throws IOException {
		final StringReader reader = new StringReader(YamlCommentReaderTest.COMMENT);
		final YamlConfig configuration = new YamlConfig();
		final YamlCommentReader commentReader = new YamlCommentParser(configuration.options(), reader);
		commentReader.nextLine();
		
		MatcherAssert.assertThat(
		  "The text is blank!",
		  commentReader.isBlank(),
		  new IsNot<>(new IsTrue())
		);
		
		commentReader.nextLine();
		commentReader.nextLine();
		
		MatcherAssert.assertThat(
		  "The text is not blank!",
		  commentReader.isBlank(),
		  new IsTrue()
		);
	}
	
	@Test
	void isComment() throws IOException {
		final StringReader reader = new StringReader(YamlCommentReaderTest.COMMENT);
		final YamlConfig configuration = new YamlConfig();
		final YamlCommentReader commentReader = new YamlCommentParser(configuration.options(), reader);
		commentReader.nextLine();
		final boolean comment = commentReader.isComment();
		
		MatcherAssert.assertThat(
		  "The text is not a comment!",
		  comment,
		  new IsTrue()
		);
	}
	
	@Test
	void nextLine() throws IOException {
		final StringReader reader = new StringReader(YamlCommentReaderTest.COMMENT);
		final YamlConfig configuration = new YamlConfig();
		final YamlCommentReader commentReader = new YamlCommentParser(configuration.options(), reader);
		final boolean nextLine = commentReader.nextLine();
		
		MatcherAssert.assertThat(
		  "The text has not a next line!",
		  nextLine,
		  new IsTrue()
		);
	}
	
	@Test
	void track() throws IOException {
		final StringReader reader = new StringReader(YamlCommentReaderTest.COMMENT);
		final YamlConfig configuration = new YamlConfig();
		final YamlCommentReader commentReader = new YamlCommentParser(configuration.options(), reader);
		commentReader.nextLine();
		final KeyTree.Node track = commentReader.track(0, "a");
		
		MatcherAssert.assertThat(
		  "Wrong indentation!",
		  track.getIndent(),
		  new IsEqual<>(0)
		);
		MatcherAssert.assertThat(
		  "The node's name is not correct!",
		  track.getName(),
		  new IsEqual<>("a")
		);
		MatcherAssert.assertThat(
		  "There is a comment!",
		  track.getComment(),
		  new IsNull<>()
		);
		MatcherAssert.assertThat(
		  "There is a side comment!",
		  track.getSideComment(),
		  new IsNull<>()
		);
	}
}
