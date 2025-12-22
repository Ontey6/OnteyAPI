package com.ontey.api;

import com.ontey.api.brigadier.registry.CommandRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class OnteyAPI {
   
   public static void load(JavaPlugin plugin) {
      CommandRegistry.load(plugin);
   }
}
