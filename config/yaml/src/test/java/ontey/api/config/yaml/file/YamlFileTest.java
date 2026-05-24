package ontey.api.config.yaml.file;

import ontey.api.config.ConfigSection;
import ontey.api.config.comment.CommentType;
import ontey.api.config.comment.KeyTree;
import ontey.api.config.serialization.ConfigSerialization;
import ontey.api.config.util.StringUtils;
import ontey.api.config.yaml.comment.format.YamlCommentFormat;
import ontey.api.config.yaml.comment.format.YamlCommentFormatter;
import ontey.api.config.yaml.comment.format.YamlCommentFormatterConfig;
import ontey.api.config.yaml.comment.format.YamlHeaderFormatter;
import ontey.api.config.yaml.examples.Person;
import ontey.api.config.yaml.implementation.api.QuoteStyle;
import ontey.api.config.yaml.utils.TestResources;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsSame;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

class YamlFileTest {
	
	@Test
	@SuppressWarnings("deprecation")
	void load() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test.yml"));
		final String content = TestResources.testContent();
		yamlFile.load();
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile.load(new File(TestResources.getResourceURI("test.yml")));
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile.load(TestResources.getResourceURL("test.yml").openStream());
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile.load(() -> TestResources.getResourceURL("test.yml").openStream());
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void loadWithFolderSpaces() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("folder with space/test.yml"));
		final String content = TestResources.testContent();
		yamlFile.load();
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void loadConfiguration() throws Exception {
		YamlFile yamlFile = YamlFile.loadConfiguration(new File(TestResources.getResourceURI("test.yml")));
		final String content = TestResources.testContent();
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile = YamlFile.loadConfiguration(() -> TestResources.getResourceInputStream("test.yml"));
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile = YamlFile.loadConfiguration(() -> TestResources.getResourceInputStream("test.yml"));
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile = YamlFile.loadConfigurationFromString(content);
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void loadWithComments() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		yamlFile.loadWithComments();
		assertThat(
		  "Couldn't load the file with comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(TestResources.testComments())
		);
	}
	
	@Test
	void loadConfigurationWithComments() throws Exception {
		YamlFile yamlFile = YamlFile.loadConfiguration(new File(TestResources.getResourceURI("test-comments.yml")), true);
		final String content = TestResources.testComments();
		assertThat(
		  "Couldn't load the file with comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile = YamlFile.loadConfiguration(() -> TestResources.getResourceInputStream("test-comments.yml"), true);
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile = YamlFile.loadConfiguration(() -> TestResources.getResourceInputStream("test-comments.yml"), true);
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
		yamlFile = YamlFile.loadConfigurationFromString(content, true);
		assertThat(
		  "Couldn't load the file!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void createOrLoad() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		final String content = TestResources.testWithHeader();
		yamlFile.createOrLoad();
		assertThat(
		  "Couldn't load the file with comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void createOrLoadWithComments() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		final String content = TestResources.testComments();
		yamlFile.createOrLoadWithComments();
		assertThat(
		  "Couldn't load the file with comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void save() throws Exception {
		final File temp = TestResources.tempFile();
		//noinspection ResultOfMethodCallIgnored
		temp.delete();
		
		final YamlFile yamlFile = new YamlFile(temp);
		
		assertThat(
		  "File already exists!",
		  yamlFile.exists(),
		  new IsNot<>(new IsTrue())
		);
		
		final String content = "number: 5\n";
		yamlFile.set("number", 5);
		
		yamlFile.save();
		yamlFile.save(temp);
		yamlFile.save(temp.getAbsolutePath());
		
		assertThat(
		  "File has not being correctly saved!",
		  TestResources.fileToStringUnix(yamlFile),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void saveWithComments() throws Exception {
		final File temp = TestResources.tempFile();
		//noinspection ResultOfMethodCallIgnored
		temp.delete();
		
		final YamlFile yamlFile = new YamlFile(temp);
		
		assertThat(
		  "File already exists!",
		  yamlFile.exists(),
		  new IsNot<>(new IsTrue())
		);
		
		final String content = """
		  # Test
		  number: 5 # Side
		  """;
		
		yamlFile.set("number", 5);
		yamlFile.setComment("number", "Test");
		yamlFile.setComment("number", "Side", CommentType.SIDE);
		
		yamlFile.save();
		
		assertThat(
		  "File has not being correctly saved with comments!",
		  TestResources.fileToStringUnix(yamlFile),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void saveDefaults() throws Exception {
		final File temp = TestResources.tempFile();
		//noinspection ResultOfMethodCallIgnored
		temp.delete();
		
		final YamlFile yamlFile = new YamlFile(temp);
		
		assertThat(
		  "File already exists!",
		  yamlFile.exists(),
		  new IsNot<>(new IsTrue())
		);
		
		final String content = "number: 5\n";
		yamlFile.addDefault("number", 5);
		
		yamlFile.save();
		
		assertThat(
		  "Defaults have not being correctly saved!",
		  TestResources.fileToStringUnix(yamlFile),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void fileToString() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test.yml"));
		final String content = TestResources.testContent();
		
		assertThat(
		  "Couldn't get the content of the file (fileToString)!",
		  TestResources.fileToStringUnix(yamlFile),
		  new IsEqual<>(content)
		);
		
		yamlFile.load();
		yamlFile.set("test.number", 10);
		
		assertThat(
		  "fileToString must not change until save!",
		  TestResources.fileToStringUnix(yamlFile),
		  new IsEqual<>(content)
		);
		
		final String newContent = """
		  test:
		    number: 10
		    string: Hello world
		    boolean: true
		    list:
		      - Each
		      - word
		      - will
		      - be
		      - in
		      - a
		      - separated
		      - entry
		  math:
		    pi: 3.141592653589793
		  timestamp:
		    canonicalDate: 2020-07-04T13:18:04.458Z
		    formattedDate: 04/07/2020 15:18:04
		  """;
		
		yamlFile.setConfigurationFile(TestResources.tempFile());
		yamlFile.save();
		
		assertThat(
		  "Couldn't get the content of the file after save (fileToString)!",
		  TestResources.fileToStringUnix(yamlFile),
		  new IsEqual<>(newContent)
		);
	}
	
	@Test
	void saveToString() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test.yml"));
		yamlFile.load();
		final String content = TestResources.testContent();
		
		assertThat(
		  "Couldn't get the content of the file (saveToString)!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content));
		
		assertThat(
		  "Couldn't get the content of the file (toString)!",
		  yamlFile.toString(),
		  new IsEqual<>(content));
	}
	
	@Test
	void saveToStringWithComments() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		yamlFile.loadWithComments();
		
		final String content = TestResources.testComments();
		
		assertThat(
		  "Couldn't get the content of the file (saveToString)!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content));
	}
	
	@Test
	void saveToStringWithComments2() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments2.yml"));
		yamlFile.loadWithComments();
		
		final String content = TestResources.fileToStringUnix(yamlFile);
		
		assertThat(
		  "Couldn't get the content of the file (saveToString)!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content));
	}
	
	@Test
	void saveToStringWithComments3() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments3.yml"));
		yamlFile.loadWithComments();
		
		final String content = TestResources.fileToStringUnix(yamlFile);
		
		assertThat(
		  "Couldn't get the content of the file (saveToString)!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content));
	}
	
	@Test
	void saveToStringWithComments4() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments4.yml"));
		yamlFile.loadWithComments();
		
		final String content = TestResources.testCommentsSpecial();
		
		assertThat(
		  "Couldn't get the content of the file (saveToString)!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content));
	}
	
	@Test
	void getComment() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		yamlFile.loadWithComments();
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("test.string"),
		  new IsEqual<>("Hello!")
		);
		
		assertThat(
		  "Couldn't parse comments correctly!",
		  yamlFile.getComment("test.list.entry"),
		  new IsEqual<>("Comment on a list item")
		);
		
		assertThat(
		  "Couldn't parse the side comments correctly!",
		  yamlFile.getComment("test.list.entry", CommentType.SIDE),
		  new IsEqual<>(":)")
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("test.wrap"),
		  new IsEqual<>("This is a\nmultiline comment")
		);
		
		assertThat(
		  "Couldn't parse the side comments correctly!",
		  yamlFile.getComment("test.wrap", CommentType.SIDE),
		  new IsNull<>()
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("math", YamlCommentFormat.RAW),
		  new IsEqual<>("\n# Wonderful numbers")
		);
		
		assertThat(
		  "Couldn't parse the side comments below correctly!",
		  yamlFile.getComment("math.pi", CommentType.SIDE),
		  new IsEqual<>("Side comment below")
		);
		
		assertThat(
		  "Couldn't parse the side comments below correctly!",
		  yamlFile.getComment("math.pi", CommentType.SIDE, YamlCommentFormat.RAW),
		  new IsEqual<>("\n# Side comment below")
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("timestamp"),
		  new IsEqual<>("Some timestamps")
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("timestamp", YamlCommentFormat.RAW),
		  new IsEqual<>("\n# Some timestamps")
		);
	}
	
	@Test
	void getCommentEdgeCases() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments4.yml"));
		yamlFile.loadWithComments();
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("test. wrap "),
		  new IsEqual<>("Block #comment with # character")
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("test. wrap ", CommentType.SIDE),
		  new IsEqual<>("Side #comment with # character")
		);
		
		assertThat(
		  "Couldn't parse the values correctly!",
		  yamlFile.getString("test. wrap "),
		  new IsEqual<>(" # not a comment ")
		);
		
		yamlFile.getSection("test").getKeys(false)
		  .forEach((key) ->
			 assertThat(
				"Side comment mismatch (test." + key + ")",
				yamlFile.getComment("test." + key, CommentType.SIDE),
				new IsEqual<>("Side #comment with # character")));
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("blank.empty", CommentType.SIDE),
		  new IsEqual<>("Side #comment with # character\nMultiple line side comment")
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("test.entries.entry'#''", CommentType.SIDE),
		  new IsEqual<>(":)\ndangling comment")
		);
		
		final YamlFile yamlFile2 = new YamlFile();
		
		yamlFile2.path("wrap")
		  .set(" # not a comment", QuoteStyle.PLAIN)
		  .commentSide("Side #comment with # character");
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.getComment("wrap", CommentType.SIDE),
		  new IsEqual<>("Side #comment with # character")
		);
		
		assertThat(
		  "Couldn't parse the values correctly!",
		  yamlFile2.getString("wrap"),
		  new IsEqual<>(" # not a comment")
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.saveToString(),
		  new IsEqual<>("wrap: ' # not a comment' # Side #comment with # character\n"));
		
		yamlFile2.remove("wrap");
		
		yamlFile2.path("null")
		  .commentSide("# Side #comment with # character")
		  .set(null, QuoteStyle.SINGLE);
		
		String tagContents = "'null': !!null '' # Side #comment with # character\n";
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.getComment("null", CommentType.SIDE),
		  new IsEqual<>("Side #comment with # character")
		);
		
		assertThat(
		  "Couldn't parse the values correctly!",
		  yamlFile2.getString("null"),
		  new IsNull<>()
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.saveToString(),
		  new IsEqual<>(tagContents));
		
		yamlFile2.loadFromString(tagContents);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.getComment("null", CommentType.SIDE),
		  new IsEqual<>("Side #comment with # character")
		);
		
		assertThat(
		  "Couldn't parse the values correctly!",
		  yamlFile2.getString("null"),
		  new IsNull<>()
		);
		
		yamlFile2.remove("null");
		
		yamlFile2.options().quoteStyleDefaults().setQuoteStyle(Integer.class, QuoteStyle.SINGLE);
		
		yamlFile2.path("i").set(1).commentSide("Side #comment with # character");
		
		tagContents = "i: !!int '1' # Side #comment with # character\n";
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.getComment("i", CommentType.SIDE),
		  new IsEqual<>("Side #comment with # character")
		);
		
		assertThat(
		  "Couldn't parse the values correctly!",
		  yamlFile2.getInt("i"),
		  new IsEqual<>(1)
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.saveToString(),
		  new IsEqual<>(tagContents));
		
		yamlFile2.loadFromString(tagContents);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.getComment("i", CommentType.SIDE),
		  new IsEqual<>("Side #comment with # character")
		);
		
		assertThat(
		  "Couldn't parse the values correctly!",
		  yamlFile2.getInt("i"),
		  new IsEqual<>(1)
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile2.saveToString(),
		  new IsEqual<>(tagContents));
	}
	
	@Test
	void loadWithTag() throws IOException {
		final YamlFile configuration = new YamlFile();
		configuration.loadFromString("tag: !!str ' # not a comment'\n");
		
		assertThat(
		  "Comments are wrong!",
		  configuration.getComment("tag", CommentType.SIDE),
		  new IsNull<>()
		);
		
		assertThat(
		  "Value is wrong!",
		  configuration.get("tag"),
		  new IsEqual<>(" # not a comment")
		);
	}
	
	@Test
	void setComment() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		yamlFile.loadWithComments();
		
		yamlFile.setComment("test.string", "Edited hello comment!");
		yamlFile.setComment("test.string", "Edited hello side comment!", CommentType.SIDE);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("test.string"),
		  new IsEqual<>("Edited hello comment!")
		);
		
		assertThat(
		  "Couldn't parse the comments correctly!",
		  yamlFile.getComment("test.string", CommentType.SIDE),
		  new IsEqual<>("Edited hello side comment!")
		);
	}
	
	@Test
	void setCommentFormat() throws IOException {
		final YamlFile yamlFile = new YamlFile();
		
		assertThat(
		  "Couldn't get the default comment formatter correctly!",
		  yamlFile.options().commentFormatter(),
		  new IsSame<>(YamlCommentFormat.DEFAULT.commentFormatter())
		);
		
		yamlFile.path("first").set(1).comment("1");
		yamlFile.path("second").set(2).comment("2").commentSide("side");
		
		String contents = "# 1\nfirst: 1\n# 2\nsecond: 2 # side\n";
		
		assertThat(
		  "Couldn't format the contents correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contents)
		);
		
		assertThat(
		  "Couldn't get the comment correctly!",
		  yamlFile.getComment("first"),
		  new IsEqual<>("1")
		);
		
		final YamlCommentFormatter customFormatter = new YamlCommentFormatter(new YamlCommentFormatterConfig("## "));
		yamlFile.setCommentFormat(customFormatter);
		
		assertThat(
		  "Couldn't get the comment formatter correctly!",
		  yamlFile.options().commentFormatter(),
		  new IsSame<>(customFormatter)
		);
		
		yamlFile.path("first").set(1).comment("1");
		yamlFile.path("second").set(2).comment("2").commentSide("side");
		
		contents = "## 1\nfirst: 1\n## 2\nsecond: 2 # side\n";
		
		assertThat(
		  "Couldn't format the contents correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contents)
		);
		
		assertThat(
		  "Couldn't get the comment correctly!",
		  yamlFile.getComment("first"),
		  new IsEqual<>("1")
		);
		
		yamlFile.setCommentFormat(YamlCommentFormat.RAW);
		
		yamlFile.path("first").set(1).comment("#1");
		yamlFile.path("second").set(2).comment("\n#2").commentSide("#side");
		
		contents = "#1\nfirst: 1\n\n#2\nsecond: 2 #side\n";
		
		assertThat(
		  "Couldn't format the contents correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contents)
		);
		
		assertThat(
		  "Couldn't get the comment correctly!",
		  yamlFile.getComment("second"),
		  new IsEqual<>("\n#2")
		);
		
		yamlFile.setCommentFormat(YamlCommentFormat.PRETTY);
		
		yamlFile.path("first").set(1).comment("1");
		yamlFile.path("second").set(2).comment("2").commentSide("side");
		
		contents = "# 1\nfirst: 1\n\n# 2\nsecond: 2 # side\n";
		
		assertThat(
		  "Couldn't format the contents correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contents)
		);
		
		assertThat(
		  "Couldn't get the comment correctly!",
		  yamlFile.getComment("second"),
		  new IsEqual<>("2")
		);
		
		yamlFile.setCommentFormat(YamlCommentFormat.BLANK_LINE);
		
		yamlFile.path("first").set(1).comment("1");
		yamlFile.path("second").set(2).comment("2").commentSide("side");
		
		contents = "\n# 1\nfirst: 1\n\n# 2\nsecond: 2\n# side\n";
		
		assertThat(
		  "Couldn't format the contents correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contents)
		);
		
		assertThat(
		  "Couldn't get the comment correctly!",
		  yamlFile.getComment("second"),
		  new IsEqual<>("\n2")
		);
		
		assertThat(
		  "Couldn't get the comment correctly!",
		  yamlFile.getComment("second", CommentType.SIDE),
		  new IsEqual<>("\nside")
		);
	}
	
	@Test
	void setGetComment() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		yamlFile.loadWithComments();
		
		final String content = TestResources.fileToStringUnix(yamlFile);
		
		yamlFile.setCommentFormat(YamlCommentFormat.RAW);
		
		yamlFile.setComment("test", yamlFile.getComment("test"));
		yamlFile.setComment("math", yamlFile.getComment("math"));
		yamlFile.setComment("test.comment", yamlFile.getComment("test.comment"));
		yamlFile.setComment("test.comment", yamlFile.getComment("test.comment", CommentType.SIDE), CommentType.SIDE);
		yamlFile.setComment("test.list.entry", yamlFile.getComment("test.list.entry"));
		yamlFile.setComment("test.list.entry", yamlFile.getComment("test.list.entry", CommentType.SIDE), CommentType.SIDE);
		
		assertThat(
		  "Couldn't set the comments correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void header() throws Exception {
		YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		yamlFile.load();
		
		assertThat(
		  "Couldn't build the header correctly!",
		  yamlFile.buildHeader(),
		  new IsEqual<>(TestResources.testHeader())
		);
		
		// header without stripping # prefix and without the last blank line
		final String headerWithPrefix = TestResources.testHeader().trim();
		
		assertThat(
		  "Couldn't get the options header correctly!",
		  yamlFile.options().header(),
		  new IsEqual<>(headerWithPrefix)
		);
		
		assertThat(
		  "Couldn't get the header correctly!",
		  yamlFile.getHeader(),
		  new IsEqual<>(headerWithPrefix)
		);
		
		assertThat(
		  "Couldn't get the first key comment correctly (loaded without comments)!",
		  yamlFile.getComment("test"),
		  new IsNull<>()
		);
		
		yamlFile.loadWithComments();
		
		assertThat(
		  "Couldn't build the header correctly!",
		  yamlFile.buildHeader(),
		  new IsEqual<>(TestResources.testHeader())
		);
		
		assertThat(
		  "Couldn't get the options header correctly!",
		  yamlFile.options().header(),
		  new IsEqual<>(headerWithPrefix)
		);
		
		assertThat(
		  "Couldn't get the header correctly!",
		  yamlFile.getHeader(),
		  new IsEqual<>(headerWithPrefix)
		);
		
		assertThat(
		  "Couldn't get the first key comment correctly!",
		  yamlFile.getComment("test"),
		  new IsEqual<>("Test comments")
		);
		
		final String headerSeparator = StringUtils.padding(20, '#');
		yamlFile.options().headerFormatter()
		  .prefixFirst(headerSeparator).commentPrefix("##\t").commentSuffix("\t##").suffixLast(headerSeparator);
		
		final String newHeader = "New header\nSecond line";
		yamlFile.setHeader(newHeader);
		
		assertThat(
		  "Couldn't get the options new header correctly!",
		  yamlFile.options().header(),
		  new IsEqual<>(newHeader)
		);
		
		assertThat(
		  "Couldn't get the new header correctly!",
		  yamlFile.getHeader(),
		  new IsEqual<>(headerSeparator + "\n##\tNew header\t##\n##\tSecond line\t##\n" + headerSeparator)
		);
		
		yamlFile.options().headerFormatter(new YamlHeaderFormatter().stripPrefix(true));
		
		assertThat(
		  "Couldn't get the options new header correctly!",
		  yamlFile.options().header(),
		  new IsEqual<>(newHeader)
		);
		
		assertThat(
		  "Couldn't get the new header correctly!",
		  yamlFile.getHeader(),
		  new IsEqual<>(newHeader)
		);
		
		yamlFile.loadWithComments();
		
		// header stripping # prefix and without the last blank line
		final String headerStrip = """
		  #####################
		  #  HEADER COMMENT  ##
		  #####################""";
		
		assertThat(
		  "Couldn't build the header correctly!",
		  yamlFile.buildHeader(),
		  new IsEqual<>(TestResources.testHeader())
		);
		
		assertThat(
		  "Couldn't get the stripped header correctly!",
		  yamlFile.getHeader(),
		  new IsEqual<>(headerStrip)
		);
		
		assertThat(
		  "Couldn't get the options header correctly!",
		  yamlFile.options().header(),
		  new IsEqual<>(headerWithPrefix)
		);
		
		assertThat(
		  "Couldn't get the first key comment correctly!",
		  yamlFile.getComment("test"),
		  new IsEqual<>("Test comments")
		);
	}
	
	@Test
	void footer() throws Exception {
		YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		yamlFile.loadWithComments();
		
		assertThat(
		  "Couldn't get the footer correctly!",
		  yamlFile.getFooter(),
		  new IsEqual<>("End")
		);
		
		yamlFile.setCommentFormat(YamlCommentFormat.RAW);
		
		assertThat(
		  "Couldn't get the footer correctly!",
		  yamlFile.getFooter(),
		  new IsEqual<>("\n# End")
		);
		
		yamlFile.setConfigurationFile(TestResources.getResourceURI("test-comments4.yml"));
		yamlFile.loadWithComments();
		
		assertThat(
		  "Couldn't get the footer correctly!",
		  yamlFile.getFooter(),
		  new IsEqual<>("""
			 
			 # Multiline
			 # footer with blank lines""")
		);
	}
	
	@Test
	void headerEmptyRootComment() throws Exception {
		final String contents = TestResources.testHeader() +
		  "root: This is a root key\n";
		
		YamlFile yamlFile = new YamlFile(TestResources.tempFile(contents));
		
		yamlFile.loadWithComments();
		
		assertThat(
		  "Couldn't write to temporal file!",
		  TestResources.fileToStringUnix(yamlFile),
		  new IsEqual<>(contents)
		);
		
		assertThat(
		  "Couldn't get the options header correctly!",
		  yamlFile.options().header(),
		  new IsEqual<>(TestResources.testHeader().trim())
		);
		
		assertThat(
		  "Couldn't get the header correctly!",
		  yamlFile.getHeader(),
		  new IsEqual<>(TestResources.testHeader().trim())
		);
		
		assertThat(
		  "Couldn't get the first key comment correctly!",
		  yamlFile.getComment("root", YamlCommentFormat.RAW),
		  new IsNull<>()
		);
		
		assertThat(
		  "Couldn't get the first key comment correctly!",
		  yamlFile.getComment("[0]", YamlCommentFormat.RAW),
		  new IsNull<>()
		);
		
		assertThat(
		  "Couldn't save the header correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contents)
		);
		
		yamlFile.setComment("root", ""); // empty comment (blank line)
		
		yamlFile.save();
		
		final String contentsEmptyComment = TestResources.testHeader() +
		  "\nroot: This is a root key\n";
		
		yamlFile.loadWithComments();
		
		assertThat(
		  "Couldn't get the options header correctly after save!",
		  yamlFile.options().header(),
		  new IsEqual<>(TestResources.testHeader().trim())
		);
		
		assertThat(
		  "Couldn't get the header correctly after save!",
		  yamlFile.getHeader(),
		  new IsEqual<>(TestResources.testHeader().trim())
		);
		
		assertThat(
		  "Couldn't get the first key comment correctly!",
		  yamlFile.getComment("root", YamlCommentFormat.RAW),
		  new IsEqual<>("")
		);
		
		assertThat(
		  "Couldn't get the first key comment correctly!",
		  yamlFile.getComment("[0]", YamlCommentFormat.RAW),
		  new IsEqual<>("")
		);
		
		assertThat(
		  "Couldn't save the header correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contentsEmptyComment)
		);
	}
	
	@Test
	void path() throws IOException {
		YamlFile yamlFile = new YamlFile();
		
		yamlFile.path("default").addDefault("default");
		yamlFile.path("test")
		  .comment("Test comment", YamlCommentFormat.BLANK_LINE).commentSide("Side comment")
		  .path("children")
		  .blankLine()
		  .path("child1")
		  .addDefault(1).comment("Child comment").blankLine()
		  .parent()
		  .addDefault("child2", 2)
		  .setChild("child3", 3);
		
		final String contents =
		  """
			 default: default
			 
			 # Test comment
			 test: # Side comment
			  \s
			   children:
			    \s
			     # Child comment
			     child1: 1
			     child2: 2
			     child3: 3
			 """;
		
		assertThat(
		  "Wrong current path!",
		  yamlFile.path("test.children").getCurrentPath(),
		  new IsEqual<>("test.children")
		);
		
		final YamlFileWrapper parent = yamlFile.path("test.children").parent();
		
		assertThat(
		  "Wrong parent path!",
		  parent,
		  new IsNot<>(new IsNull<>())
		);
		
		assertThat(
		  "Wrong parent path!",
		  parent.getCurrentPath(),
		  new IsEqual<>("test")
		);
		
		assertThat(
		  "Couldn't save the contents correctly!",
		  yamlFile.saveToString(),
		  new IsEqual<>(contents)
		);
	}
	
	@Test
	void exists() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.tempFile());
		
		assertThat(
		  "The file couldn't be found!",
		  yamlFile.exists(),
		  new IsTrue()
		);
		
		yamlFile.deleteFile();
		
		assertThat(
		  "The file still exists!",
		  yamlFile.exists(),
		  new IsNot<>(new IsTrue())
		);
	}
	
	@Test
	void createNewFile() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.tempFile());
		
		yamlFile.deleteFile();
		
		assertThat(
		  "The file already exists!",
		  yamlFile.exists(),
		  new IsNot<>(new IsTrue())
		);
		
		yamlFile.createNewFile(false);
		
		assertThat(
		  "The file couldn't be found!",
		  yamlFile.exists(),
		  new IsTrue()
		);
	}
	
	@Test
	void deleteFile() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.tempFile());
		yamlFile.createOrLoad();
		
		assertThat(
		  "The file does not exists!",
		  yamlFile.exists(),
		  new IsTrue()
		);
		
		yamlFile.deleteFile();
		
		assertThat(
		  "The file has not being deleted!",
		  yamlFile.exists(),
		  new IsNot<>(new IsTrue())
		);
	}
	
	@Test
	void getSize() throws URISyntaxException {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		final String content = TestResources.testComments();
		
		assertThat(
		  "The file size is not correct!",
		  yamlFile.getSize(),
		  new IsEqual<>(((long) content.getBytes().length))
		);
	}
	
	@Test
	void getFilePath() throws URISyntaxException {
		final File file = new File(TestResources.getResourceURI("test.yml"));
		final YamlFile yamlFile = new YamlFile(file);
		
		assertThat(
		  "Configuration file path is not the same!",
		  yamlFile.getFilePath(),
		  new IsEqual<>(file.getAbsolutePath()));
	}
	
	@Test
	void getConfigurationFile() throws URISyntaxException {
		final File file = new File(TestResources.getResourceURI("test.yml"));
		final YamlFile yamlFile = new YamlFile(file);
		
		assertThat(
		  "Configuration file is not the same!",
		  yamlFile.getConfigurationFile(),
		  new IsSame<>(file));
	}
	
	@Test
	void setConfigurationFile() throws URISyntaxException {
		final File file = new File(TestResources.getResourceURI("test.yml"));
		final YamlFile yamlFile = new YamlFile();
		
		yamlFile.setConfigurationFile(file);
		
		assertThat(
		  "Configuration file has not changed!",
		  yamlFile.getConfigurationFile(),
		  new IsSame<>(file));
	}
	
	@Test
	void copyTo() throws Exception {
		final YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-comments.yml"));
		final File copy = TestResources.tempFile();
		yamlFile.copyTo(copy);
		
		final YamlFile copied = new YamlFile(copy);
		copied.loadWithComments();
		final String content = TestResources.testComments();
		assertThat(
		  "Couldn't copy the file!",
		  copied.toString(),
		  new IsEqual<>(content)
		);
	}
	
	@Test
	void testMapListSerialization() throws Exception {
		ConfigSerialization.registerClass(Person.class);
		
		final YamlFile mapListYamlFile = new YamlFile(TestResources.getResourceURI("test-map-list.yml"));
		final String expected = TestResources.fileToStringUnix(mapListYamlFile);
		
		mapListYamlFile.loadWithComments();
		
		assertThat(
		  "Could not load/save map list elements!",
		  mapListYamlFile.saveToString(),
		  new IsEqual<>(expected));
		
		assertThat(
		  "Could not load map list comments!",
		  mapListYamlFile.getComment("people[-1].id"),
		  new IsEqual<>("Comment on list map element"));
		
		final Person p1 = new Person("12345678A", "John", 1990);
		final Person p2 = new Person("12345678B", "Maria", 1990);
		
		final List<Integer> testList = Arrays.asList(1, 2, 3, 2, 3);
		final List<Person> peopleList = Arrays.asList(p1, p2); // Serializable
		
		// Set -> Comment
		
		YamlFile yamlFile = new YamlFile();
		
		yamlFile.set("test", testList);
		yamlFile.set("people", peopleList);
		
		yamlFile.setComment("test[-1]", "last");
		yamlFile.setComment("test.2", "repeated");
		yamlFile.setComment("test[0]", "first");
		yamlFile.setComment("people[1].id", "Comment on list map element");
		
		final String output = yamlFile.saveToString();
		
		assertThat(
		  "Could not save map list elements!",
		  output,
		  new IsEqual<>(expected));
		
		yamlFile.loadFromString(output);
		
		assertThat(
		  "Could not parse list elements!",
		  yamlFile.getIntegerList("test"),
		  new IsEqual<>(testList));
		
		assertThat(
		  "Could not parse map list elements!",
		  yamlFile.getList("people"),
		  new IsEqual<>(peopleList));
		
		assertThat(
		  "Could not load/save map list elements!",
		  yamlFile.saveToString(),
		  new IsEqual<>(expected));
		
		// Comment -> Set
		
		yamlFile = new YamlFile();
		
		yamlFile.setComment("test[0]", "first");
		yamlFile.setComment("test.2", "repeated");
		yamlFile.setComment("test[-1]", "last");
		yamlFile.setComment("people[1].id", "Comment on list map element");
		
		yamlFile.set("test", testList);
		yamlFile.set("people", peopleList);
		
		assertThat(
		  "Could not save map list elements!",
		  yamlFile.saveToString(),
		  new IsEqual<>(expected));
		
		// Comment (different order) -> Set
		
		yamlFile = new YamlFile();
		
		yamlFile.setComment("people[1].id", "Comment on list map element");
		yamlFile.setComment("test[-1]", "last");
		yamlFile.setComment("test[0]", "first");
		yamlFile.setComment("test.2", "repeated");
		
		yamlFile.set("test", testList);
		yamlFile.set("people", peopleList);
		
		assertThat(
		  "Could not save map list elements!",
		  yamlFile.saveToString(),
		  new IsEqual<>(expected));
	}
	
	@Test
	void getListMap() throws Exception {
		ConfigSerialization.registerClass(Person.class);
		
		final Person p1 = new Person("12345678A", "John", 1990);
		final Person p2 = new Person("12345678B", "Maria", 1990);
		
		YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-map-list.yml"));
		yamlFile.loadWithComments();
		
		final KeyTree tree = yamlFile.getCommentMapper().getKeyTree();
		
		assertThat(
		  "List map node must be a list!",
		  tree.get("people").isList(),
		  new IsTrue()
		);
		
		assertThat(
		  "Could not get list map keys!",
		  tree.get("people[1].id"),
		  new IsNot<>(new IsNull<>())
		);
		
		assertThat(
		  "Could not get list map keys!",
		  tree.get("people[1].id").getName(),
		  new IsEqual<>("id")
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.getSerializable("people[0]", Person.class),
		  new IsEqual<>(p1)
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.getSerializable("people[1]", Person.class),
		  new IsEqual<>(p2)
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.getString("people[0].name"),
		  new IsEqual<>(p1.getName())
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.getString("people[-1].id"),
		  new IsEqual<>(p2.getId())
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.getInt("test[-3]"),
		  new IsEqual<>(3)
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.get("test[-6]"),
		  new IsNull<>()
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.get("test[5]"),
		  new IsNull<>()
		);
		
		yamlFile.set("test[-1]", 4);
		
		assertThat(
		  "Could not set list map values!",
		  yamlFile.getInt("test[5]"),
		  new IsEqual<>(4)
		);
		
		Map<String, Integer> innerMap = new LinkedHashMap<>();
		innerMap.put("one", 1);
		innerMap.put("two", 2);
		
		Map<String, Collection<Map<String, Integer>>> deepMap = new LinkedHashMap<>();
		
		List<Map<String, Integer>> listMap = new ArrayList<>(Collections.singletonList(innerMap));
		Set<Map<String, Integer>> setMap = new LinkedHashSet<>(Collections.singleton(innerMap));
		
		deepMap.put("list", listMap);
		deepMap.put("set", setMap);
		
		yamlFile.createSection("section").set("deep", deepMap);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.getMapList("section.deep.list"),
		  new IsEqual<>(listMap)
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.get("section.deep.list[0]"),
		  new IsEqual<>(listMap.getFirst())
		);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.getInt("section.deep.list[0].one"),
		  new IsEqual<>(listMap.getFirst().get("one"))
		);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.get("section.deep.set"),
		  new IsEqual<>(setMap)
		);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.get("section.deep.set[0]"),
		  new IsEqual<>(listMap.getFirst())
		);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.getInt("section.deep.list[0].one"),
		  new IsEqual<>(listMap.getFirst().get("one"))
		);
		
		yamlFile.set("section.deep.list[0].three", 3);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.getInt("section.deep.list[0].three"),
		  new IsEqual<>(3)
		);
		
		Map<String, String> innerMap2 = new LinkedHashMap<>();
		innerMap2.put("a", "a");
		innerMap2.put("b", "b");
		
		yamlFile.set("section.deep.list[-1]", innerMap2);
		yamlFile.set("section.deep.set[-1]", innerMap2);
		
		assertThat(
		  "Could not set list map values!",
		  yamlFile.get("section.deep.list[1]"),
		  new IsEqual<>(innerMap2)
		);
		
		assertThat(
		  "Could not set set map values!",
		  yamlFile.get("section.deep.set[-1]"),
		  new IsEqual<>(innerMap2)
		);
		
		yamlFile.set("section.deep.list[-1]", null);
		yamlFile.set("section.deep.set", null);
		
		assertThat(
		  "Could not get list map values!",
		  yamlFile.get("section.deep.list[1]"),
		  new IsNull<>()
		);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.get("section.deep.set"),
		  new IsNull<>()
		);
		
		yamlFile = new YamlFile();
		
		yamlFile.set("section.deep", deepMap);
		
		yamlFile.setComment("section.deep.list[0]", "[0]");
		yamlFile.setComment("section.deep.list[0].one", "[0].one");
		yamlFile.setComment("section.deep.list[0].one", "ONE", CommentType.SIDE);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.getComment("section.deep.list[0]"),
		  new IsEqual<>("[0]")
		);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.getComment("section.deep.list[0].one"),
		  new IsEqual<>("[0].one")
		);
		
		assertThat(
		  "Could not get set map values!",
		  yamlFile.getComment("section.deep.list[0].one", CommentType.SIDE),
		  new IsEqual<>("ONE")
		);
		
		final String output = """
		  section:
		    deep:
		      list:
		        # [0]
		          # [0].one
		        - one: 1 # ONE
		          two: 2
		          three: 3
		  """;
		
		assertThat(
		  "Could not save list map comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
	}
	
	@Test
	void indentListFormat() throws Exception {
		final List<String> list = Arrays.asList("entry 1", "entry 2");
		
		YamlFile yamlFile = new YamlFile();
		
		yamlFile.set("test.list", list);
		yamlFile.setComment("test.list.entry 1", "Comment on a list item");
		yamlFile.setComment("test.list.entry 2", ":)", CommentType.SIDE);
		
		String output = """
		  test:
		    list:
		      # Comment on a list item
		      - entry 1
		      - entry 2 # :)
		  """;
		
		assertThat(
		  "Could not save list comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
		
		yamlFile = new YamlFile();
		
		yamlFile.set("test.list", list);
		yamlFile.setComment("test.list.entry 2", "Comment on a list item");
		yamlFile.setComment("test.list.entry 2", ":)", CommentType.SIDE);
		
		output = """
		  test:
		    list:
		      - entry 1
		      # Comment on a list item
		      - entry 2 # :)
		  """;
		
		assertThat(
		  "Could not save list comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
		
		yamlFile = new YamlFile();
		
		yamlFile.options().indentList(0);
		
		yamlFile.set("test.list", list);
		yamlFile.setComment("test.list.entry 2", "Comment on a list item");
		yamlFile.setComment("test.list.entry 2", ":)", CommentType.SIDE);
		
		output = """
		  test:
		    list:
		    - entry 1
		    # Comment on a list item
		    - entry 2 # :)
		  """;
		
		assertThat(
		  "Could not save list comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
		
		yamlFile = new YamlFile();
		
		yamlFile.options().indentList(1);
		
		yamlFile.set("test.list", list);
		yamlFile.setComment("test.list.entry 2", "Comment on a list item");
		yamlFile.setComment("test.list.entry 2", ":)", CommentType.SIDE);
		
		output = """
		  test:
		    list:
		     - entry 1
		     # Comment on a list item
		     - entry 2 # :)
		  """;
		
		assertThat(
		  "Could not save list comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
		
		yamlFile = new YamlFile();
		
		yamlFile.set("test.list", Arrays.asList(list, list));
		yamlFile.setComment("test.list[0].entry 1", "Comment on a list item 1");
		yamlFile.setComment("test.list[0].entry 1", ":)", CommentType.SIDE);
		yamlFile.setComment("test.list[0].entry 2", "Comment on a list item 2");
		yamlFile.setComment("test.list[0].entry 2", ":)", CommentType.SIDE);
		
		output =
		  """
			 test:
			   list:
			     - &id001
			         # Comment on a list item 1
			         - entry 1 # :)
			         # Comment on a list item 2
			         - entry 2 # :)
			     - *id001
			 """;
		
		assertThat(
		  "Could not save list comments!",
		  yamlFile.saveToString(),
		  new IsEqual<>(output)
		);
	}
	
	@Test
	void serializables() throws Exception {
		YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test-serializables.yml"));
		
		yamlFile.load();
		
		Person output = new Person("12345678B", "Maria", 1990);
		
		assertThat(
		  "Person doesn't match!",
		  yamlFile.getSerializable("person", Person.class),
		  new IsEqual<>(output)
		);
	}
	
	@Test
	void getObject() throws Exception {
		YamlFile yamlFile = new YamlFile(TestResources.getResourceURI("test.yml"));
		
		yamlFile.load();
		
		ConfigSection section = yamlFile.getSection("test");
		
		assertThat(
		  "getObject() test on String didn't work",
		  section.getObject("string", String.class),
		  new IsEqual<>("Hello world")
		);
		
		assertThat(
		  "getObject() test on Integer didn't work",
		  section.getObject("number", Integer.class),
		  new IsEqual<>(5)
		);
		
		assertThat(
		  "getObject() shouldn't stringify other types",
		  section.getObject("number", String.class),
		  new IsNot<>(new IsEqual<>("5"))
		);
	}
}
