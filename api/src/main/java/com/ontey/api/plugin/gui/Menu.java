package com.ontey.api.plugin.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

@NullMarked
public interface Menu extends InventoryHolder {
   
   boolean click(int slot, Player player);
   
   Component getName();
   
   @Range(from = 1, to = 6)
   int getRows();
   
   @Range(from = 9, to = 54)
   int getSlots();
   
   default void setItem(int slot, @Nullable ItemStack item) {
      getInventory().setItem(slot, item);
   }
   
   void setItem(int slot, @Nullable ItemStack item, Consumer<Player> onClick);
   
   default void open(Player player) {
      player.openInventory(getInventory());
   }
   
   default void close(Player player) {
      if(player.getOpenInventory() == getInventory())
         player.closeInventory();
   }
}
