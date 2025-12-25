package com.ontey.api;

import com.ontey.api.brigadier.registry.CommandRegistry;
import com.ontey.api.plugin.OnteyPlugin;
import lombok.Getter;

public class OnteyAPI {
   
   @Getter
   private static OnteyPlugin plugin;
   
   public static void load(OnteyPlugin plugin) {
      OnteyAPI.plugin = plugin;
      
      CommandRegistry.load();
   }
}
