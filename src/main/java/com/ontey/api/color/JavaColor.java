package com.ontey.api.color;

import com.ontey.api.regex.RegexString;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JavaColor {
   
   // Presets
   
   public static final String
     BLACK        = direct(0, 0, 0),
     DARK_BLUE    = direct(0, 0, 170),
     DARK_GREEN   = direct(0, 170, 0),
     DARK_AQUA    = direct(0, 170, 170),
     DARK_RED     = direct(170, 0, 0),
     DARK_PURPLE  = direct(170, 0, 170),
     GOLD         = direct(255, 170, 0),
     GRAY         = direct(170, 170, 170),
     DARK_GRAY    = direct(85, 85, 85),
     BLUE         = direct(85, 85, 255),
     GREEN        = direct(85, 255, 85),
     AQUA         = direct(85, 255, 255),
     RED          = direct(255, 85, 85),
     LIGHT_PURPLE = direct(255, 85, 255),
     YELLOW       = direct(255, 255, 85),
     WHITE        = direct(255, 255, 255),
   
     RESET         = "\033[0m",
     BOLD          = "\033[1m",
     DIM           = "\033[2m",
     ITALIC        = "\033[3m",
     UNDERLINE     = "\033[4m",
     BLINK         = "\033[5m",
     REVERSE       = "\033[7m",
     HIDDEN        = "\033[8m",
     STRIKETHROUGH = "\033[9m";
   
   private final int r, g, b;
   
   public static String direct(int r, int g, int b) {
      return of(r, g, b);
   }
   
   public static String of(int r, int g, int b) {
      return foreground(r, g, b);
   }
   
   public static String foreground(int r, int g, int b) {
      return new JavaColor(r, g, b).toString();
   }
   
   public static String background(int r, int g, int b) {
      return new JavaColor(r, g, b).toString();
   }
   
   public String toString() {
      return String.format("\033[38;2;%s;%s;%sm", r, g, b);
   }
   
   public static String colorize(String str) {
      if(str == null || str.isEmpty())
         return str;
      
      RegexString rs = new RegexString(str);
      
      // 1) Minecraft &-codes
      str = rs.replaceAll("&([0-9A-FK-ORa-fk-or])", "$1", grp -> {
         char c = grp.charAt(0);
         return switch(Character.toLowerCase(c)) {
            case '0' -> BLACK;
            case '1' -> DARK_BLUE;
            case '2' -> DARK_GREEN;
            case '3' -> DARK_AQUA;
            case '4' -> DARK_RED;
            case '5' -> DARK_PURPLE;
            case '6' -> GOLD;
            case '7' -> GRAY;
            case '8' -> DARK_GRAY;
            case '9' -> BLUE;
            case 'a' -> GREEN;
            case 'b' -> AQUA;
            case 'c' -> RED;
            case 'd' -> LIGHT_PURPLE;
            case 'e' -> YELLOW;
            case 'f' -> WHITE;
            case 'k' -> HIDDEN; // obfuscated -> map to hidden
            case 'l' -> BOLD;
            case 'm' -> STRIKETHROUGH;
            case 'n' -> UNDERLINE;
            case 'o' -> ITALIC;
            case 'r' -> RESET;
            default -> "&" + grp;
         };
      });
      
      // 2) RGB tags like <255,0,0>
      str = new RegexString(str).replaceAll("<(\\d{1,3}),(\\d{1,3}),(\\d{1,3})>", List.of("$1", "$2", "$3"), parts -> {
         int rr = Integer.parseInt(parts[0]);
         int gg = Integer.parseInt(parts[1]);
         int bb = Integer.parseInt(parts[2]);
         // clamp to valid range
         rr = Math.max(0, Math.min(255, rr));
         gg = Math.max(0, Math.min(255, gg));
         bb = Math.max(0, Math.min(255, bb));
         return JavaColor.direct(rr, gg, bb);
      });
      
      // 3) Named mini-message tags like <red>, <bold>, etc.
      str = new RegexString(str).replaceAll("<([a-zA-Z_]+)>", "$1", grp ->
        switch(grp.toLowerCase()) {
           case "black" -> BLACK;
           case "dark_blue" -> DARK_BLUE;
           case "dark_green" -> DARK_GREEN;
           case "dark_aqua" -> DARK_AQUA;
           case "dark_red" -> DARK_RED;
           case "dark_purple" -> DARK_PURPLE;
           case "gold" -> GOLD;
           case "gray" -> GRAY;
           case "dark_gray" -> DARK_GRAY;
           case "blue" -> BLUE;
           case "green" -> GREEN;
           case "aqua" -> AQUA;
           case "red" -> RED;
           case "light_purple" -> LIGHT_PURPLE;
           case "yellow" -> YELLOW;
           case "white" -> WHITE;
           
           case "reset", "r" -> RESET;
           case "bold", "b" -> BOLD;
           case "dim" -> DIM;
           case "italic", "i" -> ITALIC;
           case "underline", "u" -> UNDERLINE;
           case "blink" -> BLINK;
           case "reverse" -> REVERSE;
           case "hidden" -> HIDDEN;
           case "strikethrough", "s" -> STRIKETHROUGH;
           default -> "<" + grp + ">";
        });
      
      return str;
   }
   
}