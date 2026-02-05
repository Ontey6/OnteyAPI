package ontey.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.NonNull;
import ontey.command.argument.Arg;
import ontey.plugin.OnteyPlugin;

/**
 * A command that has no arguments.
 *
 * <pre>{@code
 * new SimpleCommand(plugin, "yeet", ctx -> Arg.requirePlayer(ctx, p -> p.setVelocity(new Vector(0, 100, 0))))
 * }</pre>
 *
 * This command can be executed in chat only like: {@code /yeet} and specifying any argument will make the command fail.
 */

public class SimpleCommand extends Command {
   
   public SimpleCommand(@NonNull OnteyPlugin plugin, @NonNull String name, com.mojang.brigadier.Command<CommandSourceStack> cmd) {
      super(plugin, name);
      
      setRoot(Arg.literal(name).executes(cmd));
   }
}
