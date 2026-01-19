package com.ontey.api;

import com.ontey.api.java.log.Log;
import com.ontey.api.java.log.PluginLogger;
import com.ontey.api.plugin.OnteyPlugin;
import com.ontey.api.plugin.brigadier.registry.CommandRegistry;
import com.ontey.api.plugin.gui.MenuAPI;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class OnteyAPI {
   
   public static final PluginLogger logger = Log.of("OnteyAPI");
   
   public static void load(OnteyPlugin plugin) {
      Objects.requireNonNull(plugin);
      
      CommandRegistry.load(plugin);
      // unfinished
      //MenuAPI.load(plugin);
   }
}
