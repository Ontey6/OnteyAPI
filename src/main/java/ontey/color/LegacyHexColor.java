package ontey.color;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Objects;

import static ontey.color.MiniMessageColor.MINI_MESSAGE;

/**
 * A coloring method that replaces Legacy HEX color codes like {@code &#00FFCC} with {@link MiniMessage} HEX color codes.
 */

public class LegacyHexColor {
   
   @NonNull
   public static Component colorize(@NonNull String input) {
      return MINI_MESSAGE.deserialize(replace(Objects.requireNonNull(input, "input")));
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
