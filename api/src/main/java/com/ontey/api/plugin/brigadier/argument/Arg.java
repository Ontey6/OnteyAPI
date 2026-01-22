package com.ontey.api.plugin.brigadier.argument;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ontey.api.plugin.brigadier.config.CommandOptions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.*;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import com.ontey.api.plugin.brigadier.command.Command;

import static com.ontey.api.plugin.brigadier.command.Command.FAIL;
import static com.ontey.api.plugin.brigadier.command.Command.SUCCESS;
import static java.util.Objects.requireNonNull;

/**
 * A static factory class for easily making paper brigadier arguments and reducing boilerplate code in command-tree creation.
 */

@NullMarked
public final class Arg {
   
   /**
    * A fixed keyword also shown in tab.
    * All arguments at this argument-position have to be literals.
    * If the argument at the argument-position isn't one of the specified literals, the argument appears red in chat.
    * @param literal The literal
    */
   
   public static LiteralArgumentBuilder<CommandSourceStack> literal(String literal) {
      return Commands.literal(requireNonNull(literal, "literal"));
   }
   
   /**
    * An argument with a custom {@link ArgumentType}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument(String, Class) get arguments}.
    */
   
   public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> argument) {
      return Commands.argument(requireNonNull(name, "name"), requireNonNull(argument, "argument"));
   }
   
   /**
    * A {@link String} without spaces.
    * Often used for identifiers, the replacement for spaces is usually an underscore ({@code _}).
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, String> wordArg(String name) {
      return argument(name, StringArgumentType.word());
   }
   
   /**
    * A {@link String} that can be plain: {@code simple} or quoted: {@code "some text"}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, String> stringArg(String name) {
      return argument(name, StringArgumentType.string());
   }
   
   /**
    * A {@link Component} that can be either a plain/quoted String: {@code simple}, {@code "some text"}.
    * <p>
    * Or a {@link Component} tree like {@code {text:"text",color:green}}, {@code [{text:"some ",color:green},{text:"text",color:red}]}
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Component> componentArg(String name) {
      return argument(name, ArgumentTypes.component());
   }
   
   /**
    * A string that can have spaces without being quoted: {@code some text}
    * Has to be the last argument.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, String> varargs(String name) {
      return argument(name, StringArgumentType.greedyString());
   }
   
   /**
    * A boolean; {@code true} or {@code false}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Boolean> booleanArg(String name) {
      return argument(name, BoolArgumentType.bool());
   }
   
   // Numbers
   
   /**
    * An integer.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name) {
      return argument(name, IntegerArgumentType.integer());
   }
   
   /**
    * An integer that is higher equals {@code min}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name, int min) {
      return argument(name, IntegerArgumentType.integer(min));
   }
   
   /**
    * An integer that is higher equals {@code min} and lower equals {@code max}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(String name, int min, int max) {
      return argument(name, IntegerArgumentType.integer(min, max));
   }
   
   /**
    * An integer.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name) {
      return argument(name, LongArgumentType.longArg());
   }
   
   /**
    * An integer that is higher equals {@code min}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name, long min) {
      return argument(name, LongArgumentType.longArg(min));
   }
   
   /**
    * An integer that is higher equals {@code min} and lower equals {@code max}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Long> longArg(String name, long min, long max) {
      return argument(name, LongArgumentType.longArg(min, max));
   }
   
   /**
    * A float.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Float> floatArg(String name) {
      return argument(name, FloatArgumentType.floatArg());
   }
   
   /**
    * A float that is higher equals {@code min}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Float> floatArg(String name, float min) {
      return argument(name, FloatArgumentType.floatArg(min));
   }
   
   /**
    * A float that is higher equals {@code min} and lower equals {@code max}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Float> floatArg(String name, float min, float max) {
      return argument(name, FloatArgumentType.floatArg(min, max));
   }
   
   /**
    * A double.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name) {
      return argument(name, DoubleArgumentType.doubleArg());
   }
   
   /**
    * A double that is higher equals {@code min}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name, double min) {
      return argument(name, DoubleArgumentType.doubleArg(min));
   }
   
   /**
    * A double that is higher equals {@code min} and lower equals {@code max}.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(String name, double min, double max) {
      return argument(name, DoubleArgumentType.doubleArg(min, max));
   }
   
   // More Custom args
   
   /**
    * An argument that suggests all online players.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playersArg(String name) {
      return argument(name, ArgumentTypes.players());
   }
   
   /**
    * An argument that suggests all online players.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playerArg(String name) {
      return argument(name, ArgumentTypes.player());
   }
   
   /**
    * An argument that suggests all online players and includes selectors for entities.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, EntitySelectorArgumentResolver> entitiesArg(String name) {
      return argument(name, ArgumentTypes.entities());
   }
   
   /**
    * An argument that suggests all online players and includes selectors for entities.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, EntitySelectorArgumentResolver> entityArg(String name) {
      return argument(name, ArgumentTypes.entity());
   }
   
   /**
    * A String argument that suggests all online players.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, Player> playerNameArg(String name) {
      return argument(name, new OnlinePlayerNameArgument());
   }
   
   /**
    * A Gamemode, the full name ({@code creative}) or an abbreviation ({@code c}).
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, GameMode> gamemodeArg(String name) {
      return argument(name, new GamemodeArgument());
   }
   
   /**
    * A precise location.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, FinePositionResolver> location(String name) {
      return argument(name, ArgumentTypes.finePosition());
   }
   
   /**
    * A precise location.
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    * @param center Whether whole numbers should be centered (+0.5)
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, FinePositionResolver> location(String name, boolean center) {
      return argument(name, ArgumentTypes.finePosition(center));
   }
   
   /**
    * A block location (Only integers).
    * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
    */
   
   public static RequiredArgumentBuilder<CommandSourceStack, BlockPositionResolver> blockLocation(String name) {
      return argument(name, ArgumentTypes.blockPosition());
   }
   
   // other argument-related helper methods
   
   /**
    * A shortcut for getting Players via an argument type like {@link ArgumentTypes#players()} that uses the {@link PlayerSelectorArgumentResolver}
    * @param name The name of the argument
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    */
   
   public static List<Player> getPlayers(String name, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
      return ctx.getArgument(name, PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());
   }
   
   /**
    * A shortcut for getting a Player via an argument type like {@link ArgumentTypes#player()} that uses the {@link PlayerSelectorArgumentResolver}
    * @param name The name of the argument
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    */
   
   public static Player getPlayer(String name, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
      return getPlayers(name, ctx).getFirst();
   }
   
   /**
    * A shortcut for getting Entities via an argument type like {@link ArgumentTypes#entities()} that uses the {@link EntitySelectorArgumentResolver}
    * @param name The name of the argument
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    */
   
   public static List<Entity> getEntities(String name, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
      return ctx.getArgument(name, EntitySelectorArgumentResolver.class).resolve(ctx.getSource());
   }
   
   /**
    * A shortcut for getting an Entity via an argument type like {@link ArgumentTypes#entity()} that uses the {@link EntitySelectorArgumentResolver}
    * @param name The name of the argument
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    */
   
   public static Entity getEntity(String name, CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
      return getEntities(name, ctx).getFirst();
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
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requirePlayer(CommandContext<CommandSourceStack> ctx, @Nullable String errorMessage, CommandConsumer<Player> action) throws CommandSyntaxException {
      return requireEntity(ctx, Player.class, errorMessage, action);
   }
   
   /**
    * Requires the executor to be a {@link Player} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with an error message "Not a player!".
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requirePlayer(CommandContext<CommandSourceStack> ctx, CommandConsumer<Player> action) throws CommandSyntaxException {
      return requirePlayer(ctx, "Not a player!", action);
   }
   
   /**
    * Requires the executor to be a {@link Player} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with an error message from the {@code errorMessageGetter} which defaults to "Not a player!".
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requirePlayer(CommandContext<CommandSourceStack> ctx, CommandOptions errorMessageGetter, CommandConsumer<Player> action) throws CommandSyntaxException {
      return requireType(ctx, Player.class, errorMessageGetter.getMessage("messages.incapable-executor", "Not a player!"), action);
   }
   
   /**
    * Requires the executor to be a {@link CommandSender} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requireSender(CommandContext<CommandSourceStack> ctx, @Nullable String errorMessage, CommandConsumer<CommandSender> action) throws CommandSyntaxException {
      return requireType(ctx, CommandSender.class, errorMessage, action);
   }
   
   /**
    * Requires the executor to be a {@link CommandSender} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requireSender(CommandContext<CommandSourceStack> ctx, CommandConsumer<CommandSender> action) throws CommandSyntaxException {
      return requireSender(ctx, "Not a player/console!", action);
   }
   
   /**
    * Requires the executor to be a {@link CommandSender} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with a message from the {@code errorMessageGetter} which defaults to "Not a player/console".
    * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
    * @param action The action executed if the executor is a player
    */
   
   public static int requireSender(CommandContext<CommandSourceStack> ctx, CommandOptions errorMessageGetter, CommandConsumer<CommandSender> action) throws CommandSyntaxException {
      return requireType(ctx, CommandSender.class, errorMessageGetter.getMessage("messages.incapable-executor", "Not a player/console"), action);
   }
   
   /**
    * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param type The type that the executor has to be
    * @return {@link Command#SUCCESS SUCCESS}
    *         or {@link Command#FAIL FAIL}
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
    * @return {@link Command#SUCCESS SUCCESS}
    *         or {@link Command#FAIL FAIL}
    *         depending on whether the executor is of the specified {@code type}
    */
   
   public static <T extends Entity> int requireEntity(CommandContext<CommandSourceStack> ctx, Class<T> type, CommandConsumer<T> action) throws CommandSyntaxException {
      return requireEntity(ctx, type, "Not a " + type.getSimpleName() + "!", action);
   }
   
   /**
    * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with an error-message queried from the {@code errorMessageGetter}.
    * @param type The type that the executor has to be
    * @return {@link Command#SUCCESS SUCCESS}
    *         or {@link Command#FAIL FAIL}
    *         depending on whether the executor is of the specified {@code type}
    */
   
   public static <T extends Entity> int requireEntity(CommandContext<CommandSourceStack> ctx, Class<T> type, CommandOptions errorMessageGetter, CommandConsumer<T> action) throws CommandSyntaxException {
      return requireType(ctx, type, errorMessageGetter, action);
   }
   
   /**
    * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with the {@code errorMessage}.
    * @param type The type that the executor has to be
    * @return {@link Command#SUCCESS SUCCESS}
    *         or {@link Command#FAIL FAIL}
    *         depending on whether the executor is of the specified {@code type}
    */
   
   public static <T> int requireType(CommandContext<CommandSourceStack> ctx, Class<T> type, @Nullable String errorMessage, CommandConsumer<T> action) throws CommandSyntaxException {
      requireNonNull(ctx, "ctx");
      requireNonNull(type, "type");
      requireNonNull(action, "action");
      
      Entity entity = ctx.getSource().getExecutor();
      CommandSender sender = ctx.getSource().getSender();
      
      if(entity == null) {
         if(!type.equals(CommandSender.class))
            return FAIL;
         
         //noinspection unchecked
         action.accept((T) Bukkit.getConsoleSender());
         return SUCCESS;
      }
      
      if(type.isAssignableFrom(entity.getClass())) {
         //noinspection unchecked
         action.accept((T) entity);
         return SUCCESS;
      }
      
      if(errorMessage != null)
         sender.sendMessage(errorMessage);
      
      return FAIL;
   }
   
   /**
    * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
    * <p>
    * Otherwise, informs the sender with an error-message queried from the {@code errorMessageGetter}.
    * @param type The type that the executor has to be
    * @return {@link Command#SUCCESS SUCCESS}
    *         or {@link Command#FAIL FAIL}
    *         depending on whether the executor is of the specified {@code type}
    */
   
   public static <T> int requireType(CommandContext<CommandSourceStack> ctx, Class<T> type, CommandOptions errorMessageGetter, CommandConsumer<T> action) throws CommandSyntaxException {
      return requireType(ctx, type, errorMessageGetter.getMessage("messages.incapable-executor", "That entity can't run this command!"), action);
   }
   
   /**
    * A consumer that can throw a {@link CommandSyntaxException} to remove extra boilerplate in user code (unnecessary try/catch statements)
    */
   
   @FunctionalInterface
   public interface CommandConsumer<T> {
      void accept(T t) throws CommandSyntaxException;
   }
}
