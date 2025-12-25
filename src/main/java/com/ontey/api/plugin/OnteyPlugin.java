package com.ontey.api.plugin;

import com.ontey.api.OnteyAPI;
import com.ontey.api.log.PluginLogger;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OnteyPlugin extends JavaPlugin {
   
   @Getter
   private static JavaPlugin plugin;
   
   @Getter
   protected static String version;
   
   @Getter
   private static PluginManager pluginManager;
   
   @Getter
   private static ServicesManager servicesManager;
   
   @Getter
   private static PluginLogger logger;
   
   /**
    * Initializes this class and loads the API.
    * <p>
    * Supposed to be called at first place in the {@link JavaPlugin#onLoad() onLoad()} or {@link JavaPlugin#onEnable() onEnable()} method.
    */
   
   public void load(String loggerName) {
      plugin = this;
      version = getPluginMeta().getVersion();
      
      pluginManager = getServer().getPluginManager();
      servicesManager = getServer().getServicesManager();
      
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
