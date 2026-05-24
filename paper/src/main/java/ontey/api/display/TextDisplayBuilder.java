package ontey.api.display;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import ontey.api.check.Nullity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;

public class TextDisplayBuilder extends DisplayBuilder<TextDisplayBuilder> {
	
	@NonNull
	private final Component text;
	
	private int lineWidth = 200;
	
	private Color backgroundColor;
	
	private byte textOpacity = -1;
	
	private boolean shadowed = false;
	
	private boolean seeThrough = false;
	
	private boolean defaultBackground = false;
	
	private TextDisplay.TextAlignment textAlignment = TextDisplay.TextAlignment.CENTER;
	
	TextDisplayBuilder(@NonNull Component text) {
		this.text = text;
	}
	
	@Override
	protected TextDisplayBuilder getThis() {
		return this;
	}
	
	/**
	 * Sets the line width.
	 */
	public TextDisplayBuilder lineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}
	
	/**
	 * Sets the background color.
	 */
	public TextDisplayBuilder backgroundColor(@Nullable Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	
	/**
	 * Sets the text opacity.
	 */
	public TextDisplayBuilder textOpacity(byte textOpacity) {
		this.textOpacity = textOpacity;
		return this;
	}
	
	/**
	 * Sets whether the text is shadowed.
	 */
	public TextDisplayBuilder shadowed(boolean shadowed) {
		this.shadowed = shadowed;
		return this;
	}
	
	/**
	 * Sets whether the text is see-through.
	 */
	public TextDisplayBuilder seeThrough(boolean seeThrough) {
		this.seeThrough = seeThrough;
		return this;
	}
	
	/**
	 * Sets whether to use the default background.
	 */
	public TextDisplayBuilder defaultBackground(boolean defaultBackground) {
		this.defaultBackground = defaultBackground;
		return this;
	}
	
	/**
	 * Sets the text alignment.
	 */
	public TextDisplayBuilder textAlignment(TextDisplay.TextAlignment textAlignment) {
		this.textAlignment = textAlignment;
		return this;
	}
	
	public TextDisplay spawn(@NonNull World world, @NonNull Location coordinates) {
		return world.spawn(coordinates, TextDisplay.class, display -> {
			applyBeforeSpawning(display);
			display.text(text);
			display.setLineWidth(lineWidth);
			display.setBackgroundColor(backgroundColor);
			display.setTextOpacity(textOpacity);
			display.setShadowed(shadowed);
			display.setSeeThrough(seeThrough);
			display.setDefaultBackground(defaultBackground);
			display.setAlignment(textAlignment);
		});
	}
	
	public TextDisplay spawn(@NonNull Location loc) {
		return spawn(Nullity.nonNull(loc.getWorld()), loc);
	}
}
