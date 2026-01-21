package com.ontey.api.plugin.brigadier.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ontey.api.plugin.brigadier.config.*;
import com.ontey.api.plugin.files.Files;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class ConfigCommand extends Command {
   
   public final CommandConfiguration defaults, values;
   
   public final CommandOptions options;
   
   public ConfigCommand(@NotNull ConfigurationSection config, @NotNull CommandConfiguration defaults) {
      // defaults should always have more than 1 name anyway
      super(defaults.names().getFirst()); // why can't I just use java 22...
      
      Objects.requireNonNull(config);
      
      String name = defaults.names().getFirst();
      
      var section = config.getConfigurationSection(name);
      
      if(section == null)
         section = config.createSection(name, defaults.serialize());
      
      this.defaults = defaults;
      
      this.values = new CommandConfiguration(
        Files.getListable(section, "names"),
        Files.getMessage(section, "description", defaults.description()),
        section.getString("permission", defaults.permission()),
        options(section)
      );
      
      this.options = new CommandOptions(section.getConfigurationSection("options"));
      
      if(!getNames().isEmpty())
         getNames().remove(getName());
      
      super.getNames().clear();
      
      super
        .setDescription(values.description())
        .setPermission(values.permission())
        .addAliases(values.names());
   }
   
   @Override
   public ConfigCommand setRoot(LiteralArgumentBuilder<CommandSourceStack> root) {
      if(values.names().isEmpty())
         return this;
      
      root
        .requires(source -> permission == null || source.getSender().hasPermission(permission));
      
      LiteralCommandNode<CommandSourceStack> node = new LiteralCommandNode<>(
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
   
   private static Map<String, Object> options(ConfigurationSection section) {
      var opt = section.getConfigurationSection("options");
      
      return opt == null ? Map.of() : opt.getValues(false);
   }
}
