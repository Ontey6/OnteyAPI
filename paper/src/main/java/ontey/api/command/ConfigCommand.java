package ontey.api.command;

import lombok.NonNull;
import ontey.api.command.argument.Arg;
import ontey.api.command.config.CommandConfig;
import ontey.api.command.config.CommandOptions;
import ontey.api.command.registry.RegistryCommand;
import ontey.api.config.yaml.file.YamlFile;
import ontey.api.plugin.OnteyPlugin;

public class ConfigCommand extends Command {
	
	protected final CommandConfig defaults;
	
	protected final CommandConfig values;
	
	protected final CommandOptions options;
	
	public ConfigCommand(@NonNull YamlFile commandsConfig, @NonNull CommandConfig defaults) {
		super(defaults.name());
		
		this.defaults = defaults;
		this.values = commandsConfig.getSerializable(defaults.name(), CommandConfig.class, defaults);
		this.root = Arg.literal(values.name());
		this.options = new CommandOptions(commandsConfig.getSection(values.name()));
		
		this.aliases = values.aliases();
		this.description = values.description();
		this.permission = values.permission();
		this.consoleOnly = values.consoleOnly();
	}
	
	public ConfigCommand(@NonNull OnteyPlugin plugin, @NonNull CommandConfig defaults) {
		this(plugin.getCommandsConfig(), defaults);
	}
	
	public RegistryCommand build() {
		var source = super.build();
		
		return new RegistryCommand(source.name(), source.aliases(), source.description(), source.root(), values::enabled);
	}
}
