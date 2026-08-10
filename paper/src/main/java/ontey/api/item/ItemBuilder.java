package ontey.api.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.item.*;
import lombok.NonNull;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.papermc.paper.datacomponent.DataComponentTypes.*;

public class ItemBuilder {
	
	protected final ItemStack item;
	
	protected ItemBuilder(@NonNull ItemStack item) {
		this.item = item;
	}
	
	// Create
	
	/**
	 * Creates a new builder that builds a basic {@link ItemStack item}.
	 * Doesn't keep the {@link ItemStack}'s data.
	 *
	 * @see #edit(ItemStack)
	 */
	
	@NonNull
	public static ItemBuilder of(@NonNull ItemType material) {
		return new ItemBuilder(material.createItemStack());
	}
	
	/**
	 * Creates a new builder that builds a basic {@link ItemStack item}.
	 * Doesn't keep the {@link ItemStack}'s data.
	 *
	 * @see #edit(ItemStack)
	 */
	
	@NonNull
	public static ItemBuilder of(@NonNull ItemStack item) {
		return of(item.getType().asItemType());
	}
	
	/**
	 * Creates a new builder that edits a basic {@link ItemStack item}.
	 */
	
	@NonNull
	public static ItemBuilder edit(@NonNull ItemStack item) {
		return new ItemBuilder(item);
	}
	
	@NonNull
	public ItemBuilder data(@NonNull DataComponentType.NonValued data) {
		item.setData(data);
		return this;
	}
	
	@NonNull
	public <T> ItemBuilder data(@NonNull DataComponentType.Valued<T> data, T value) {
		item.setData(data, value);
		return this;
	}
	
	@NonNull
	public <T> ItemBuilder data(@NonNull DataComponentType.Valued<T> data, DataComponentBuilder<T> value) {
		item.setData(data, value);
		return this;
	}
	
	/**
	 * Sets the item's display name and removes italic style that is added by default.
	 */
	
	@NonNull
	public ItemBuilder name(@NonNull Component name) {
		return data(ITEM_NAME, name);
	}
	
	/**
	 * Sets the item's lore.
	 */
	
	@NonNull
	public ItemBuilder lore(@NonNull Component @NonNull ... lore) {
		return data(LORE, ItemLore.lore(List.of(lore)));
	}
	
	/**
	 * Enchants the item with the enchantment
	 */
	
	@NonNull
	public ItemBuilder enchantment(@NonNull Enchantment enchantment, int level) {
		var alreadySetEnchantments = item.getData(ENCHANTMENTS);
		var enchantments = ItemEnchantments.itemEnchantments().add(enchantment, level);
		
		if(alreadySetEnchantments != null)
			enchantments.addAll(alreadySetEnchantments.enchantments());
		
		return data(ENCHANTMENTS, enchantments);
	}
	
	/**
	 * Adds enchantment glint to this item without an enchantment
	 */
	
	@NonNull
	public ItemBuilder enchantmentGlintOverride(boolean value) {
		return data(ENCHANTMENT_GLINT_OVERRIDE, value);
	}
	
	/**
	 * Adds enchantment glint to this item without an enchantment
	 */
	
	@NonNull
	public ItemBuilder enchantmentGlintOverride() {
		return enchantmentGlintOverride(true);
	}
	
	/**
	 * Makes this item unbreakable; it will never lose durability.
	 */
	
	@NonNull
	public ItemBuilder unbreakable(boolean value) {
		if(value)
			data(UNBREAKABLE);
		return this;
	}
	
	/**
	 * Makes this item unbreakable; it will never lose durability.
	 */
	
	@NonNull
	public ItemBuilder unbreakable() {
		return unbreakable(true);
	}
	
	/**
	 * Sets the item model of this item.
	 * This is the replacement for custom model data (The integer, not CustomModelData).
	 *
	 * @param key The key of the item model.
	 */
	
	@NonNull
	public ItemBuilder itemModel(@Nullable Key key) {
		return data(ITEM_MODEL, key);
	}
	
	/**
	 * Sets the cooldown after you use the item
	 */
	
	public ItemBuilder usageCooldown(@Nullable UseCooldown cooldown) {
		return data(USE_COOLDOWN, cooldown);
	}
	
	public ItemBuilder tooltipDisplay(TooltipDisplay tooltipDisplay) {
		return data(TOOLTIP_DISPLAY, tooltipDisplay);
	}
	
	/**
	 * Sets this item's maximum stack size.
	 * Can only be 1-99.
	 */
	
	public ItemBuilder maxStackSize(@Nullable Integer max) {
		return data(MAX_STACK_SIZE, max);
	}
	
	/**
	 * Sets this item's rarity.
	 */
	
	@NonNull
	public ItemBuilder rarity(@NonNull ItemRarity rarity) {
		return data(RARITY, rarity);
	}
	
	/**
	 * Sets this item's tool component.
	 * That e.g. determines the mining speed for specific blocks or block tags.
	 */
	
	@NonNull
	public ItemBuilder tool(@NonNull Tool tool) {
		return data(TOOL, tool);
	}
	
	/**
	 * Sets this item's weapon component.
	 * That e.g. determines the damage the item deals.
	 */
	
	@NonNull
	public ItemBuilder weapon(@NonNull Weapon weapon) {
		return data(WEAPON, weapon);
	}
	
	/**
	 * Makes this item equippable and
	 */
	
	public ItemBuilder equippable(Equippable equippable) {
		return data(EQUIPPABLE, equippable);
	}
	
	/**
	 * Sets this item's food component.
	 * That e.g. determines how much hunger points the item gives when eaten.
	 * Makes it edible.
	 */
	
	@NonNull
	public ItemBuilder food(@NonNull FoodProperties food) {
		return data(FOOD, food);
	}
	
	/**
	 * Sets this item's jukebox-playable component.
	 * That determines what song to play when put into a jukebox.
	 * Makes it playable in jukeboxes.
	 */
	
	@NonNull
	public ItemBuilder jukeboxPlayable(@NonNull JukeboxPlayable jukeboxPlayable) {
		return data(JUKEBOX_PLAYABLE, jukeboxPlayable);
	}
	
	/**
	 * Adds {@link AttributeModifier}s.
	 * <br>
	 * {@code attributes} should not be null, as a null value is only used
	 * to clear all attributes which won't be necessary in a builder.
	 */
	
	@NonNull
	public ItemBuilder attributes(@NonNull ItemAttributeModifiers attributes) {
		return data(ATTRIBUTE_MODIFIERS, attributes);
	}
	
	// Build
	
	/**
	 * Builds the item.
	 */
	
	@NonNull
	public ItemStack build() {
		return item;
	}
	
	/**
	 * Gives the item to the specified player.
	 */
	
	@NonNull
	public ItemBuilder give(Player player) {
		player.getInventory().addItem(build());
		return this;
	}
	
	/**
	 * Spawns the item in the specified world at the specified location.
	 * Ignores the location's world.
	 */
	
	@NonNull
	public ItemBuilder spawn(World world, Location location) {
		world.spawn(location, Item.class, item -> item.setItemStack(build()));
		return this;
	}
	
	/**
	 * Spawns the item in the location's world at the specified location.
	 * Uses the location's world.
	 */
	
	@NonNull
	public ItemBuilder spawn(Location location) {
		return spawn(location.getWorld(), location);
	}
}
