package ontey.color;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * A coloring method that replaces color codes like from {@link LegacyColor}, {@link LegacyHexColor},
 * {@link MiniMessageColor} and {@link RgbTagColor} with {@link MiniMessage} color codes.
 */

public class MinecraftColor {
   
   @NonNull
   public static Component colorize(@NonNull String input) {
      return MiniMessageColor.colorize(replace(input));
   }
   
   @NonNull
   public static String replace(@NonNull String input) {
      input = LegacyColor.replace(input);
      input = LegacyHexColor.replace(input);
      input = RgbTagColor.replace(input);
      
      return input;
   }
}
