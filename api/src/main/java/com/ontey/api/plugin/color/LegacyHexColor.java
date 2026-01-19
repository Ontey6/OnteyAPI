package com.ontey.api.plugin.color;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static com.ontey.api.plugin.color.MiniMessageColor.MINI_MESSAGE;

@NullMarked
public class LegacyHexColor {
   
   public static Component colorize(String input) {
      return MINI_MESSAGE.deserialize(replace(Objects.requireNonNull(input, "input")));
   }
   
   public static String replace(String input) {
      return Objects.requireNonNull(input, "input")
        .replaceAll("(?<![&\\\\])(?<!&)&(/)?#([A-Fa-f0-9]{6})", "<$1#$2>")
        .replaceAll("(?<![&\\\\])(?<!§)§(/)?#([A-Fa-f0-9]{6})", "<$1#$2>")
        
        .replaceAll("[&\\\\]&(/)?#([A-Fa-f0-9]{6})", "&$1#$2")
        .replaceAll("[§\\\\]§(/)?#([A-Fa-f0-9]{6})", "§$1#$2");
   }
}
