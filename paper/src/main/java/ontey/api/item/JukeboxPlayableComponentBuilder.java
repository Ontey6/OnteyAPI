package ontey.api.item;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.JukeboxSong;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;

public class JukeboxPlayableComponentBuilder {
	
	@NonNull
	@Getter
	private final JukeboxPlayableComponent component;
	
	protected JukeboxPlayableComponentBuilder(@NonNull ItemMeta meta) {
		this.component = meta.getJukeboxPlayable();
	}
	
	public JukeboxPlayableComponentBuilder song(@NonNull JukeboxSong song) {
		component.setSong(song);
		return this;
	}
	
	public JukeboxPlayableComponentBuilder song(@NonNull NamespacedKey key) {
		component.setSongKey(key);
		return this;
	}
	
	public JukeboxPlayableComponentBuilder showSong(boolean flag) {
		component.setShowInTooltip(flag);
		return this;
	}
	
	public JukeboxPlayableComponentBuilder showSong() {
		return showSong(true);
	}
	
	public JukeboxPlayableComponentBuilder hideSong() {
		return showSong(false);
	}
}
