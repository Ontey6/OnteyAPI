package com.ontey.api.plugin.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

class MenuClickListener implements Listener {
   
   @EventHandler
   public void onClick(InventoryClickEvent event) {
      Inventory inv = event.getClickedInventory();
      
      if(!(inv instanceof final Menu menu)) // also null-check
         return;
      
      event.setCancelled(menu.click(event.getSlot(), (Player) event.getWhoClicked()));
   }
}
