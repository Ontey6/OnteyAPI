package ontey.api.command.config;

import lombok.Builder;
import lombok.NonNull;
import ontey.api.command.ConfigCommand;
import ontey.api.config.Config;
import ontey.api.config.ConfigSection;
import ontey.api.config.yaml.file.YamlConfig;
import ontey.api.serialization.ConfigSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

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
  @NonNull Map<@NonNull String, ? extends @UnknownNullability Object> options,
  boolean enabled
) implements ConfigSerializable {
	
	/**
	 * Creates a new {@link ConfigSection} for the {@code input} and deserializes it into a {@link CommandConfig} using {@link #deserialize(ConfigSection)}
	 *
	 * @return A new {@link CommandConfig} based on the values of the given {@link ConfigSection}.
	 * @throws IllegalStateException If the section does not contain a field named {@code name} which can be converted to a String
	 */
	
	public static @NonNull CommandConfig deserialize(@NonNull Map<@NonNull String, @Nullable Object> input) throws IllegalStateException {
		Config config = new YamlConfig();
		for(var entry : input.entrySet())
			config.set(entry.getKey(), entry.getValue());
		
		return deserialize(config);
	}
	
	/**
	 * Deserializes the given {@link ConfigSection} into a {@link CommandConfig}
	 *
	 * @return A new {@link CommandConfig} based on the values of the given {@link ConfigSection}.
	 * @throws IllegalStateException If the section does not contain a field named {@code name} which can be converted to a String
	 */
	
	public static @NonNull CommandConfig deserialize(ConfigSection section) throws IllegalStateException {
		String name = section.getString("name");
		
		if(name == null)
			throw new IllegalStateException("Section does not have a name field, can't convert to CommandConfig");
		
		List<String> aliases = section.getStringList("aliases");
		String description = section.getString("description");
		String permission = section.getString("permission");
		boolean consoleOnly = section.getBoolean("console-only");
		Map<String, ?> options = section.getSection("options").getMapValues(true);
		boolean enabled = section.getBoolean("enabled", true);
		
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
