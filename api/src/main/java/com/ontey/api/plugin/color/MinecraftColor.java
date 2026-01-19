package com.ontey.api.plugin.color;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class MinecraftColor {
   
   public static Component colorize(String input) {
      return MiniMessageColor.colorize(replace(input));
   }
   
   public static String replace(String input) {
      Objects.requireNonNull(input, "input");
      
      input = LegacyColor.replace(input);
      input = LegacyHexColor.replace(input);
      input = RgbTagColor.replace(input);
      
      return input;
   }
}
