package ontey.api.config.yaml.implementation;

import ontey.api.config.ConfigSection;
import ontey.api.config.comment.CommentType;
import ontey.api.config.exception.InvalidConfigException;
import ontey.api.config.util.SectionUtils;
import ontey.api.config.util.SupplierIO;
import ontey.api.config.yaml.comment.YamlCommentDumper;
import ontey.api.config.yaml.comment.YamlCommentMapper;
import ontey.api.config.yaml.comment.YamlCommentParser;
import ontey.api.config.yaml.file.YamlConfig;
import ontey.api.config.yaml.file.YamlConfigOptions;
import ontey.api.config.yaml.file.YamlFile;
import ontey.api.config.yaml.implementation.snakeyaml.SnakeYamlConstructor;
import ontey.api.config.yaml.implementation.snakeyaml.SnakeYamlImplementation;
import ontey.api.config.yaml.implementation.snakeyaml.SnakeYamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Map;

/**
 * Default YAML implementation using snakeyaml high-level API and a custom comment parser / dumper.
 */

public class SimpleYamlImplementation extends SnakeYamlImplementation {
	
	public SimpleYamlImplementation() {
		super();
	}
	
	public SimpleYamlImplementation(LoaderOptions loaderOptions, DumperOptions dumperOptions) {
		super(loaderOptions, dumperOptions);
	}
	
	public SimpleYamlImplementation(SnakeYamlRepresenter yamlRepresenter) {
		super(yamlRepresenter);
	}
	
	public SimpleYamlImplementation(SnakeYamlConstructor yamlConstructor, SnakeYamlRepresenter yamlRepresenter) {
		super(yamlConstructor, yamlRepresenter);
	}
	
	public SimpleYamlImplementation(SnakeYamlConstructor yamlConstructor, SnakeYamlRepresenter yamlRepresenter, Resolver resolver) {
		super(yamlConstructor, yamlRepresenter, resolver);
	}
	
	@Override
	public void setComment(String path, String comment, CommentType type) {
		if(this.yamlCommentMapper == null) {
			this.options.useComments(true);
			this.yamlCommentMapper = new YamlCommentMapper(this.options);
		}
		this.yamlCommentMapper.setComment(path, comment, type);
	}
	
	@Override
	@SuppressWarnings("DuplicateThrows")
	public void load(SupplierIO.Reader readerSupplier, ConfigSection section) throws IOException, InvalidConfigException {
		if(readerSupplier != null) {
			this.load(readerSupplier.get(), section);
			
			if(this.options.useComments()) {
				this.parseComments(readerSupplier.get());
			}
		}
	}
	
	@Override
	@SuppressWarnings("DuplicateThrows")
	public void load(Reader reader, ConfigSection section) throws IOException, InvalidConfigException {
		this.configure(this.options);
		
		if(reader != null && section != null) {
			try(reader) {
				final Map<?, ?> values = this.getYaml().load(reader);
				
				if(values != null) {
					SectionUtils.addMapToSection(values, section);
				}
			} catch(YAMLException e) {
				throw new InvalidConfigException(e);
			} catch(ClassCastException e) {
				throw new InvalidConfigException("Top level is not a Map.");
			}
		}
	}
	
	@Override
	public void dump(Writer writer, ConfigSection section) throws IOException {
		this.configure(this.options);
		
		if(this.hasContent(writer, section)) {
			if(this.options.useComments()) {
				final YamlCommentDumper commentDumper = new YamlCommentDumper(
				  this.parseComments(),
				  dumper -> super.dumpYaml(dumper, section),
				  writer
				);
				commentDumper.dump();
			} else {
				super.dumpYaml(writer, section);
			}
		}
	}
	
	/**
	 * Parse comments from the current file configuration.
	 *
	 * @return a comment mapper with comments parsed
	 * @throws IOException if it hasn't been possible to parse the comments
	 */
	
	private YamlCommentMapper parseComments() throws IOException {
		if(this.yamlCommentMapper != null) {
			return this.yamlCommentMapper;
		}
		final YamlConfig config = this.options.configuration();
		Reader reader = null;
		if(config instanceof YamlFile) {
			final File configFile = ((YamlFile) config).getConfigurationFile();
			if(configFile != null) {
				reader = configFile.exists() ? Files.newBufferedReader(configFile.toPath(), this.options.charset()) : null;
			}
		}
		return this.parseComments(reader);
	}
	
	/**
	 * Parse comments from a reader.
	 *
	 * @param reader Reader of a Configuration to parse.
	 * @return a comment mapper with comments parsed
	 * @throws InvalidConfigException if it hasn't been possible to read the contents
	 */
	
	public YamlCommentMapper parseComments(Reader reader) throws InvalidConfigException {
		try {
			if(reader != null) {
				this.yamlCommentMapper = new YamlCommentParser(this.options, reader);
				((YamlCommentParser) this.yamlCommentMapper).parse();
			} else {
				this.yamlCommentMapper = new YamlCommentMapper(this.options);
			}
			return this.yamlCommentMapper;
		} catch(IOException e) {
			throw new InvalidConfigException(e);
		}
	}
	
	@Override
	public void configure(YamlConfigOptions options) {
		super.configure(options);
		
		// Use custom comment processor
		this.loaderOptions.setProcessComments(false);
		this.dumperOptions.setProcessComments(false);
	}
}
