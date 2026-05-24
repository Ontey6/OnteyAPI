package ontey.api.item;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ToolComponentBuilder {
	
	@NonNull
	@Getter
	private final ToolComponent component;
	
	protected ToolComponentBuilder(@NonNull ItemMeta meta) {
		this.component = meta.getTool();
	}
	
	public ToolComponentBuilder defaultMiningSpeed(float speed) {
		component.setDefaultMiningSpeed(speed);
		return this;
	}
	
	public ToolComponentBuilder damagePerBlock(int damage) {
		component.setDamagePerBlock(damage);
		return this;
	}
	
	public ToolComponentBuilder rule(@NonNull Material block, @Nullable Float speed, @Nullable Boolean correctForDrops) {
		component.addRule(block, speed, correctForDrops);
		return this;
	}
	
	public ToolComponentBuilder rule(@NonNull Tag<Material> tag, @Nullable Float speed, @Nullable Boolean correctForDrops) {
		component.addRule(tag, speed, correctForDrops);
		return this;
	}
	
	public ToolComponentBuilder rule(@NonNull Collection<@NonNull Material> blocks, @Nullable Float speed, @Nullable Boolean correctForDrops) {
		component.addRule(blocks, speed, correctForDrops);
		return this;
	}
}
