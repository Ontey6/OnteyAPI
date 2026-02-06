package ontey.command;

import lombok.NonNull;
import ontey.command.argument.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import ontey.config.ConfigSection;
import ontey.plugin.OnteyPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A command which's names, permission... can be specified by users in the
 * {@link OnteyPlugin#getCommandsConfig commands config} of an {@link OnteyPlugin}.
 */

public class ConfigCommand extends Command {
   
   public final CommandConfiguration defaults, values;
   
   public final CommandOptions options;
   
   public ConfigCommand(@NonNull OnteyPlugin plugin, @NonNull CommandConfiguration defaults) {
      // defaults should always have more than 1 name anyway
      super(plugin, defaults.names().getFirst()); // why can't I just use java 22...
      
      String name = defaults.names().getFirst();
      var config = plugin.getCommandsConfig();
      
      var section = config.getSection(name);
      
      if(section == null)
         section = config.createSection(name, defaults.serialize());
      
      plugin.getCommandsConfig().save();
      
      this.defaults = defaults;
      
      this.values = new CommandConfiguration(
        section.getListable("names"),
        section.getMessage("description", defaults.description()),
        section.getString("permission", defaults.permission()),
        options(section)
      );
      
      this.options = new CommandOptions(section.getSection("options"));
      
      if(!getNames().isEmpty())
         getNames().remove(getName());
      
      getNames().clear();
      
      super
        .setDescription(values.description())
        .setPermission(values.permission())
        .addAliases(values.names());
   }
   
   /**
    * Sets the root but renames the root to this command's configured name.
    */
   
   @Override
   @NonNull
   public Command setRoot(@NonNull LiteralArgumentBuilder root) {
      if(values.names().isEmpty())
         return this;
      
      root
        .requires(source -> permission == null || source.getSender().hasPermission(permission));
      
      var node = new LiteralCommandNode<>(
        values.names().getFirst(),
        root.getCommand(),
        root.getRequirement(),
        root.getRedirect(),
        root.getRedirectModifier(),
        root.isFork()
      );
      
      for(var arg : root.getArguments())
         node.addChild(arg);
      
      this.root = node;
      
      return this;
   }
   
   @NonNull
   private static Map<@NonNull String, @Nullable Object> options(@NonNull ConfigSection section) {
      var options = section.getSection("options");
      
      return options == null ? Map.of() : options.getValues(false);
   }
}
