package ontey.api.display;

import lombok.NonNull;
import ontey.api.check.Nullity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;

public class BlockDisplayBuilder extends DisplayBuilder<BlockDisplayBuilder> {
	
	@NonNull
	private final BlockData blockData;
	
	BlockDisplayBuilder(@NonNull BlockData blockData) {
		this.blockData = blockData;
	}
	
	@Override
	protected BlockDisplayBuilder getThis() {
		return this;
	}
	
	@Override
	public BlockDisplay spawn(@NonNull World world, @NonNull Location coordinates) {
		return world.spawn(coordinates, BlockDisplay.class, display -> {
			applyBeforeSpawning(display);
			display.setBlock(blockData);
		});
	}
	
	@Override
	public BlockDisplay spawn(@NonNull Location location) {
		return spawn(Nullity.nonNull(location.getWorld()), location);
	}
}
