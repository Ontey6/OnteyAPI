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
        
        .replaceAll(code('0'), "<black>")
        .replaceAll(code('1'), "<dark_blue>")
        .replaceAll(code('2'), "<dark_green>")
        .replaceAll(code('3'), "<dark_aqua>")
        .replaceAll(code('4'), "<dark_red>")
        .replaceAll(code('5'), "<dark_purple>")
        .replaceAll(code('6'), "<gold>")
        .replaceAll(code('7'), "<gray>")
        .replaceAll(code('8'), "<dark_gray>")
        .replaceAll(code('9'), "<blue>")
        .replaceAll(code('a'), "<green>")
        .replaceAll(code('b'), "<aqua>")
        .replaceAll(code('c'), "<red>")
        .replaceAll(code('d'), "<light_purple>")
        .replaceAll(code('e'), "<yellow>")
        .replaceAll(code('f'), "<white>")
        
        .replaceAll(code('k'), "<obfuscated>")
        .replaceAll(code('l'), "<bold>")
        .replaceAll(code('m'), "<strikethrough>")
        .replaceAll(code('n'), "<underlined>")
        .replaceAll(code('o'), "<italic>")
        .replaceAll(code('r'), "<reset>")
        
        .replaceAll("[&\\\\]&(/)?([0-9a-fk-or])", "&$1$2");
   }
   
   private static String code(char code) {
      return "(?<![&\\\\])&" + code;
   }
}
