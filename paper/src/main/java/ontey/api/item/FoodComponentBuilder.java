package ontey.api.item;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;

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
}
