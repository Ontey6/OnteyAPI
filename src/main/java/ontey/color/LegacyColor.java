package ontey.color;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static ontey.color.MiniMessageColor.MINI_MESSAGE;

/**
 * A coloring method that replaces Section and Ampersand color codes like {@code &a} with {@link MiniMessage} color codes.
 */

public class LegacyColor {
   
   @NonNull
   public static Component colorize(@NonNull String input) {
      return MINI_MESSAGE.deserialize(replace(input));
   }
   
   @NonNull
   public static String replace(@NonNull String input) {
      
      return input
        // Legacy
        .replaceAll("§([0-9a-fk-or])", "&$1") // § to &
        .replaceAll("(?<![&\\\\])&0", "<black>")
        .replaceAll("(?<![&\\\\])&1", "<dark_blue>")
        .replaceAll("(?<![&\\\\])&2", "<dark_green>")
        .replaceAll("(?<![&\\\\])&3", "<dark_aqua>")
        .replaceAll("(?<![&\\\\])&4", "<dark_red>")
        .replaceAll("(?<![&\\\\])&5", "<dark_purple>")
        .replaceAll("(?<![&\\\\])&6", "<gold>")
        .replaceAll("(?<![&\\\\])&7", "<gray>")
        .replaceAll("(?<![&\\\\])&8", "<dark_gray>")
        .replaceAll("(?<![&\\\\])&9", "<blue>")
        .replaceAll("(?<![&\\\\])&a", "<green>")
        .replaceAll("(?<![&\\\\])&b", "<aqua>")
        .replaceAll("(?<![&\\\\])&c", "<red>")
        .replaceAll("(?<![&\\\\])&d", "<light_purple>")
        .replaceAll("(?<![&\\\\])&e", "<yellow>")
        .replaceAll("(?<![&\\\\])&f", "<white>")
        
        .replaceAll("(?<![&\\\\])&k", "<obfuscated>")
        .replaceAll("(?<![&\\\\])&l", "<bold>")
        .replaceAll("(?<![&\\\\])&m", "<strikethrough>")
        .replaceAll("(?<![&\\\\])&n", "<underlined>")
        .replaceAll("(?<![&\\\\])&o", "<italic>")
        .replaceAll("(?<![&\\\\])&r", "<reset>")
        
        .replaceAll("[&\\\\]&(/)?([0-9a-fk-or])", "&$1$2");
   }
}
