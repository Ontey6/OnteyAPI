package com.ontey.api;

import com.ontey.api.brigadier.registry.CommandRegistry;
import com.ontey.api.plugin.OnteyPlugin;

public class OnteyAPI {
   
   public static OnteyPlugin plugin;
   
   public static void load(OnteyPlugin plugin) {
      OnteyAPI.plugin = plugin;
      
      CommandRegistry.load();
   }
}
