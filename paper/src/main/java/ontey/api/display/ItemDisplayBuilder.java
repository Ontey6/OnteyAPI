package ontey.api.display;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

public class ItemDisplayBuilder extends DisplayBuilder<ItemDisplayBuilder> {
	
	@NonNull
	private final ItemStack item;
	
	ItemDisplayBuilder(@NonNull ItemStack item) {
		this.item = item;
	}
	
	@Override
	protected ItemDisplayBuilder getThis() {
		return this;
	}
	
	@Override
	public ItemDisplay spawn(@NonNull World world, @NonNull Location coordinates) {
		return world.spawn(coordinates, ItemDisplay.class, display -> {
			applyBeforeSpawning(display);
			display.setItemStack(item);
		});
	}
	
	@Override
	public ItemDisplay spawn(@NonNull Location location) {
		return spawn(location.getWorld(), location);
	}
}
