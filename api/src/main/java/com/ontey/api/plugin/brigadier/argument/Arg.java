package com.ontey.api.plugin.brigadier.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.ontey.api.plugin.brigadier.command.Command.FAIL;
import static com.ontey.api.plugin.brigadier.command.Command.SUCCESS;

/**
 * A static factory class for easily making simple brigadier arguments
 */

@NullMarked
public final class Arg {
   
   /**
    * A fixed keyword also shown in tab
    */
   
   public static LiteralArgumentBuilder<CommandSourceStack> literal(String literal) {
      return Commands.literal(literal);
   }
   
   /**
    * requires the name to be non-null
    */
   
   private static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> argument) {
      return Commands.argument(Objects.requireNonNull(name, "name"), argument);
   }
   
   /**
    * A string without spaces
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, String> wordArg(String name) {
      return argument(name, StringArgumentType.word());
   }
   
   /**
    * A string that can have spaces if wrapped in quotation marks ("this is all one argument")
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, String> stringArg(String name) {
      return argument(name, StringArgumentType.string());
   }
   
   /**
    * A string that can have spaces without quotation marks. Has to be the last argument
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, String> varargs(String name) {
      return argument(name, StringArgumentType.greedyString());
   }
   
   /**
    * A boolean; true or false
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Boolean> booleanArg(String name) {
      return argument(name, BoolArgumentType.bool());
   }
   
   // Numbers
   
   /**
    * An integer
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name) {
      return argument(name, IntegerArgumentType.integer());
   }
   
   /**
    * An integer that is higher equals {@code min}
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name, int min) {
      return argument(name, IntegerArgumentType.integer(min));
   }
   
   /**
    * An integer that is higher equals {@code min} and lower equals {@code max}
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name, int min, int max) {
      return argument(name, IntegerArgumentType.integer(min, max));
   }
   
   /**
    * An integer
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name) {
      return argument(name, LongArgumentType.longArg());
   }
   
   /**
    * An integer that is higher equals {@code min}
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name, long min) {
      return argument(name, LongArgumentType.longArg(min));
   }
   
   /**
    * An integer that is higher equals {@code min} and lower equals {@code max}
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name, long min, long max) {
      return argument(name, LongArgumentType.longArg(min, max));
   }
   
   /**
    * A double
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name) {
      return argument(name, DoubleArgumentType.doubleArg());
   }
   
   /**
    * A double that is higher equals {@code min}
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name, double min) {
      return argument(name, DoubleArgumentType.doubleArg(min));
   }
   
   /**
    * A double that is higher equals {@code min} and lower equals {@code max}
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name, double min, double max) {
      return argument(name, DoubleArgumentType.doubleArg(min, max));
   }
   
   // More Custom args
   
   /**
    * An argument that suggests all online players
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playersArg(String name) {
      return argument(name, ArgumentTypes.players());
   }
   
   /**
    * An argument that suggests all online players
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playerArg(String name) {
      return argument(name, ArgumentTypes.player());
   }
   
   /**
    * A String argument that suggests all online players
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Player> playerNameArg(String name) {
      return argument(name, new OnlinePlayerNameArgument());
   }
   
   /**
    * A Gamemode, the full name ({@code creative}) or an abbreviation ({@code c})
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, GameMode> gamemodeArg(String name) {
      return argument(name, new GamemodeArgument());
   }
   
   /**
    * A precise location
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, FinePositionResolver> location(String name) {
      return argument(name, ArgumentTypes.finePosition());
   }
   
   /**
    * A precise location
    * @param center Whether whole numbers should be centered (+0.5)
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, FinePositionResolver> location(String name, boolean center) {
      return argument(name, ArgumentTypes.finePosition(center));
   }
   
   /**
    * A block location (Only integers)
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, BlockPositionResolver> blockLocation(String name) {
      return argument(name, ArgumentTypes.blockPosition());
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
   
   public static int runForPlayers(List<Player> players, CommandConsumer<Player> action) throws CommandSyntaxException {
      for(Player player : players)
         action.accept(player);
      
      return SUCCESS;
   }
   
   /**
    * Requires the executor to be a {@link Player} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes(Command) executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requirePlayer(CommandContext<CommandSourceStack> ctx, @Nullable String errorMessage, CommandConsumer<Player> action) throws CommandSyntaxException {
      return requireEntity(ctx, Player.class, errorMessage, action);
   }
   
   /**
    * Requires the executor to be a {@link Player} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with an error message "Not a player!".
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes(Command) executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requirePlayer(CommandContext<CommandSourceStack> ctx, CommandConsumer<Player> action) throws CommandSyntaxException {
      return requirePlayer(ctx, "Not a player!", action);
   }
   
   /**
    * Requires the executor to be a {@link CommandSender} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes(Command) executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requireSender(CommandContext<CommandSourceStack> ctx, @Nullable String errorMessage, CommandConsumer<CommandSender> action) throws CommandSyntaxException {
      return requireType(ctx, CommandSender.class, errorMessage, action);
   }
   
   /**
    * Requires the executor to be a {@link CommandSender} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes(Command) executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requireSender(CommandContext<CommandSourceStack> ctx, CommandConsumer<CommandSender> action) throws CommandSyntaxException {
      return requireSender(ctx, "Not a player/console!", action);
   }
   
   /**
    * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param type The type that the executor has to be
    * @return {@link com.ontey.api.plugin.brigadier.command.Command#SUCCESS SUCCESS}
    *         or {@link com.ontey.api.plugin.brigadier.command.Command#FAIL FAIL}
    *         depending on whether the executor is of the specified {@code type}
    */
   
   public static <T extends Entity> int requireEntity(CommandContext<CommandSourceStack> ctx, Class<T> type, @Nullable String errorMessage, CommandConsumer<T> action) throws CommandSyntaxException {
      return requireType(ctx, type, errorMessage, action);
   }
   
   /**
    * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param type The type that the executor has to be
    * @return {@link com.ontey.api.plugin.brigadier.command.Command#SUCCESS SUCCESS}
    *         or {@link com.ontey.api.plugin.brigadier.command.Command#FAIL FAIL}
    *         depending on whether the executor is of the specified {@code type}
    */
   
   public static <T extends Entity> int requireEntity(CommandContext<CommandSourceStack> ctx, Class<T> type, CommandConsumer<T> action) throws CommandSyntaxException {
      return requireEntity(ctx, type, "Not a " + type.getSimpleName() + "!", action);
   }
   
   /**
    * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param type The type that the executor has to be
    * @return {@link com.ontey.api.plugin.brigadier.command.Command#SUCCESS SUCCESS}
    *         or {@link com.ontey.api.plugin.brigadier.command.Command#FAIL FAIL}
    *         depending on whether the executor is of the specified {@code type}
    */
   
   public static <T> int requireType(CommandContext<CommandSourceStack> ctx, Class<T> type, @Nullable String errorMessage, CommandConsumer<T> action) throws CommandSyntaxException {
      Objects.requireNonNull(ctx, "ctx");
      Objects.requireNonNull(type, "type");
      Objects.requireNonNull(action, "action");
      
      Entity entity = ctx.getSource().getExecutor();
      CommandSender baseSender = ctx.getSource().getSender();
      
      if(entity != null && type.isAssignableFrom(entity.getClass())) {
         //noinspection unchecked
         action.accept((T) entity);
         return SUCCESS;
      }
      
      if(errorMessage != null)
         baseSender.sendMessage(errorMessage);
      
      return FAIL;
   }
   
   /**
    * A consumer that can throw a {@link CommandSyntaxException} to remove extra boilerplate in user code
    */
   
   public interface CommandConsumer<T> {
      void accept(T t) throws CommandSyntaxException;
   }
}
