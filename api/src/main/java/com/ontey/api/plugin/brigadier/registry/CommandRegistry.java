package com.ontey.api.plugin.brigadier.registry;

import com.ontey.api.plugin.brigadier.command.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NullMarked
public final class CommandRegistry {
   
   public static final List<Command> commands = new ArrayList<>();
   
   public static void registerCommands(Commands registrar) {
      Objects.requireNonNull(registrar, "registrar");
      
      // only commands that have more than 1 name are here
      for(var cmd : commands)
         registrar.register(cmd.getRoot(), cmd.getDescription(), cmd.getAliases());
   }
   
   @SneakyThrows // method should always exist
   public static void reload() {
      Bukkit.getServer().getClass().getMethod("syncCommands").invoke(Bukkit.getServer());
   }
   
   public static void load(JavaPlugin plugin) {
      Objects.requireNonNull(plugin, "plugin")
        .getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
          registerCommands(commands.registrar())
        );
   }
}