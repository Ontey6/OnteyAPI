package ontey.api.config.yaml.file;

import ontey.api.config.comment.CommentType;
import ontey.api.config.yaml.comment.format.YamlCommentFormat;
import ontey.api.config.yaml.comment.format.YamlCommentFormatterConfig;
import ontey.api.config.yaml.implementation.api.QuoteStyle;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsTrue;
import org.llorllale.cactoos.matchers.Throws;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

class YamlConfigOptionsTest {
	
	@Test
	void configuration() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		MatcherAssert.assertThat(
		  "The configuration is different!",
		  options.configuration(),
		  new IsEqual<>(configuration)
		);
	}
	
	@Test
	void copyDefaults() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		options.copyDefaults(true);
		MatcherAssert.assertThat(
		  "Couldn't set copy default!",
		  options.copyDefaults(),
		  new IsTrue()
		);
		options.copyDefaults(false);
		MatcherAssert.assertThat(
		  "Couldn't set copy default!",
		  options.copyDefaults(),
		  new IsNot<>(new IsTrue())
		);
	}
	
	@Test
	void pathSeparator() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		MatcherAssert.assertThat(
		  "Default path separator is not a dot!",
		  options.pathSeparator(),
		  new IsEqual<>('.')
		);
		
		options.pathSeparator('/');
		
		MatcherAssert.assertThat(
		  "Path separator has not changed!",
		  options.pathSeparator(),
		  new IsEqual<>('/')
		);
	}
	
	@Test
	void header() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		final String header = "Test header!";
		options.header(header);
		
		MatcherAssert.assertThat(
		  "Couldn't set header!",
		  options.header(),
		  new IsEqual<>(header)
		);
	}
	
	@Test
	void copyHeader() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		options.copyHeader(true);
		MatcherAssert.assertThat(
		  "Couldn't set copy header!",
		  options.copyHeader(),
		  new IsTrue()
		);
		options.copyHeader(false);
		MatcherAssert.assertThat(
		  "Couldn't set copy header!",
		  options.copyHeader(),
		  new IsNot<>(new IsTrue())
		);
	}
	
	@Test
	void indent() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		MatcherAssert.assertThat(
		  "Default indent is not 2!",
		  options.indent(),
		  new IsEqual<>(2)
		);
		
		options.indent(4);
		
		MatcherAssert.assertThat(
		  "Indent has not changed!",
		  options.indent(),
		  new IsEqual<>(4)
		);
	}
	
	@Test
	void indentList() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		MatcherAssert.assertThat(
		  "Default indentList is not 2!",
		  options.indentList(),
		  new IsEqual<>(2)
		);
		
		options.indentList(0);
		
		MatcherAssert.assertThat(
		  "List indent has not changed!",
		  options.indentList(),
		  new IsEqual<>(0)
		);
	}
	
	@Test
	void commentFormatter() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		final YamlCommentFormatterConfig blockFormatterConfiguration = options.commentFormatter().formatterConfiguration(CommentType.BLOCK);
		
		MatcherAssert.assertThat(
		  "Default comment prefix is not '# '!",
		  blockFormatterConfiguration.prefixFirst(),
		  new IsEqual<>("# ")
		);
		
		blockFormatterConfiguration.prefix("#");
		
		MatcherAssert.assertThat(
		  "Comment prefix has not changed!",
		  blockFormatterConfiguration.prefixFirst(),
		  new IsEqual<>("#")
		);
		
		blockFormatterConfiguration.prefix("\n# ");
		
		MatcherAssert.assertThat(
		  "Comment prefix has not changed!",
		  blockFormatterConfiguration.prefixFirst(),
		  new IsEqual<>("\n# ")
		);
		
		final YamlCommentFormatterConfig sideFormatterConfiguration = options.commentFormatter().formatterConfiguration(CommentType.SIDE);
		
		MatcherAssert.assertThat(
		  "Default side comment prefix is not ' # '!",
		  sideFormatterConfiguration.prefixFirst(),
		  new IsEqual<>(" # ")
		);
		
		MatcherAssert.assertThat(
		  "Side comment must start with space",
		  () -> sideFormatterConfiguration.prefix("#"),
		  new Throws<>(IllegalArgumentException.class)
		);
		
		MatcherAssert.assertThat(
		  "Side comment prefix is invalid!",
		  sideFormatterConfiguration.prefixFirst(),
		  new IsNot<>(new IsEqual<>("#"))
		);
		
		MatcherAssert.assertThat(
		  "Side below comments should not require to start with a space",
		  () -> sideFormatterConfiguration.prefix("\n# "),
		  new IsNot<>(new Throws<>(IllegalArgumentException.class))
		);
		
		MatcherAssert.assertThat(
		  "Side comment prefix has not changed!",
		  sideFormatterConfiguration.prefixFirst(),
		  new IsEqual<>("\n# ")
		);
		
		YamlCommentFormat.reset();
	}
	
	@Test
	void charset() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		MatcherAssert.assertThat(
		  "Default charset is not UTF-8!",
		  options.charset(),
		  new IsEqual<>(StandardCharsets.UTF_8)
		);
		
		options.charset(StandardCharsets.US_ASCII);
		
		MatcherAssert.assertThat(
		  "Charset has not changed!",
		  options.charset(),
		  new IsEqual<>(StandardCharsets.US_ASCII)
		);
	}
	
	@Test
	void isUnicode() {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = new YamlConfigOptions(configuration);
		
		MatcherAssert.assertThat(
		  "Default charset is not Unicode!",
		  options.isUnicode(),
		  new IsTrue()
		);
		
		options.charset(StandardCharsets.US_ASCII);
		
		MatcherAssert.assertThat(
		  "ASCII must not be Unicode!",
		  options.isUnicode(),
		  new IsNot<>(new IsTrue())
		);
	}
	
	@Test
	void quoteStyleDefaults() throws IOException {
		final YamlConfig configuration = new YamlConfig();
		final YamlConfigOptions options = configuration.options();
		
		// Default: PLAIN
		
		MatcherAssert.assertThat(
		  "Default quote style is not PLAIN!",
		  options.quoteStyleDefaults().getDefaultQuoteStyle(),
		  new IsEqual<>(QuoteStyle.PLAIN)
		);
		
		MatcherAssert.assertThat(
		  "Default quote style is not PLAIN!",
		  options.quoteStyleDefaults().getQuoteStyle(String.class),
		  new IsEqual<>(QuoteStyle.PLAIN)
		);
		
		configuration.set("test", "test");
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("test: test\n"));
		
		// Default: DOUBLE
		
		options.quoteStyleDefaults().setDefaultQuoteStyle(QuoteStyle.DOUBLE);
		
		MatcherAssert.assertThat(
		  "Default quote style has not changed!",
		  options.quoteStyleDefaults().getDefaultQuoteStyle(),
		  new IsEqual<>(QuoteStyle.DOUBLE)
		);
		
		MatcherAssert.assertThat(
		  "Default quote style has not changed!",
		  options.quoteStyleDefaults().getQuoteStyle(Boolean.class),
		  new IsEqual<>(QuoteStyle.DOUBLE)
		);
		
		MatcherAssert.assertThat(
		  "Default quote style has not changed!",
		  options.quoteStyleDefaults().getQuoteStyle(String.class),
		  new IsEqual<>(QuoteStyle.DOUBLE)
		);
		
		configuration.set("test", "test");
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": \"test\"\n"));
		
		// String: PLAIN
		
		options.quoteStyleDefaults().setQuoteStyle(String.class, QuoteStyle.PLAIN);
		
		MatcherAssert.assertThat(
		  "Quote style has not changed!",
		  options.quoteStyleDefaults().getQuoteStyle(String.class),
		  new IsEqual<>(QuoteStyle.PLAIN)
		);
		
		configuration.set("test", "test");
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": test\n"));
		
		MatcherAssert.assertThat(
		  "Default quote style has changed!",
		  options.quoteStyleDefaults().getQuoteStyle(Boolean.class),
		  new IsEqual<>(QuoteStyle.DOUBLE)
		);
		
		configuration.set("test", true);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": !!bool \"true\"\n"));
		
		// Boolean: SINGLE
		
		options.quoteStyleDefaults().setQuoteStyle(Boolean.class, QuoteStyle.SINGLE);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": !!bool \"true\"\n")); // Was set previously with QuoteStyle.DOUBLE
		
		MatcherAssert.assertThat(
		  "Quote style has not changed!",
		  options.quoteStyleDefaults().getQuoteStyle(Boolean.class),
		  new IsEqual<>(QuoteStyle.SINGLE)
		);
		
		configuration.set("test", true); // Now we set it again, so now it uses the Boolean default
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": !!bool 'true'\n"));
		
		configuration.set("test", "test");
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": test\n"));
		
		configuration.set("test", true, QuoteStyle.PLAIN);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": true\n"));
		
		configuration.set("test", true);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": !!bool 'true'\n"));
		
		options.quoteStyleDefaults().setQuoteStyle(Boolean.class, null);
		
		MatcherAssert.assertThat(
		  "Quote style is not default!",
		  options.quoteStyleDefaults().getQuoteStyle(Boolean.class),
		  new IsEqual<>(options.quoteStyleDefaults().getDefaultQuoteStyle())
		);
		
		configuration.set("test", true);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("\"test\": !!bool \"true\"\n"));
		
		// Default: PLAIN
		
		options.quoteStyleDefaults().setDefaultQuoteStyle(null);
		
		configuration.set("test", true);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("test: true\n"));
		
		// Integer: DOUBLE
		
		options.quoteStyleDefaults().setQuoteStyle(Integer.class, QuoteStyle.DOUBLE);
		
		configuration.set("test", 1);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("test: !!int \"1\"\n"));
		
		configuration.set("test", 1, QuoteStyle.SINGLE);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("test: !!int '1'\n"));
		
		final List<Integer> integerList = Arrays.asList(1, 2, 3);
		
		configuration.set("test", integerList);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("test:\n  - 1\n  - 2\n  - 3\n")); // List<Integer> is not instance of Integer
		
		configuration.set("test", integerList, QuoteStyle.DOUBLE);
		
		MatcherAssert.assertThat(
		  "Wrong value!",
		  configuration.saveToString(),
		  new IsEqual<>("test:\n  - !!int \"1\"\n  - !!int \"2\"\n  - !!int \"3\"\n"));
	}
}
