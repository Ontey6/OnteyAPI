package ontey.api.item;

import com.google.common.collect.Multimap;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import ontey.api.check.Nullity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {
	
	protected final ItemStack item;
	
	protected final ItemMeta meta;
	
	protected ItemBuilder(@NonNull ItemStack item) {
		this.item = item;
		this.meta = item.getItemMeta();
	}
	
	// Create
	
	/**
	 * Creates a new builder that builds a basic {@link ItemStack item}.
	 * Doesn't keep the {@link ItemStack}'s data.
	 *
	 * @see #edit(ItemStack)
	 */
	
	@NonNull
	public static ItemBuilder of(@NonNull Material material) {
		return new ItemBuilder(new ItemStack(material));
	}
	
	/**
	 * Creates a new builder that builds a basic {@link ItemStack item}.
	 * Doesn't keep the {@link ItemStack}'s data.
	 *
	 * @see #edit(ItemStack)
	 */
	
	@NonNull
	public static ItemBuilder of(@NonNull ItemStack item) {
		return of(item.getType());
	}
	
	/**
	 * @return A new {@link ItemStack} with the given material.
	 */
	
	@NonNull
	public static ItemStack simple(Material material) {
		return new ItemStack(material);
	}
	
	/**
	 * Creates a new builder that edits a basic {@link ItemStack item}.
	 */
	
	public static ItemBuilder edit(@NonNull ItemStack item) {
		return new ItemBuilder(item);
	}
	
	/**
	 * Creates a new {@link FoodComponentBuilder}.
	 */
	
	@NonNull
	public static FoodComponentBuilder food(@NonNull Material material) {
		return new FoodComponentBuilder(new ItemStack(material).getItemMeta());
	}
	
	/**
	 * Creates a new {@link FoodComponentBuilder}.
	 */
	
	@NonNull
	public static FoodComponentBuilder food(@NonNull ItemStack item) {
		return food(item.getType());
	}
	
	/**
	 * Creates a new {@link ToolComponentBuilder}.
	 */
	
	@NonNull
	public static ToolComponentBuilder tool(@NonNull Material material) {
		return new ToolComponentBuilder(new ItemStack(material).getItemMeta());
	}
	
	/**
	 * Creates a new {@link ToolComponentBuilder}.
	 */
	
	@NonNull
	public static ToolComponentBuilder tool(@NonNull ItemStack item) {
		return tool(item.getType());
	}
	
	/**
	 * Creates a new {@link JukeboxPlayableComponentBuilder}.
	 */
	
	@NonNull
	public static JukeboxPlayableComponentBuilder jukeboxPlayable(@NonNull Material material) {
		return new JukeboxPlayableComponentBuilder(new ItemStack(material).getItemMeta());
	}
	
	/**
	 * Creates a new {@link JukeboxPlayableComponentBuilder}.
	 */
	
	@NonNull
	public static JukeboxPlayableComponentBuilder jukeboxPlayable(@NonNull ItemStack item) {
		return jukeboxPlayable(item.getType());
	}
	
	/**
	 * Sets the item's display name and removes italic style that is added by default.
	 */
	
	@NonNull
	public ItemBuilder name(@Nullable Component name) {
		meta.customName(Nullity.nonNullOr(name, n -> n.decoration(TextDecoration.ITALIC, false)));
		return this;
	}
	
	/**
	 * Sets the item's display name and removes italic style that is added by default.
	 *
	 * @deprecated - Use {@link #name(Component)} instead
	 */
	
	@NonNull
	@Deprecated(forRemoval = true)
	public ItemBuilder name(@Nullable String name) {
		return name(Nullity.nonNullOr(name, (String s) -> Component.text(s)));
	}
	
	/**
	 * Sets the item's lore.
	 */
	
	@NonNull
	public ItemBuilder lore(@Nullable Component @NonNull ... lore) {
		List<Component> out = new ArrayList<>(lore.length);
		for(Component component : lore)
			out.add(Nullity.nonNullOr(component, c -> c.decoration(TextDecoration.ITALIC, false)));
		meta.lore(out);
		return this;
	}
	
	/**
	 * Sets the item's lore.
	 *
	 * @deprecated - Use {@link #lore(Component...)} instead
	 */
	
	@NonNull
	@Deprecated(forRemoval = true)
	public ItemBuilder lore(@Nullable String @NonNull ... lore) {
		List<Component> out = new ArrayList<>(lore.length);
		for(String str : lore)
			out.add(Nullity.nonNullOr(str, (String s) -> Component.text(s).decoration(TextDecoration.ITALIC, false)));
		meta.lore(out);
		return this;
	}
	
	/**
	 * Enchants the item with the given {@code level} of the {@code enchant}.
	 */
	
	@NonNull
	public ItemBuilder enchant(@NonNull Enchantment enchant, int level) {
		meta.addEnchant(enchant, level, true);
		return this;
	}
	
	/**
	 * Enchants the item with level {@code 1} of the {@code enchant}.
	 */
	
	@NonNull
	public ItemBuilder enchant(@NonNull Enchantment enchant) {
		return enchant(enchant, 1);
	}
	
	/**
	 * Adds enchantment glint to this item without an enchantment
	 */
	
	@NonNull
	public ItemBuilder glowing(boolean value) {
		meta.setEnchantmentGlintOverride(value);
		return this;
	}
	
	/**
	 * Adds enchantment glint to this item without an enchantment
	 */
	
	@NonNull
	public ItemBuilder glowing() {
		return glowing(true);
	}
	
	/**
	 * Makes this item unbreakable; it will never lose durability.
	 * Optionally hides the "unbreakable" tooltip.
	 */
	
	@NonNull
	public ItemBuilder unbreakable(boolean value, boolean hide) {
		meta.setUnbreakable(value);
		if(value && hide)
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		return this;
	}
	
	/**
	 * Makes this item unbreakable; it will never lose durability.
	 */
	
	@NonNull
	public ItemBuilder unbreakable(boolean value) {
		return unbreakable(value, false);
	}
	
	/**
	 * Makes this item unbreakable; it will never lose durability.
	 */
	
	@NonNull
	public ItemBuilder unbreakable() {
		return unbreakable(true, false);
	}
	
	/**
	 * Sets the custom model data of this item.
	 *
	 * @deprecated - Use {@link #itemModel(NamespacedKey)} or {@link #itemModel(String)} instead
	 */
	
	@NonNull
	@Deprecated(forRemoval = true)
	public ItemBuilder customModelData(@Nullable Integer modelData) {
		meta.setCustomModelData(modelData);
		return this;
	}
	
	/**
	 * Sets the item model of this item.
	 * This is the replacement for custom model data.
	 *
	 * @param key The key of the item model.
	 */
	
	@NonNull
	public ItemBuilder itemModel(@Nullable NamespacedKey key) {
		meta.setItemModel(key);
		return this;
	}
	
	/**
	 * Sets the item model of this item.
	 * This is the replacement for custom model data.
	 *
	 * @param identifier The minecraft Identifier of the item model.
	 * Will be parsed as {@code minecraft:<identifier>}
	 */
	
	@NonNull
	public ItemBuilder itemModel(@Nullable String identifier) {
		return itemModel(Nullity.nonNullOr(identifier, NamespacedKey::minecraft));
	}
	
	/**
	 * Sets this item's {@link CustomModelDataComponent}.
	 */
	
	public ItemBuilder customModelDataComponent(@Nullable CustomModelDataComponent component) {
		meta.setCustomModelDataComponent(component);
		return this;
	}
	
	/**
	 * Sets this item's {@link UseCooldownComponent}.
	 */
	
	public ItemBuilder useCooldownComponent(@Nullable UseCooldownComponent component) {
		meta.setUseCooldown(component);
		return this;
	}
	
	/**
	 * Adds {@link ItemFlag}s.
	 */
	
	@NonNull
	public ItemBuilder itemFlags(@NonNull ItemFlag @NonNull ... flags) {
		meta.addItemFlags(flags);
		return this;
	}
	
	/**
	 * Sets whether to fully hide the tooltip.
	 */
	
	@NonNull
	public ItemBuilder hideTooltip(boolean flag) {
		meta.setHideTooltip(flag);
		return this;
	}
	
	/**
	 * Sets whether to fully hide the tooltip.
	 */
	
	@NonNull
	public ItemBuilder hideTooltip() {
		return hideTooltip(true);
	}
	
	/**
	 * Sets whether this item is immune to burning in fire/lava.
	 */
	
	@NonNull
	@Deprecated(forRemoval = true)
	public ItemBuilder fireResistant(boolean flag) {
		meta.setFireResistant(flag);
		return this;
	}
	
	/**
	 * Makes this item immune to burning in fire/lava.
	 */
	
	@NonNull
	@Deprecated(forRemoval = true)
	public ItemBuilder fireResistant() {
		return fireResistant(true);
	}
	
	/**
	 * Sets this item's maximum stack size.
	 * Can only be 1-99.
	 */
	
	public ItemBuilder maxStackSize(@Nullable Integer max) {
		meta.setMaxStackSize(max);
		return this;
	}
	
	/**
	 * Sets this item's rarity.
	 */
	
	public ItemBuilder rarity(ItemRarity rarity) {
		meta.setRarity(rarity);
		return this;
	}
	
	/**
	 * Sets this item's tool component.
	 * That e.g. determines the mining speed for specific blocks or block tags.
	 */
	
	public ItemBuilder tool(ToolComponent tool) {
		meta.setTool(tool);
		return this;
	}
	
	/**
	 * Sets this item's tool component.
	 * That e.g. determines the mining speed for specific blocks or block tags.
	 */
	
	public ItemBuilder tool(ToolComponentBuilder builder) {
		return tool(builder.getComponent());
	}
	
	/**
	 * Sets this item's food component.
	 * That e.g. determines how much hunger points the item gives when eaten.
	 * Makes it edible.
	 */
	
	public ItemBuilder food(FoodComponent food) {
		meta.setFood(food);
		return this;
	}
	
	/**
	 * Sets this item's food component.
	 * That e.g. determines how much hunger points the item gives when eaten.
	 * Makes it edible.
	 */
	
	public ItemBuilder food(FoodComponentBuilder builder) {
		return food(builder.getComponent());
	}
	
	/**
	 * Sets this item's jukebox-playable component.
	 * That determines what song to play when put into a jukebox.
	 * Makes it playable in jukeboxes.
	 */
	
	public ItemBuilder jukeboxPlayable(JukeboxPlayableComponent jukeboxPlayable) {
		meta.setJukeboxPlayable(jukeboxPlayable);
		return this;
	}
	
	/**
	 * Sets this item's jukebox-playable component.
	 * That determines what song to play when put into a jukebox.
	 * Makes it playable in jukeboxes.
	 */
	
	public ItemBuilder jukeboxPlayable(JukeboxPlayableComponentBuilder builder) {
		return jukeboxPlayable(builder.getComponent());
	}
	
	/**
	 * Adds {@link AttributeModifier}s.
	 * <br>
	 * {@code attributes} should not be null, as a null value is only used
	 * to clear all attributes which won't be necessary in a builder.
	 */
	
	@NonNull
	public ItemBuilder attributes(@NonNull Multimap<@NonNull Attribute, @NonNull AttributeModifier> attributes) {
		meta.setAttributeModifiers(attributes);
		return this;
	}
	
	/**
	 * Adds an {@link AttributeModifier}.
	 */
	
	@NonNull
	public ItemBuilder attribute(@NonNull Attribute attribute, @NonNull AttributeModifier modifier) {
		meta.addAttributeModifier(attribute, modifier);
		return this;
	}
	
	/**
	 * Allows you to edit the meta
	 */
	
	public ItemBuilder editMeta(@NonNull Consumer<@NonNull ItemMeta> metaConsumer) {
		metaConsumer.accept(meta);
		return this;
	}
	
	// Build
	
	/**
	 * Builds the item.
	 * Should be the last thing you use this builder for.
	 */
	
	@NonNull
	public ItemStack build() {
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * Gives the item to the specified player.
	 */
	
	public ItemBuilder give(Player player) {
		player.getInventory().addItem(build());
		return this;
	}
	
	/**
	 * Spawns the item in the specified world at the specified location.
	 * Ignores the location's world.
	 */
	
	public ItemBuilder spawn(World world, Location location) {
		world.spawn(location, Item.class, item -> item.setItemStack(build()));
		return this;
	}
	
	/**
	 * Spawns the item in the location's world at the specified location.
	 * Uses the location's world.
	 */
	
	public ItemBuilder spawn(Location location) {
		return spawn(location.getWorld(), location);
	}
}
