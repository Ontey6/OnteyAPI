package com.ontey.api.plugin;

import com.ontey.api.java.filelog.FileLog;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class OnteyPlugin extends JavaPlugin {
   
   @Getter(AccessLevel.PROTECTED)
   private static final PluginManager pluginManager = Bukkit.getPluginManager();
   
   @Getter(AccessLevel.PROTECTED)
   private static final ServicesManager servicesManager = Bukkit.getServicesManager();
   
   public final FileLog fileLog = new FileLog(this);
}
