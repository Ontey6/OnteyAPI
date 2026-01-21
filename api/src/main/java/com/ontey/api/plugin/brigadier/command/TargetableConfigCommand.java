package com.ontey.api.plugin.brigadier.command;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ontey.api.plugin.brigadier.argument.Arg;
import com.ontey.api.plugin.brigadier.config.CommandConfiguration;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A util to easily make commands like {@code /kill} (kills yourself in-game) targetable.
 * <p>
 * That just means, you could also run {@code /kill <player>} to kill another player.
 * <p>
 * It is easy to set up, usually just one line:
 *
 * <blockquote>For example,
 * <pre>
 * {@code
 *    new BrigadierTargetableConfigCommand(defaults, p -> p.setHealth(0)).register();
 *    // A command just like described above
 * }
 * </pre>
 * </blockquote>
 */

public class TargetableConfigCommand extends ConfigCommand {
   
   @SneakyThrows
   public TargetableConfigCommand(@NotNull ConfigurationSection config, @NotNull CommandConfiguration defaults, @NotNull Arg.CommandConsumer<Player> action) {
      super(config, defaults);
      
      super.setRoot(makeRoot(action));
   }
   
   private LiteralArgumentBuilder<CommandSourceStack> makeRoot(Arg.CommandConsumer<Player> action) {
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
