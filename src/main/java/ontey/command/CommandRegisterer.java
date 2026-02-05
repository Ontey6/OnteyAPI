package ontey.command;

import lombok.NonNull;
import ontey.command.argument.LiteralArgumentBuilder;
import ontey.plugin.OnteyPlugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * A registerer for a {@link Command}.
 * If it should be registered by default, it needs to have the annotation {@link CommandName @CommandName("name")}
 * Registration of the command is done in {@link OnteyPlugin#registerCommands()}, which is also called when you {@linkplain OnteyPlugin#load load OnteyAPI}.
 * Can be used without the annotation as lambda so
 */

@FunctionalInterface
public interface CommandRegisterer {
   
   /**
    * Your command registration.
    * Here you have your command and it's root.
    * The root is automatically applied to the command.
    * The command is also automatically registered.
    * @see OnteyPlugin#registerCommands registerCommands()
    */
   
   @ApiStatus.OverrideOnly
   void register(final @NonNull Command cmd, final @NonNull LiteralArgumentBuilder root);
}
