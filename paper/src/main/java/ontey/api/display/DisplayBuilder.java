package ontey.api.display;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public abstract class DisplayBuilder<T extends DisplayBuilder<T>> {
	
	@Nullable
	private Transformation transformation;
	
	@Nullable
	private Matrix4f transformationMatrix;
	
	private int interpolationDuration = 0;
	
	private int teleportDuration = 0;
	
	private float viewRange = 1.0f;
	
	private float shadowRadius = 0.0f;
	
	private float shadowStrength = 1.0f;
	
	private float displayWidth = 0.0f;
	
	private float displayHeight = 0.0f;
	
	private int interpolationDelay = 0;
	
	@Nullable
	private Display.Billboard billboard = Display.Billboard.FIXED;
	
	@Nullable
	private Color glowColorOverride;
	
	@Nullable
	private Display.Brightness brightness;
	
	public static TextDisplayBuilder text(Component text) {
		return new TextDisplayBuilder(text);
	}
	
	public static TextDisplayBuilder text(String str) {
		return text(Component.text(str));
	}
	
	public static BlockDisplayBuilder block(BlockData data) {
		return new BlockDisplayBuilder(data);
	}
	
	public static BlockDisplayBuilder block(Material material) {
		return block(Bukkit.createBlockData(material));
	}
	
	public static ItemDisplayBuilder item(ItemStack item) {
		return new ItemDisplayBuilder(item);
	}
	
	public static ItemDisplayBuilder item(Material material) {
		return item(new ItemStack(material));
	}
	
	protected abstract T getThis();
	
	/**
	 * Sets the transformation.
	 */
	public T transformation(@Nullable Transformation transformation) {
		this.transformation = transformation;
		return getThis();
	}
	
	/**
	 * Sets the transformation matrix.
	 */
	public T transformationMatrix(@Nullable Matrix4f transformationMatrix) {
		this.transformationMatrix = transformationMatrix;
		return getThis();
	}
	
	/**
	 * Sets the interpolation duration.
	 */
	public T interpolationDuration(int interpolationDuration) {
		this.interpolationDuration = interpolationDuration;
		return getThis();
	}
	
	/**
	 * Sets the teleport duration.
	 */
	public T teleportDuration(int teleportDuration) {
		this.teleportDuration = teleportDuration;
		return getThis();
	}
	
	/**
	 * Sets the view range.
	 */
	public T viewRange(float viewRange) {
		this.viewRange = viewRange;
		return getThis();
	}
	
	/**
	 * Sets the shadow radius.
	 */
	public T shadowRadius(float shadowRadius) {
		this.shadowRadius = shadowRadius;
		return getThis();
	}
	
	/**
	 * Sets the shadow strength.
	 */
	public T shadowStrength(float shadowStrength) {
		this.shadowStrength = shadowStrength;
		return getThis();
	}
	
	/**
	 * Sets the display width.
	 */
	public T displayWidth(float displayWidth) {
		this.displayWidth = displayWidth;
		return getThis();
	}
	
	/**
	 * Sets the display height.
	 */
	public T displayHeight(float displayHeight) {
		this.displayHeight = displayHeight;
		return getThis();
	}
	
	/**
	 * Sets the interpolation delay.
	 */
	public T interpolationDelay(int interpolationDelay) {
		this.interpolationDelay = interpolationDelay;
		return getThis();
	}
	
	/**
	 * Sets the billboard.
	 */
	public T billboard(@Nullable Display.Billboard billboard) {
		this.billboard = billboard;
		return getThis();
	}
	
	/**
	 * Sets the glow color override.
	 */
	public T glowColorOverride(@Nullable Color glowColorOverride) {
		this.glowColorOverride = glowColorOverride;
		return getThis();
	}
	
	/**
	 * Sets the brightness.
	 */
	public T brightness(@Nullable Display.Brightness brightness) {
		this.brightness = brightness;
		return getThis();
	}
	
	/**
	 * Spawns the display in the {@code world} at the {@code coordinates}.
	 * Ignores the {@code coordinates}' world.
	 *
	 * @return The spawned display.
	 */
	public abstract Display spawn(@NonNull World world, @NonNull Location coordinates);
	
	/**
	 * Spawns the display in the {@code location}'s world at the {@code location}'s coordinates.
	 *
	 * @return The spawned display.
	 */
	public abstract Display spawn(@NonNull Location location);
	
	protected void applyBeforeSpawning(Display display) {
		if(transformation != null)
			display.setTransformation(transformation);
		
		if(transformationMatrix != null)
			display.setTransformationMatrix(transformationMatrix);
		
		display.setInterpolationDuration(interpolationDuration);
		display.setTeleportDuration(teleportDuration);
		display.setViewRange(viewRange);
		display.setShadowRadius(shadowRadius);
		display.setShadowStrength(shadowStrength);
		display.setDisplayWidth(displayWidth);
		display.setDisplayHeight(displayHeight);
		display.setInterpolationDelay(interpolationDelay);
		
		if(billboard != null)
			display.setBillboard(billboard);
		
		display.setGlowColorOverride(glowColorOverride);
		display.setBrightness(brightness);
	}
}
