package com.ontey.api.brigadier.registry;

import com.ontey.api.OnteyAPI;
import com.ontey.api.brigadier.command.Command;
import com.ontey.api.plugin.OnteyPlugin;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public final class CommandRegistry {
   
   public static final List<Command> commands = new ArrayList<>();
   
   public static void registerCommands(Commands registrar) {
      // only commands that have more than 1 name are here
      for(var cmd : CommandRegistry.commands)
         registrar.register(cmd.getRoot(), cmd.getDescription(), cmd.getAliases());
   }
   
   @SneakyThrows
   public static void reload() {
      Bukkit.getServer().getClass().getMethod("syncCommands").invoke(Bukkit.getServer());
   }
   
   public static void load() {
      OnteyAPI.getPlugin().getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
         registerCommands(commands.registrar())
      );
   }
}