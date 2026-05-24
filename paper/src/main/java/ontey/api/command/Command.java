package ontey.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import lombok.NonNull;
import ontey.api.command.argument.Arg;
import ontey.api.command.registry.RegistryCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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
	
	public Command(@NonNull String name) {
		this.name = name;
		this.root = Arg.literal(name);
	}
	
	public RegistryCommand build() {
		Predicate<CommandSourceStack> check =
		  source -> (permission == null || source.getSender().hasPermission(permission))
			 && (!consoleOnly || source.getSender() instanceof ConsoleCommandSender);
		
		var requirement = check.and(root.getRequirement());
		root.requires(requirement);
		
		return new RegistryCommand(name, aliases, description, root.build());
	}
}
