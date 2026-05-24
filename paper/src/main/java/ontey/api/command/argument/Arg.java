package ontey.api.command.argument;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import ontey.api.command.Command;
import ontey.api.command.config.CommandOptions;
import ontey.api.text.TextDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static ontey.api.command.Command.FAIL;
import static ontey.api.command.Command.SUCCESS;

/**
 * A static factory class for easily making paper brigadier arguments and reducing boilerplate code in command-tree creation.
 */

// 1.2: Annotate argument() with @lombok.NonNull to add checks and
// the all other argument-creating methods with @NotNull for performance
// and decompile code readability as they already use argument() which
// already checks nullity. @lombok.NonNull produces actual code checks every time.

public final class Arg {
	
	/**
	 * A fixed keyword also shown in tab.
	 * All arguments at this argument-position have to be literals.
	 * If the argument at the argument-position isn't one of the specified literals, the argument appears red in chat.
	 *
	 * @param literal The literal
	 */
	
	public static @NonNull LiteralArgumentBuilder<CommandSourceStack> literal(@NonNull String literal) {
		return LiteralArgumentBuilder.literal(literal);
	}
	
	/**
	 * An argument with a custom {@link ArgumentType}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static <T> @NonNull RequiredArgumentBuilder<CommandSourceStack, T> of(@NonNull String name, @NonNull ArgumentType<T> argument) {
		return RequiredArgumentBuilder.argument(name, argument);
	}
	
	/**
	 * A {@link String} without spaces.
	 * Often used for identifiers, the replacement for spaces is usually an underscore ({@code _}).
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, String> wordArg(@NotNull String name) {
		return of(name, StringArgumentType.word());
	}
	
	/**
	 * A {@link String} that can be plain: {@code simple} or quoted: {@code "some text"}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, String> stringArg(@NotNull String name) {
		return of(name, StringArgumentType.string());
	}
	
	/**
	 * A {@link Component} that can be either a plain/quoted String: {@code simple}, {@code "some text"}.
	 * <br>
	 * Or a {@link Component} tree like {@code {text:"text",color:green}}, {@code [{text:"some ",color:green},{text:"text",color:red}]}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Component> componentArg(@NotNull String name) {
		return of(name, ArgumentTypes.component());
	}
	
	/**
	 * A string that can have spaces without being quoted: {@code some text}
	 * Has to be the last argument.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, String> varargs(@NotNull String name) {
		return of(name, StringArgumentType.greedyString());
	}
	
	/**
	 * A boolean; {@code true} or {@code false}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Boolean> booleanArg(@NotNull String name) {
		return of(name, BoolArgumentType.bool());
	}
	
	// Numbers
	
	/**
	 * An integer.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(@NotNull String name) {
		return of(name, IntegerArgumentType.integer());
	}
	
	/**
	 * An integer that is higher equals {@code min}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(@NotNull String name, int min) {
		return of(name, IntegerArgumentType.integer(min));
	}
	
	/**
	 * An integer that is higher equals {@code min} and lower equals {@code max}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Integer> intArg(@NotNull String name, int min, int max) {
		return of(name, IntegerArgumentType.integer(min, max));
	}
	
	/**
	 * An integer.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Long> longArg(@NotNull String name) {
		return of(name, LongArgumentType.longArg());
	}
	
	/**
	 * An integer that is higher equals {@code min}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Long> longArg(@NotNull String name, long min) {
		return of(name, LongArgumentType.longArg(min));
	}
	
	/**
	 * An integer that is higher equals {@code min} and lower equals {@code max}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Long> longArg(@NotNull String name, long min, long max) {
		return of(name, LongArgumentType.longArg(min, max));
	}
	
	/**
	 * A float.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Float> floatArg(@NotNull String name) {
		return of(name, FloatArgumentType.floatArg());
	}
	
	/**
	 * A float that is higher equals {@code min}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Float> floatArg(@NotNull String name, float min) {
		return of(name, FloatArgumentType.floatArg(min));
	}
	
	/**
	 * A float that is higher equals {@code min} and lower equals {@code max}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Float> floatArg(@NotNull String name, float min, float max) {
		return of(name, FloatArgumentType.floatArg(min, max));
	}
	
	/**
	 * A double.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(@NotNull String name) {
		return of(name, DoubleArgumentType.doubleArg());
	}
	
	/**
	 * A double that is higher equals {@code min}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(@NotNull String name, double min) {
		return of(name, DoubleArgumentType.doubleArg(min));
	}
	
	/**
	 * A double that is higher equals {@code min} and lower equals {@code max}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Double> doubleArg(@NotNull String name, double min, double max) {
		return of(name, DoubleArgumentType.doubleArg(min, max));
	}
	
	// More Custom args
	
	/**
	 * An argument that suggests all online players.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playersArg(@NotNull String name) {
		return of(name, ArgumentTypes.players());
	}
	
	/**
	 * An argument that suggests all online players.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, PlayerSelectorArgumentResolver> playerArg(@NotNull String name) {
		return of(name, ArgumentTypes.player());
	}
	
	/**
	 * An argument that suggests all online players and includes selectors for entities.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, EntitySelectorArgumentResolver> entitiesArg(@NotNull String name) {
		return of(name, ArgumentTypes.entities());
	}
	
	/**
	 * An argument that suggests all online players and includes selectors for entities.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, EntitySelectorArgumentResolver> entityArg(@NotNull String name) {
		return of(name, ArgumentTypes.entity());
	}
	
	/**
	 * A Gamemode like {@code creative}, {@code survival}, {@code adventure}.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, GameMode> gamemodeArg(@NotNull String name) {
		return of(name, ArgumentTypes.gameMode());
	}
	
	/**
	 * A precise location.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, FinePositionResolver> locationArg(@NotNull String name) {
		return of(name, ArgumentTypes.finePosition());
	}
	
	/**
	 * A precise location.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 * @param center Whether whole numbers should be centered (+0.5)
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, FinePositionResolver> locationArg(@NotNull String name, boolean center) {
		return of(name, ArgumentTypes.finePosition(center));
	}
	
	/**
	 * A block location (Only integers).
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, BlockPositionResolver> blockLocationArg(@NotNull String name) {
		return of(name, ArgumentTypes.blockPosition());
	}
	
	/**
	 * A list of player profiles. Can find online and offline players.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, PlayerProfileListResolver> playerProfilesArg(@NotNull String name) {
		return of(name, ArgumentTypes.playerProfiles());
	}
	
	/**
	 * An item like
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, ItemStack> itemArg(@NotNull String name) {
		return of(name, ArgumentTypes.itemStack());
	}
	
	/**
	 * A block state like {@code minecraft:anvil[facing=east]}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, BlockState> blockStateArg(@NotNull String name) {
		return of(name, ArgumentTypes.blockState());
	}
	
	/**
	 * An {@link ItemStackPredicate}. A {@link java.util.function.Predicate Predicate} of {@link ItemStack}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, ItemStackPredicate> itemPredicateArg(@NotNull String name) {
		return of(name, ArgumentTypes.itemPredicate());
	}
	
	/**
	 * A named color like {@code green} or {@code red}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, NamedTextColor> namedColorArg(@NotNull String name) {
		return of(name, ArgumentTypes.namedColor());
	}
	
	/**
	 * A style like {@code {"italic":true}} or {@code {"color":"red"}}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Style> styleArg(@NotNull String name) {
		return of(name, ArgumentTypes.style());
	}
	
	/**
	 * A scoreboard {@link DisplaySlot} like {@code sidebar}, {@code list}, {@code below_name}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, DisplaySlot> scoreboardSlotArg(@NotNull String name) {
		return of(name, ArgumentTypes.scoreboardDisplaySlot());
	}
	
	/**
	 * A {@link World} like {@code minecraft:overworld}, {@code myplugin:custom_world}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, World> worldArg(@NotNull String name) {
		return of(name, ArgumentTypes.world());
	}
	
	/**
	 * A registry for literally anything. Like attribute: {@code attack_damage}, biome: {@code badlands}, block: {@code grass_block}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull <T> RequiredArgumentBuilder<CommandSourceStack, T> resourceArg(@NotNull String name, RegistryKey<T> key) {
		return of(name, ArgumentTypes.resource(key));
	}
	
	/**
	 * A registry for literally anything. Like attribute: {@code attack_damage}, biome: {@code badlands}, block: {@code grass_block}.
	 * Similar to {@link #resourceArg}, but just returns the a {@link TypedKey} instead of the actual value.
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull <T> RequiredArgumentBuilder<CommandSourceStack, TypedKey<T>> resourceKeyArg(@NotNull String name, RegistryKey<T> key) {
		return of(name, ArgumentTypes.resourceKey(key));
	}
	
	/**
	 * A duration in ticks like {@code 1t}, {@code 2s}, {@code 3d}
	 *
	 * @param name The name of the argument. Shown if there is no tab completion and used to {@linkplain CommandContext#getArgument get arguments}.
	 */
	
	public static @NotNull RequiredArgumentBuilder<CommandSourceStack, Integer> timeArg(@NotNull String name) {
		return of(name, ArgumentTypes.time());
	}
	
	// other argument-related helper methods
	
	/**
	 * Clones only the root, the children are just copied over.
	 *
	 * <blockquote>
	 * Showcase of behavior
	 * <pre>{@code
	 *    var root = ...;
	 *    var clone = Arg.cloneRoot(root);
	 *
	 *    assert root != clone;
	 *    assert root.getArguments().equals(clone.getArguments())
	 *    assert root.getArguments() != clone.getArguments();
	 *    }</pre>
	 * </blockquote>
	 *
	 * @param root The root with children to clone
	 */
	
	public static LiteralArgumentBuilder<CommandSourceStack> cloneRoot(LiteralArgumentBuilder<CommandSourceStack> root) {
		var result = Arg.literal(root.getLiteral());
		
		for(var argument : root.getArguments())
			result.then(argument);
		
		return result;
	}
	
	/**
	 * A shortcut for getting Players via an argument type like {@link ArgumentTypes#players()}
	 * <br>
	 * Experimental. Maybe make own ArgumentBuilder implementation instead.
	 *
	 * @param name The name of the argument
	 */
	
	@ApiStatus.Experimental
	public static com.mojang.brigadier.Command<CommandSourceStack> getPlayers(@NonNull String name, ExtendedContextBiFunction<List<Player>> action) {
		return ctx -> {
			var players = ctx.getArgument(name, PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());
			return action.apply(ctx, players);
		};
	}
	
	/**
	 * A shortcut for getting Players via an argument type like {@link ArgumentTypes#players()}
	 *
	 * @param name The name of the argument
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 */
	
	public static List<Player> getPlayers(@NonNull String name, @NonNull CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		return ctx.getArgument(name, PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());
	}
	
	/**
	 * A shortcut for getting a Player via an argument type like {@link ArgumentTypes#player()}
	 *
	 * @param name The name of the argument
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 */
	
	public static Player getPlayer(@NotNull String name, @NotNull CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var out = getPlayers(name, ctx);
		if(out.size() != 1)
			throw simpleRedException("Required 1 player for argument " + name + ", got " + out.size());
		
		return out.getFirst();
	}
	
	/**
	 * A shortcut for getting Entities via an argument type like {@link ArgumentTypes#entities()} that uses the {@link EntitySelectorArgumentResolver}
	 *
	 * @param name The name of the argument
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 */
	
	public static List<Entity> getEntities(@NonNull String name, @NonNull CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		return ctx.getArgument(name, EntitySelectorArgumentResolver.class).resolve(ctx.getSource());
	}
	
	/**
	 * A shortcut for getting an Entity via an argument type like {@link ArgumentTypes#entity()} that uses the {@link EntitySelectorArgumentResolver}
	 *
	 * @param name The name of the argument
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 */
	
	public static Entity getEntity(@NotNull String name, @NotNull CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var out = getEntities(name, ctx);
		if(out.size() != 1)
			throw simpleRedException("Required 1 entity for argument " + name + ", got " + out.size());
		
		return out.getFirst();
	}
	
	/**
	 * A shortcut for getting {@link PlayerProfile player profiles} via an argument type like {@link ArgumentTypes#entities()} that uses the {@link PlayerProfileListResolver}
	 *
	 * @param name The name of the argument
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 */
	
	public static List<PlayerProfile> getProfiles(@NonNull String name, @NonNull CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		return ctx.getArgument(name, PlayerProfileListResolver.class).resolve(ctx.getSource()).stream().toList();
	}
	
	/**
	 * A shortcut for getting a {@link PlayerProfile} via an argument type like {@link ArgumentTypes#entity()} that uses the {@link PlayerProfileListResolver}
	 *
	 * @param name The name of the argument
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 */
	
	public static PlayerProfile getProfile(@NotNull String name, @NotNull CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var out = getProfiles(name, ctx);
		if(out.size() != 1)
			throw simpleRedException("Required 1 player profile for argument " + name + ", got " + out.size());
		
		return out.getFirst();
	}
	
	/**
	 * Executes a given action for all players.
	 * Useful in combination with {@link #getPlayers getPlayers}.
	 *
	 * @param players The players to run the action for
	 * @param action the action ran for all players
	 */
	
	public static int runForPlayers(@NonNull List<Player> players, @NonNull CommandConsumer<Player> action) throws CommandSyntaxException {
		for(Player player : players)
			action.accept(player);
		
		return SUCCESS;
	}
	
	/**
	 * Requires the executor to be a {@link Player} to run the {@code action}.
	 * <br>
	 * Otherwise, informs the sender with the {@code errorMessage}.
	 *
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 * @param action The action executed if the executor is a player
	 */
	
	public static int requirePlayer(@NotNull CommandContext<CommandSourceStack> ctx, @Nullable String errorMessage, @NotNull CommandConsumer<Player> action) throws CommandSyntaxException {
		return requireType(ctx, Player.class, errorMessage, action);
	}
	
	/**
	 * Requires the executor to be a {@link Player} to run the {@code action}.
	 * <br>
	 * Otherwise, informs the sender with an error message "Not a player!".
	 *
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 * @param action The action executed if the executor is a player
	 */
	
	public static int requirePlayer(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull CommandConsumer<Player> action) throws CommandSyntaxException {
		return requirePlayer(ctx, "Not a player!", action);
	}
	
	/**
	 * Requires the executor to be a {@link Player} to run the {@code action}.
	 * <br>
	 * Otherwise, informs the sender with an error message from the {@code errorMessageGetter} which defaults to "Not a player!".
	 *
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 * @param action The action executed if the executor is a player
	 */
	
	public static int requirePlayer(@NotNull CommandContext<CommandSourceStack> ctx, @NonNull CommandOptions errorMessageGetter, @NotNull CommandConsumer<Player> action) throws CommandSyntaxException {
		return requireType(ctx, Player.class, errorMessageGetter.get("messages.incapable-executor", "Not a player!"), action);
	}
	
	/**
	 * Requires the executor to be a {@link CommandSender} to run the {@code action}.
	 * <br>
	 * Otherwise, informs the sender with the {@code errorMessage}.
	 *
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 * @param action The action executed if the executor is a player
	 */
	
	public static int requireSender(@NotNull CommandContext<CommandSourceStack> ctx, @Nullable String errorMessage, @NotNull CommandConsumer<CommandSender> action) throws CommandSyntaxException {
		return requireType(ctx, CommandSender.class, errorMessage, action);
	}
	
	/**
	 * Requires the executor to be a {@link CommandSender} to run the {@code action}.
	 * <br>
	 * Otherwise, informs the sender with the {@code errorMessage}.
	 *
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 * @param action The action executed if the executor is a player
	 */
	
	public static int requireSender(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull CommandConsumer<CommandSender> action) throws CommandSyntaxException {
		return requireSender(ctx, "Not a player/console!", action);
	}
	
	/**
	 * Requires the executor to be a {@link CommandSender} to run the {@code action}.
	 * <br>
	 * Otherwise, informs the sender with a message from the {@code errorMessageGetter} which defaults to "Not a player/console".
	 *
	 * @param ctx The {@link CommandContext} of your argument (available in the lambda of {@link ArgumentBuilder#executes executes})
	 * @param action The action executed if the executor is a player
	 */
	
	public static int requireSender(@NotNull CommandContext<CommandSourceStack> ctx, @NonNull CommandOptions errorMessageGetter, @NotNull CommandConsumer<CommandSender> action) throws CommandSyntaxException {
		return requireType(ctx, CommandSender.class, errorMessageGetter.get("messages.incapable-executor", "Not a player/console"), action);
	}
	
	/**
	 * Requires the executor to be a subclass of {@code <T>}/{@code type} to run the {@code action}.
	 * <br>
	 * Otherwise, informs the sender with the {@code errorMessage}.
	 *
	 * @param type The type that the executor has to be
	 * @return {@link Command#SUCCESS SUCCESS}
	 * or {@link Command#FAIL FAIL}
	 * depending on whether the executor is of the specified {@code type}
	 */
	
	public static <T> int requireType(@NonNull CommandContext<CommandSourceStack> ctx, @NonNull Class<T> type, @Nullable String errorMessage, @NonNull CommandConsumer<T> action) throws CommandSyntaxException {
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
	 * <br>
	 * Otherwise, informs the sender with an error-message queried from the {@code errorMessageGetter}.
	 *
	 * @param type The type that the executor has to be
	 * @return {@link Command#SUCCESS SUCCESS}
	 * or {@link Command#FAIL FAIL}
	 * depending on whether the executor is of the specified {@code type}
	 */
	
	public static <T> int requireType(@NotNull CommandContext<CommandSourceStack> ctx, @NotNull Class<T> type, @NonNull CommandOptions errorMessageGetter, @NotNull CommandConsumer<T> action) throws CommandSyntaxException {
		return requireType(ctx, type, errorMessageGetter.get("messages.incapable-executor", "That entity can't run this command!"), action);
	}
	
	/**
	 * @return A simple {@link CommandSyntaxException} with the specified {@link Message} as message.
	 */
	
	public static @NonNull CommandSyntaxException simpleException(@NonNull Message msg) {
		return new SimpleCommandExceptionType(msg).create();
	}
	
	/**
	 * @return A simple {@link CommandSyntaxException} with the specified {@link Component} as message.
	 */
	
	public static @NonNull CommandSyntaxException simpleException(@NonNull Component cmp) {
		return simpleException(TextDeserializer.message(cmp));
	}
	
	/**
	 * @return A simple {@link CommandSyntaxException} with the specified {@link String} as message.
	 */
	
	public static @NonNull CommandSyntaxException simpleException(@NonNull String str) {
		return simpleException(TextDeserializer.message(str));
	}
	
	/**
	 * @return A simple {@link CommandSyntaxException} with the specified {@link String} as red colored message.
	 */
	
	public static @NonNull CommandSyntaxException simpleRedException(@NonNull String str) {
		return simpleException(TextDeserializer.message(Component.text(str, NamedTextColor.RED)));
	}
	
	/**
	 * A consumer that can throw a {@link CommandSyntaxException} to remove extra boilerplate in user code (unnecessary try/catch statements)
	 */
	
	@FunctionalInterface
	public interface CommandConsumer<T> {
		
		void accept(T t) throws CommandSyntaxException;
	}
	
	@FunctionalInterface
	public interface ExtendedContextBiFunction<T> {
		
		int apply(CommandContext<CommandSourceStack> ctx, T t);
	}
}
