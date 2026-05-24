package ontey.api.color;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static ontey.api.color.MiniMessageColor.MINI_MESSAGE;

/**
 * A coloring method that replaces Legacy HEX color codes like {@code &#00FFCC} with {@link MiniMessage} HEX color codes.
 *
 * @deprecated - Use {@link MiniMessageColor}. Legacy colors are not supported anymore.
 */

@Deprecated
public class LegacyHexColor {
	
	@NonNull
	public static Component colorize(@NonNull String input) {
		return MINI_MESSAGE.deserialize(replace(input));
	}
	
	@NonNull
	public static String replace(@NonNull String input) {
		return input
		  .replaceAll("(?<![&\\\\])(?<!&)&(/)?#([A-Fa-f0-9]{6})", "<$1#$2>")
		  .replaceAll("(?<![&\\\\])(?<!§)§(/)?#([A-Fa-f0-9]{6})", "<$1#$2>")
		  
		  .replaceAll("[&\\\\]&(/)?#([A-Fa-f0-9]{6})", "&$1#$2")
		  .replaceAll("[§\\\\]§(/)?#([A-Fa-f0-9]{6})", "§$1#$2");
	}
}
