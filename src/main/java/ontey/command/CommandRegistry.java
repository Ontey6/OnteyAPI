package ontey.command;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import ontey.plugin.OnteyPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

/**
 * The CommandRegistry (adjacent to an {@link ontey.plugin.OnteyPlugin OnteyPlugin}) is a storage for commands specified by that plugin.
 */

public final class CommandRegistry {
   
   @Getter
   private final Set<Command> commands = new HashSet<>();
   
   /**
    * Creates a new (or the) registry for the plugin.
    */
   
   @ApiStatus.Internal
   public CommandRegistry(@NonNull OnteyPlugin plugin) {
      plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
          registerCommands(commands.registrar())
        );
   }
   
   /**
    * Registers all commands using the registrar.
    */
   
   private void registerCommands(@NonNull Commands registrar) {
      // only commands that have more than 1 name are here
      for(var cmd : commands)
         registrar.register(cmd.getRoot(), cmd.getDescription(), cmd.getAliases());
   }
   
   /**
    * Adds a command to this registry.
    * Assumes the command has a non-null root already
    */
   
   public void addCommand(@NonNull Command cmd) {
      commands.add(cmd);
   }
   
   /**
    * <b>Experimental</b>: Needs testing, might not work for brigadier.
    */
   
   @SneakyThrows // method should always exist
   @ApiStatus.Experimental
   public static void reload() {
      Bukkit.getServer().getClass().getMethod("syncCommands").invoke(Bukkit.getServer());
   }
}
