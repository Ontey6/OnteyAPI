package com.ontey.api.plugin.color;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static com.ontey.api.plugin.color.MiniMessageColor.MINI_MESSAGE;

@NullMarked
public class LegacyColor {
   
   public static Component colorize(String input) {
      return MINI_MESSAGE.deserialize(replace(Objects.requireNonNull(input, "input")));
   }
   
   public static String replace(String input) {
      
      return Objects.requireNonNull(input)
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
