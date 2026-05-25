package ontey.api.command.config;

import lombok.Builder;
import lombok.NonNull;
import ontey.api.command.ConfigCommand;
import ontey.api.config.Config;
import ontey.api.config.ConfigSection;
import ontey.api.config.yaml.file.YamlConfig;
import ontey.api.serialization.ConfigSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A command configuration for {@link ConfigCommand}s.
 */

@Builder
public record CommandConfig(
  @NonNull String name,
  @NonNull List<@NonNull String> aliases,
  @NonNull String description,
  @Nullable String permission,
  boolean consoleOnly,
  @NonNull Map<String, ?> options,
  boolean enabled
) implements ConfigSerializable {
	
	public static @NonNull CommandConfig deserialize(@NonNull Map<String, Object> input) {
		Config config = new YamlConfig();
		for(var entry : input.entrySet())
			config.set(entry.getKey(), entry.getValue());
		
		return deserialize(config);
	}
	
	public static @NonNull CommandConfig deserialize(ConfigSection section) {
		String name = section.getString("name");
		List<String> aliases = section.getStringList("aliases");
		String description = section.getString("description");
		String permission = section.getString("permission");
		boolean consoleOnly = section.getBoolean("console-only");
		Map<String, ?> options = section.getSection("options").getMapValues(true);
		boolean enabled = section.getBoolean("enabled");
		
		return new CommandConfig(name, aliases, description, permission, consoleOnly, options, enabled);
	}
	
	public @NonNull Map<@NonNull String, @Nullable Object> serialize() {
		Map<String, Object> out = new HashMap<>(7);
		
		out.put("name", name);
		out.put("aliases", aliases);
		out.put("permission", permission);
		out.put("description", description);
		out.put("console-only", consoleOnly);
		out.put("options", options);
		out.put("enabled", enabled);
		
		return out;
	}
}
