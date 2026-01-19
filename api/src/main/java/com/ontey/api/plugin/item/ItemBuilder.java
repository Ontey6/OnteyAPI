package com.ontey.api.plugin.item;

import com.google.common.collect.Multimap;
import com.ontey.api.plugin.color.MinecraftColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;

@NullMarked
public class ItemBuilder {
   
   protected final ItemStack item;
   protected final ItemMeta meta;
   
   protected ItemBuilder(ItemStack item) {
      this.item = item;
      this.meta = item.getItemMeta();
   }
   
   // Create
   
   /**
    * Creates a new builder with the {@code material}.
    */
   
   public static ItemBuilder of(Material material) {
      return new ItemBuilder(new ItemStack(material));
   }
   
   /**
    * Creates a new builder with the {@code item}'s material.
    * Doesn't keep the {@link ItemStack}'s data.
    * @see ItemBuilder#edit(ItemStack)
    */
   
   public static ItemBuilder of(ItemStack item) {
      return of(item.getType());
   }
   
   /**
    * Creates a new builder that edits the {@code item}; keeps the item's current data.
    */
   
   public static ItemBuilder edit(ItemStack item) {
      return new ItemBuilder(item);
   }
   
   // Edit
   
   /**
    * Sets the item's display name and removes italic style that is added by default.
    */
   
   public ItemBuilder name(@Nullable Component name) {
      meta.displayName(name == null ? null : name.decoration(TextDecoration.ITALIC, false));
      return this;
   }
   
   /**
    * Sets the item's display name and removes italic style that is added by default.
    */
   
   public ItemBuilder name(@Nullable String name) {
      return name(name == null ? null : MinecraftColor.colorize(name));
   }
   
   /**
    * Sets the item's lore.
    */
   
   public ItemBuilder lore(@Nullable Component ... name) {
      meta.lore(Arrays.asList(name));
      return this;
   }
   
   /**
    * Sets the item's lore.
    */
   
   public ItemBuilder lore(String... name) {
      return lore(Arrays.stream(name).map(MinecraftColor::colorize).toArray(Component[]::new));
   }
   
   /**
    * Enchants the item with the given {@code level} of the {@code enchant}.
    */
   
   public ItemBuilder enchant(Enchantment enchant, int level) {
      meta.addEnchant(enchant, level, true);
      return this;
   }
   
   /**
    * Enchants the item with level {@code 1} of the {@code enchant}.
    */
   
   public ItemBuilder enchant(Enchantment enchant) {
      return enchant(enchant, 1);
   }
   
   /**
    * Adds enchantment glint to this item without an enchantment
    */
   
   public ItemBuilder glowing(boolean value) {
      meta.setEnchantmentGlintOverride(value);
      return this;
   }
   
   /**
    * Adds enchantment glint to this item without an enchantment
    */
   
   public ItemBuilder glowing() {
      return glowing(true);
   }
   
   /**
    * Makes this item unbreakable; it will never lose durability.
    * Optionally hides the "unbreakable" tooltip.
    */
   
   public ItemBuilder unbreakable(boolean value, boolean hide) {
      meta.setUnbreakable(value);
      if(value && hide)
         meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
      return this;
   }
   
   /**
    * Makes this item unbreakable; it will never lose durability.
    */
   
   public ItemBuilder unbreakable(boolean value) {
      return unbreakable(value, false);
   }
   
   /**
    * Makes this item unbreakable; it will never lose durability.
    */
   
   public ItemBuilder unbreakable() {
      return unbreakable(true, false);
   }
   
   /**
    * Sets the custom model data of this item.
    */
   
   public ItemBuilder customModelData(@Nullable Integer modelData) {
      meta.setCustomModelData(modelData);
      return this;
   }
   
   /**
    * Adds {@link ItemFlag ItemFlags}.
    */
   
   public ItemBuilder itemFlags(ItemFlag... flags) {
      meta.addItemFlags(flags);
      return this;
   }
   
   /**
    * Adds {@link AttributeModifier Attribute modifiers}
    */
   
   public ItemBuilder attributes(Multimap<Attribute, AttributeModifier> attributes) {
      meta.setAttributeModifiers(attributes);
      return this;
   }
   
   // Build
   
   /**
    * Builds the item.
    * Should be the last thing you use this builder for.
    * If you need another Item with the built one as preset, {@link #edit(ItemStack) edit} the built item.
    */
   
   public ItemStack build() {
      item.setItemMeta(meta);
      return item;
   }
}
