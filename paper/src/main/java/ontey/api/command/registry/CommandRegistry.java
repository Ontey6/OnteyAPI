package ontey.api.command.registry;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.NonNull;
import ontey.api.event.command.CommandRegistrationEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public final class CommandRegistry {
	
	private final Set<RegistryCommand> commands = new HashSet<>();
	
	public CommandRegistry(@NonNull LifecycleEventManager<?> lifecycleManager) {
		lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
			for(RegistryCommand command : this.commands) {
				boolean shouldRegister = command.shouldRegister();
				
				CommandRegistrationEvent event = new CommandRegistrationEvent(command, shouldRegister);
				event.callEvent();
				
				if(shouldRegister && !event.isCancelled())
					commands.registrar().register(command.root(), command.description(), command.aliases());
			}
		});
	}
	
	public CommandRegistry(@NonNull Plugin plugin) {
		this(plugin.getLifecycleManager());
	}
	
	public void register(@NonNull RegistryCommand command) {
		commands.add(command);
	}
}
