package ontey.color;

import lombok.NonNull;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ontey.regex.RegexString;
import net.kyori.adventure.text.Component;

import java.util.List;

import static ontey.color.MiniMessageColor.MINI_MESSAGE;

/**
 * A coloring method that replaces RGB tag color codes like {@code &a} with {@link MiniMessage} HEX color codes.
 */

public class RgbTagColor {
   
   @NonNull
   public static Component colorize(@NonNull String input) {
      return MINI_MESSAGE.deserialize(replace(input));
   }
   
   @NonNull
   public static String replace(@NonNull String input) {
      return new RegexString(input)
        .replaceAll(
          "(?<!\\\\)<(\\s)?(/)?(\\s)?(\\d{1,3})[-,](\\s)?(\\d{1,3})[-,](\\s)?(\\d{1,3})[-,](\\s)?>",
          List.of("$2", "$4", "$6", "$8"),
          rgb -> {
             String escapable = rgb[0];
             int r = Integer.parseInt(rgb[1]), g = Integer.parseInt(rgb[2]), b = Integer.parseInt(rgb[3]);
             System.out.println(escapable);
             return String.format("<%s#%02x%02x%02x>", escapable, r, g, b);
          }
        )
        .replaceAll("\\\\(<(\\s)?(/)?(\\s)?(\\d{1,3})[-,](\\s)?(\\d{1,3})[-,](\\s)?(\\d{1,3})(\\s)?>)", "$1");
   }
}
