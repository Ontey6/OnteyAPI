package ontey.command;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A command configuration for {@link ConfigCommand ConfigCommands}.
 */

public record CommandConfiguration(
  @NonNull List<String> names,
  @NonNull String description,
  @Nullable String permission,
  @NonNull Map<String, ?> options
) {
   
   public @NonNull Map<String, Object> serialize() {
      Map<String, Object> out = new HashMap<>(4);
      
      out.put("names", names);
      out.put("permission", permission);
      out.put("description", description);
      out.put("options", options);
      
      return out;
   }
}
