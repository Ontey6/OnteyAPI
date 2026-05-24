package ontey.api.item;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FoodComponentBuilder {
	
	@NonNull
	@Getter
	private final FoodComponent component;
	
	protected FoodComponentBuilder(@NonNull ItemMeta meta) {
		this.component = meta.getFood();
	}
	
	@NonNull
	public FoodComponentBuilder nutrition(int nutrition) {
		component.setNutrition(nutrition);
		return this;
	}
	
	@NonNull
	public FoodComponentBuilder saturation(float saturation) {
		component.setSaturation(saturation);
		return this;
	}
	
	@NonNull
	public FoodComponentBuilder alwaysEdible(boolean flag) {
		component.setCanAlwaysEat(flag);
		return this;
	}
	
	@NonNull
	public FoodComponentBuilder alwaysEdible() {
		return alwaysEdible(true);
	}
	
	@NonNull
	public FoodComponentBuilder convertsTo(@Nullable ItemStack item) {
		component.setUsingConvertsTo(item);
		return this;
	}
	
	@NonNull
	public FoodComponentBuilder effect(@NonNull PotionEffect effect, float probability) {
		component.addEffect(effect, probability);
		return this;
	}
	
	@NonNull
	public FoodComponentBuilder effects(@NonNull Map<@NonNull PotionEffect, @NonNull Float> effects) {
		effects.forEach(component::addEffect);
		return this;
	}
}
