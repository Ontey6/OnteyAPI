package ontey.api.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import lombok.NonNull;
import ontey.api.command.argument.Arg;
import ontey.api.command.registry.RegistryCommand;
import ontey.api.util.DurationFormatter;
import ontey.api.command.registry.CommandRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

public abstract class Command {
	
	/**
	 * A command's return values for success.
	 * Just for code-readability, value doesn't have any meaning
	 */
	
	public static final int SUCCESS = 1;
	
	/**
	 * A command's return values for fail.
	 * Just for code-readability, value doesn't have any meaning
	 */
	
	public static final int FAIL = 0;
	
	@NonNull
	@Getter
	protected final String name;
	
	@NonNull
	@Getter
	protected LiteralArgumentBuilder<CommandSourceStack> root;
	
	@NonNull
	@Getter
	protected List<@NonNull String> aliases = new ArrayList<>(5);
	
	@Nullable
	@Getter
	protected String description;
	
	@Nullable
	@Getter
	protected String permission;
	
	@Getter
	protected boolean consoleOnly = false;
	
	@NonNull
	@Getter
	protected Duration cooldown = Duration.ZERO;
	
	@NonNull
	@ApiStatus.Internal
	private final Map<@NonNull UUID, @NonNull Long> COOLDOWNS = new HashMap<>();
	
	private final Map<UUID, BukkitTask> WARMUP_TASKS = new HashMap<>();
	
	private static final DynamicCommandExceptionType COOLDOWN_EXCEPTION = new DynamicCommandExceptionType(formattedCooldown ->
	  new LiteralMessage("Command is on a cooldown! Wait " + formattedCooldown)
	);
	
	/**
	 * @return A copy of this command's {@link #COOLDOWNS} map.
	 */
	
	public @NonNull Map<@NonNull UUID, @NonNull Long> getCooldowns() {
		return Map.copyOf(COOLDOWNS);
	}
	
	public Command(@NonNull String name) {
		this.name = name;
		this.root = Arg.literal(name);
	}
	
	/**
	 * Builds this mutable command into an immutable {@link RegistryCommand}
	 * which can be registered in the {@link CommandRegistry}.
	 * The checks for {@link #consoleOnly} and {@link #permission} are merged into the root node's requirement.
	 */
	
	public RegistryCommand build() {
		Predicate<CommandSourceStack> check =
		  source -> (permission == null || source.getSender().hasPermission(permission))
			 && (!consoleOnly || source.getSender() instanceof ConsoleCommandSender);
		
		var requirement = check.and(root.getRequirement());
		root.requires(requirement);
		
		return new RegistryCommand(name, aliases, description, root.build());
	}
	
	/**
	 * The sender is automatically passed if not a player.
	 * Checks and possibly updates the cooldown for the player.
	 * Put this on the first line of your command like this if you want to use a cooldown:
	 * <pre>{@code
	 * .executes(ctx -> {
	 *    checkAndUpdateCooldown(ctx.getSource().getSender());
	 *
	 *    //...
	 * })
	 * }</pre>
	 *
	 * @throws CommandSyntaxException If the player is on cooldown. Creates a {@link #COOLDOWN_EXCEPTION}
	 */
	
	protected void checkAndUpdateCooldown(@NonNull CommandSender sender) throws CommandSyntaxException {
		if(cooldown.isZero() || !(sender instanceof Player player))
			return;
		
		checkAndUpdateCooldown(player.getUniqueId());
	}
	
	/**
	 * Checks and possibly updates the cooldown for the player with the given UUID.
	 *
	 * @throws CommandSyntaxException If the player is on cooldown. Creates a {@link #COOLDOWN_EXCEPTION}
	 * @see #checkAndUpdateCooldown(CommandSender)
	 */
	
	protected void checkAndUpdateCooldown(@NonNull UUID uuid) throws CommandSyntaxException {
		if(cooldown.isZero())
			return;
		
		if(!COOLDOWNS.containsKey(uuid)) {
			COOLDOWNS.put(uuid, System.currentTimeMillis());
		} else {
			Duration remainingCooldown = getRemainingCooldown(uuid);
		
			if(remainingCooldown.isPositive())
				throw COOLDOWN_EXCEPTION.create(formatRemaining(remainingCooldown));
			else
				COOLDOWNS.put(uuid, System.currentTimeMillis());
		}
	}
	
	/**
	 * Formats the remaining duration into a human-readable format, returns 0s if the duration is 0 or negative.
	 *
 	 * @param remaining The remaining duration
	 */
	
	@NonNull
	private String formatRemaining(@NonNull Duration remaining) {
		if(remaining.isZero() || remaining.isNegative())
			return "0s";
		
		return DurationFormatter.formatHumanReadable(remaining);
	}
	
	/**
	 * @return The remaining cooldown for the player with the given UUID.
	 */
	
	@Contract(pure = true)
	@NonNull
	protected Duration getRemainingCooldown(@NonNull UUID uuid) {
		return Duration.ofMillis(cooldown.toMillis() - (System.currentTimeMillis() - COOLDOWNS.get(uuid)));
	}
}
