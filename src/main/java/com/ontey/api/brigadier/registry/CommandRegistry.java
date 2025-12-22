package com.ontey.api.brigadier.registry;

import com.ontey.api.brigadier.command.Command;
import com.ontey.api.plugin.OnteyPlugin;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

import java.util.ArrayList;
import java.util.List;

public final class CommandRegistry {
   
   public static final List<Command> commands = new ArrayList<>();
   
   public static void registerCommands(Commands registrar) {
      // only commands that have more than 1 name are here
      for(var cmd : CommandRegistry.commands)
         registrar.register(cmd.getRoot(), cmd.getDescription(), cmd.getAliases());
   }
   
   public static void reload() {
      ((CraftServer) Bukkit.getServer()).syncCommands();
   }
   
   public static void load() {
      OnteyPlugin.plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
         registerCommands(commands.registrar())
      );
   }
}