package ontey.command;

import lombok.NonNull;
import ontey.command.argument.Arg;
import ontey.command.argument.LiteralArgumentBuilder;
import ontey.plugin.OnteyPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A util to easily make commands like {@code /kill} (kills yourself in-game) targetable.
 * That just means, you could also run {@code /kill <player>} to kill another player.
 * It is easy to set up, usually just one line:
 *
 * <blockquote>For example,
 * <pre>
 * {@code
 *    new BrigadierTargetableConfigCommand(defaults, p -> p.setHealth(0)).register();
 * }
 * </pre>
 * </blockquote>
 *
 * The permission to target this command for other players can be specified
 * in the {@link CommandOptions options} under {@code target-permission}
 * and it defaults to {@code <permission>.target} where the placeholder
 * permission is replaced with the command's permission
 */

public class TargetableConfigCommand extends ConfigCommand {
   
   public TargetableConfigCommand(@NonNull OnteyPlugin plugin, @NonNull CommandConfiguration defaults, @NonNull Arg.CommandConsumer<Player> action) {
      super(plugin, defaults);
      
      //noinspection DataFlowIssue
      super.setRoot(makeRoot(action));
   }
   
   private @Nullable LiteralArgumentBuilder makeRoot(@NonNull Arg.CommandConsumer<Player> action) {
      Objects.requireNonNull(action, "action");
      
      if(values.names().isEmpty())
         return null;
      
      return
        Arg.literal(values.names().getFirst())
          .executes(ctx -> Arg.requirePlayer(ctx, options.getMessage("messages.incapable-executor", "The Executor has to be a player!"), action))
          
          .then(
            Arg.playersArg("player")
              .executes(ctx -> Arg.runForPlayers(Arg.getPlayers("player", ctx), action))
              .requires(src -> src.getSender().hasPermission(options.get("target-permission", values.permission() + ".target"))));
   }
}
