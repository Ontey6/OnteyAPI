package ontey.api.event.command;

import lombok.Getter;
import lombok.Setter;
import ontey.api.command.registry.RegistryCommand;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 *
 */

public class CommandRegistrationEvent extends Event implements Cancellable {
	
	@Getter
	private static final HandlerList HANDLER_LIST = new HandlerList();
	
	/**
	 * The command that will be registered
	 */
	
	@Getter
	private final RegistryCommand command;
	
	/**
	 * Whether the {@link RegistryCommand#registryRequirement()} passed.
	 * If this is false, it won't be registered
	 */
	
	private final boolean shouldRegister;
	
	@Getter
	@Setter
	private boolean cancelled;
	
	public CommandRegistrationEvent(RegistryCommand command, boolean shouldRegister) {
		this.command = command;
		this.shouldRegister = shouldRegister;
		this.cancelled = !shouldRegister;
	}
	
	public boolean shouldRegister() {
		return shouldRegister;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
