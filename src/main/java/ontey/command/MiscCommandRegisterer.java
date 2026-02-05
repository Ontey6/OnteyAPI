package ontey.command;

import lombok.NonNull;
import ontey.command.argument.LiteralArgumentBuilder;
import ontey.plugin.OnteyPlugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link CommandRegisterer} for subclasses of Command.
 * Has to create its own {@link Command} and return it so it can be registered.
 * For that, an {@link OnteyPlugin} is provided.
 * If this class is annotated with {@link CommandName @CommandName},
 * it will be registered automatically with the provided name.
 * <p>
 * <b>NOTE</b>: Also {@link ConfigCommand ConfigCommands} have to have the annotation as removing
 * that requirement would also require to hardcode a name for the "defaults".
 */

public interface MiscCommandRegisterer {
   
   /**
    * A registry method like {@link CommandRegisterer#register} that creates its own {@link Command}.
    *
    * @param plugin Provided for creation of the {@link Command}
    * @param root The root of the command. Is assigned to the command (return value) after the method
    * @return the created {@link Command}
    * @see CommandRegisterer#register
    */
   
   @NonNull
   @ApiStatus.OverrideOnly
   Command register(@NonNull OnteyPlugin plugin, @NonNull LiteralArgumentBuilder root);
}
