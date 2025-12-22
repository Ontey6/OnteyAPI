package com.ontey.api.plugin;

import com.ontey.api.OnteyAPI;
import com.ontey.api.log.PluginLogger;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OnteyPlugin extends JavaPlugin {
   
   public static JavaPlugin plugin;
   public static String version;
   
   public static Server server;
   public static CraftServer craftServer;
   
   public static PluginManager pluginManager;
   public static ServicesManager servicesManager;
   
   public static PluginLogger logger;
   
   /**
    * Initializes this class and loads the API.
    * <p>
    * Supposed to be called at first place in the {@link JavaPlugin#onLoad() onLoad()} or {@link JavaPlugin#onEnable() onEnable()} method.
    */
   
   public void load(String loggerName) {
      plugin = this;
      version = getPluginMeta().getVersion();
      
      server = getServer();
      craftServer = (CraftServer) server;
      
      pluginManager = server.getPluginManager();
      servicesManager = server.getServicesManager();
      
      logger = new PluginLogger(loggerName);
      
      OnteyAPI.load(this);
   }
   
   /**
    * Initializes this class and loads the API.
    * <p>
    * Supposed to be called at first place in the {@link JavaPlugin#onLoad() onLoad()} or {@link JavaPlugin#onEnable() onEnable()} method.
    */
   
   public void load() {
      load(getName());
   }
   
   public static void disablePlugin(String reason) {
      logger.error("Disabling Plugin: " + reason);
      pluginManager.disablePlugin(plugin);
   }
   
   public static void disablePlugin() {
      logger.error("Disabling Plugin");
      pluginManager.disablePlugin(plugin);
   }
}
