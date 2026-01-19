package com.ontey.api.plugin.gui;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@NullMarked
public class SimpleMenu implements Menu {
   
   @Getter
   private Component name;
   
   @Getter
   private final int rows, slots;
   
   @Getter
   private final Inventory inventory;
   
   protected final Map<Integer, Consumer<Player>> clickActions;
   
   private static final int MIN = 1, MAX = 6;
   
   private SimpleMenu(@Range(from = MIN, to = MAX) int rows, Component name) {
      Objects.checkFromToIndex(MIN, MAX, rows);
      Objects.requireNonNull(name);
      
      this.rows = rows;
      this.slots = rows * 9;
      this.inventory = Bukkit.createInventory(null, slots, name);
      this.clickActions = new HashMap<>(slots);
      this.name = name;
   }
   
   @Override
   public boolean click(int slot, Player player) {
      return true;
   }
   
   @Override
   public void setItem(int slot, @Nullable ItemStack item, Consumer<Player> onClick) {
   
   }
   
   public void setName(Component name) {
      this.name = Objects.requireNonNull(name);
   }
   
   @Override
   public void close(Player player) {
   
   }
}
