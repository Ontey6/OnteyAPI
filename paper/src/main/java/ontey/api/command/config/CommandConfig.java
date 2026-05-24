package ontey.api.command.config;

import lombok.Builder;
import lombok.NonNull;
import ontey.api.command.ConfigCommand;
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
	
	//TODO implement a different way to do this...
	@SuppressWarnings("unchecked")
	public static @NonNull CommandConfig deserialize(@NonNull Map<String, Object> input) {
		String name = "";
		List<String> aliases = List.of();
		String description = "";
		String permission = null;
		boolean consoleOnly = false;
		Map<String, ?> options = Map.of();
		boolean enabled = true;
		
		if(input.containsKey("name"))
			name = (String) input.get("name");
		
		if(input.containsKey("aliases"))
			aliases = (List<String>) input.get("aliases");
		
		if(input.containsKey("description"))
			description = (String) input.get("description");
		
		if(input.containsKey("permission"))
			permission = (String) input.get("permission");
		
		if(input.containsKey("console-only"))
			consoleOnly = (boolean) input.get("console-only");
		
		if(input.containsKey("options"))
			options = (Map<String, ?>) input.get("options");
		
		if(input.containsKey("enabled"))
			enabled = (boolean) input.get("enabled");
		
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
