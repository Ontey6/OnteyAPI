package ontey.api.command.registry;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.NonNull;
import ontey.api.event.command.CommandRegistrationEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public record RegistryCommand(
  @NonNull String name,
  @NonNull List<@NonNull String> aliases,
  @Nullable String description,
  @NonNull LiteralCommandNode<CommandSourceStack> root,
  @NonNull Supplier<Boolean> registryRequirement
) {
	
	public RegistryCommand(@NonNull String name, @NonNull List<@NonNull String> aliases, @Nullable String description, @NonNull LiteralCommandNode<CommandSourceStack> root) {
		this(name, aliases, description, root, () -> true);
	}
	
	/**
	 * Internal - If you use the {@link CommandRegistrationEvent}, use {@link CommandRegistrationEvent#shouldRegister()} instead.
	 *
	 * @return
	 */
	
	@ApiStatus.Internal
	public boolean shouldRegister() {
		return registryRequirement.get();
	}
}
