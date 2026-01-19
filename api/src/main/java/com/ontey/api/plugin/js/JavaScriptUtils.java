package com.ontey.api.plugin.js;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public class JavaScriptUtils {
   
   public static Map<String, Object> getStatics() {
      
      return new HashMap<>(Map.of(
        "GameMode", new JSGameMode(),
        "Gamemode",  new JSGameMode(),
        "Component", new JSComponent(),
        "NamedTextColor", new JSNamedTextColor(),
        "TextColor", new JSTextColor()
      ));
   }
   
   // TODO unfinished
   public static Map<String, Object> getAll(Player player) {
      Map<String, Object> out = new HashMap<>();
      
      out.putAll(JavaScriptUtils.getStatics());
      
      return out;
   }
   
   public static class JSGameMode {
      
      private JSGameMode() { }
      
      public GameMode SURVIVAL = GameMode.SURVIVAL;
      public GameMode CREATIVE = GameMode.CREATIVE;
      public GameMode ADVENTURE = GameMode.ADVENTURE;
      public GameMode SPECTATOR = GameMode.SPECTATOR;
      
      public @Nullable GameMode of(String identifier) {
         return switch(identifier) {
            case "survival", "s", "0" -> SURVIVAL;
            case "creative", "c", "1" -> CREATIVE;
            case "adventure", "a", "2" -> ADVENTURE;
            case "spectator", "sp", "spec", "3" -> SPECTATOR;
            default -> null;
         };
      }
      
      public @Nullable GameMode of(int identifier) {
         return GameMode.getByValue(identifier);
      }
   }
   
   public static class JSComponent {
      
      private JSComponent() { }
      
      public final Component EMPTY = Component.empty();
      
      public Component text(String text) {
         return Component.text(text);
      }
      
      public Component text(String text, TextColor color) {
         return Component.text(text, color);
      }
   }
   
   public static class JSNamedTextColor {
      
      private JSNamedTextColor() { }
      
      public final NamedTextColor BLACK = NamedTextColor.BLACK;
      public final NamedTextColor DARK_BLUE = NamedTextColor.DARK_BLUE;
      public final NamedTextColor DARK_GREEN = NamedTextColor.DARK_GREEN;
      public final NamedTextColor DARK_AQUA = NamedTextColor.DARK_AQUA;
      public final NamedTextColor DARK_RED = NamedTextColor.DARK_RED;
      public final NamedTextColor DARK_PURPLE = NamedTextColor.DARK_PURPLE;
      public final NamedTextColor GOLD = NamedTextColor.GOLD;
      public final NamedTextColor GRAY = NamedTextColor.GRAY;
      public final NamedTextColor DARK_GRAY = NamedTextColor.DARK_GRAY;
      public final NamedTextColor BLUE = NamedTextColor.BLUE;
      public final NamedTextColor GREEN = NamedTextColor.GREEN;
      public final NamedTextColor AQUA = NamedTextColor.AQUA;
      public final NamedTextColor RED = NamedTextColor.RED;
      public final NamedTextColor LIGHT_PURPLE = NamedTextColor.LIGHT_PURPLE;
      public final NamedTextColor YELLOW = NamedTextColor.YELLOW;
      public final NamedTextColor WHITE = NamedTextColor.WHITE;
   }
   
   public static class JSTextColor extends JSNamedTextColor {
      
      private JSTextColor() { }
      
      public TextColor of(int r, int g, int b) {
         return TextColor.color(r, g, b);
      }
   }
}
