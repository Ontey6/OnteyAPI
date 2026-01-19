package com.ontey.api.plugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuAPI {
   
   public static void load(JavaPlugin plugin) {
      Bukkit.getPluginManager().registerEvents(new MenuClickListener(), plugin);
   }
}
