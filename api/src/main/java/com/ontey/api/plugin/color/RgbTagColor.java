package com.ontey.api.plugin.color;

import com.ontey.api.java.regex.RegexString;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Objects;

import static com.ontey.api.plugin.color.MiniMessageColor.MINI_MESSAGE;

@NullMarked
public class RgbTagColor {
   
   public static Component colorize(String input) {
      return MINI_MESSAGE.deserialize(replace(Objects.requireNonNull(input, "input")));
   }
   
   public static String replace(String input) {
      return new RegexString(Objects.requireNonNull(input, "input"))
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
