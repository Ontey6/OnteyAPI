package ontey.api.command.builder;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import lombok.NonNull;
import ontey.api.command.Command;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandBuilder extends LiteralArgumentBuilder<CommandSourceStack> {
	
	@NonNull
	@Getter
	protected final String name;
	
	@Getter
	protected List<@NonNull String> aliases;
	
	@Nullable
	@Getter
	protected String description;
	
	@Nullable
	@Getter
	protected String permission;
	
	@Getter
	protected boolean consoleOnly;
	
	private CommandBuilder(@NonNull String name) {
		super(name);
		this.name = name;
	}
	
	@NonNull
	@Contract("_ -> new")
	public static CommandBuilder name(@NonNull String name) {
		return new CommandBuilder(name);
	}
	
	@NonNull
	public CommandBuilder aliases(@NonNull List<@NonNull String> aliases) {
		this.aliases = aliases;
		return this;
	}
	
	@NonNull
	public CommandBuilder description(@Nullable String description) {
		this.description = description;
		return this;
	}
	
	@NonNull
	public CommandBuilder permission(@Nullable String permission) {
		this.permission = permission;
		return this;
	}
	
	@NonNull
	public CommandBuilder consoleOnly(boolean consoleOnly) {
		this.consoleOnly = consoleOnly;
		return this;
	}
	
	@Override
	@NonNull
	protected CommandBuilder getThis() {
		return this;
	}
	
	public Command buildCommand() {
		return new Command(name) {
			{
				this.root = CommandBuilder.this;
				this.aliases = CommandBuilder.this.aliases;
				this.description = CommandBuilder.this.description;
				this.permission = CommandBuilder.this.permission;
				this.consoleOnly = CommandBuilder.this.consoleOnly;
			}
		};
	}
}
