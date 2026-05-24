package ontey.api.plugin.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ontey.api.check.Checker;
import ontey.api.config.yaml.file.YamlFile;
import ontey.api.plugin.OnteyPlugin;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = @ApiStatus.Internal)
public class ConfigManager {
	
	private static final String IDENTIFIER_PATTERN = "[a-zA-Z0-9._-]+";
	
	private final File dataFolder;
	
	private final Map<String, YamlFile> configs = new HashMap<>();
	
	public ConfigManager(OnteyPlugin plugin) {
		this(plugin.getDataFolder());
	}
	
	@NonNull
	public YamlFile getConfig(@NonNull String identifier) {
		var config = configs.get(identifier);
		
		if(config == null)
			throw new IllegalArgumentException("No config with identifier '" + identifier + "'");
		
		return config;
	}
	
	public YamlFile registerConfig(@NonNull @IdentifierPattern String identifier, @NonNull String path) {
		Checker.checkArgument(identifier.matches(IDENTIFIER_PATTERN), "Invalid identifier '" + identifier + "'");
		
		var config = new YamlFile(new File(dataFolder, path));
		
		return registerConfig(identifier, config);
	}
	
	public YamlFile registerConfig(@NonNull @IdentifierPattern String identifier) {
		Checker.checkArgument(identifier.matches(IDENTIFIER_PATTERN), "Invalid identifier '" + identifier + "'");
		
		return registerConfig(identifier, identifier + ".yml");
	}
	
	public YamlFile registerConfig(@NonNull @IdentifierPattern String identifier, @NonNull YamlFile config) {
		Checker.checkArgument(identifier.matches(IDENTIFIER_PATTERN), "Invalid identifier '" + identifier + "'");
		
		configs.put(identifier, config);
		
		return config;
	}
	
	@SuppressWarnings("PatternValidation")
	public YamlFile registerConfig(@NonNull YamlFile config) {
		File file = config.getConfigurationFile();
		Checker.checkNonNull(file, "config doesn't have a configuration file set!");
		
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf('.'));
		
		return registerConfig(name, config);
	}
	
	@Pattern(IDENTIFIER_PATTERN)
	public @interface IdentifierPattern {
	
	}
}
