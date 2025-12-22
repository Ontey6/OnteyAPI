package com.ontey.api.brigadier.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static com.ontey.api.brigadier.command.Command.FAIL;
import static com.ontey.api.brigadier.command.Command.SUCCESS;

/**
 * A static factory class for easily making simple brigadier arguments
 */

public final class Arg {
   
   /**
    * A fixed keyword also shown in tab
    */
   
   public static @NotNull LiteralArgumentBuilder<CommandSourceStack> literal(@NotNull String literal) {
      return Commands.literal(literal);
   }
   
   /**
    * A string without spaces
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, String> wordArg(String name) {
      return Commands.argument(name, StringArgumentType.word());
   }
   
   /**
    * A string that can have spaces if wrapped in quotation marks ("this is all one argument")
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, String> stringArg(String name) {
      return Commands.argument(name, StringArgumentType.string());
   }
   
   /**
    * A string that can have spaces without quotation marks. Has to be the last argument
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, String> varargs(String name) {
      return Commands.argument(name, StringArgumentType.greedyString());
   }
   
   /**
    * A boolean; true or false
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Boolean> booleanArg(String name) {
      return Commands.argument(name, BoolArgumentType.bool());
   }
   
   // Numbers
   
   /**
    * An integer
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name) {
      return Commands.argument(name, IntegerArgumentType.integer());
   }
   
   /**
    * An integer that is higher equals {@code min}
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name, int min) {
      return Commands.argument(name, IntegerArgumentType.integer(min));
   }
   
   /**
    * An integer that is higher equals {@code min} and lower equals {@code max}
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name, int min, int max) {
      return Commands.argument(name, IntegerArgumentType.integer(min, max));
   }
   
   /**
    * An integer
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name) {
      return Commands.argument(name, LongArgumentType.longArg());
   }
   
   /**
    * An integer that is higher equals {@code min}
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name, long min) {
      return Commands.argument(name, LongArgumentType.longArg(min));
   }
   
   /**
    * An integer that is higher equals {@code min} and lower equals {@code max}
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name, long min, long max) {
      return Commands.argument(name, LongArgumentType.longArg(min, max));
   }
   
   /**
    * A double
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name) {
      return Commands.argument(name, DoubleArgumentType.doubleArg());
   }
   
   /**
    * A double that is higher equals {@code min}
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name, double min) {
      return Commands.argument(name, DoubleArgumentType.doubleArg(min));
   }
   
   /**
    * A double that is higher equals {@code min} and lower equals {@code max}
    */
   
   public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name, double min, double max) {
      return Commands.argument(name, DoubleArgumentType.doubleArg(min, max));
   }
   
   // More Custom args
   
   /**
    * An argument that suggests all online players
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playersArg(String name) {
      return Commands.argument(name, ArgumentTypes.players());
   }
   
   /**
    * An argument that suggests all online players
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playerArg(String name) {
      return Commands.argument(name, ArgumentTypes.player());
   }
   
   /**
    * A String argument that suggests all online players
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Player> playerNameArg(String name) {
      return Commands.argument(name, new OnlinePlayerNameArgument());
   }
   
   /**
    * A Gamemode, the full name ({@code creative}) or an abbreviation ({@code c})
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, GameMode> gamemodeArg(String name) {
      return Commands.argument(name, new GamemodeArgument());
   }
   
   // other argument-related helper methods
   
   /**
    * A shortcut for getting Players via an argument type like {@link ArgumentTypes#players} that uses the {@link PlayerSelectorArgumentResolver}
    * @param name The name of the argument
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes(Command) executes})
    */
   
   public static List<Player> getPlayers(String name, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
      return ctx.getArgument(name, PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());
   }
   
   /**
    * A shortcut for getting a Player via an argument type like {@link ArgumentTypes#player} that uses the {@link PlayerSelectorArgumentResolver}
    * @param name The name of the argument
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes(Command) executes})
    */
   
   public static Player getPlayer(String name, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
      return getPlayers(name, ctx).getFirst();
   }
   
   /**
    * Executes a given action for all players.
    * Useful in combination with {@link Arg#getPlayers(String, CommandContext) getPlayers}.
    * @param players The players to run the action for
    * @param action the action ran for all players
    */
   
   public static int runForPlayers(List<Player> players, Consumer<Player> action) {
      for(Player player : players)
         action.accept(player);
      
      return SUCCESS;
   }
   
   /**
    * Requires the executor to be a player to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with a message from the {@code options}.
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes(Command) executes})
    * @param errorMessage The error message sent if the executor is not a player
    * @param action The action executed if the executor is a player
    */
   
   public static int requirePlayer(CommandContext<CommandSourceStack> ctx, String errorMessage, Consumer<Player> action) {
      Entity entity = ctx.getSource().getExecutor();
      CommandSender sender = ctx.getSource().getSender();
      
      if(!(entity instanceof final Player player)) {
         sender.sendMessage(errorMessage);
         return FAIL;
      }
      
      action.accept(player);
      return SUCCESS;
   }
}
