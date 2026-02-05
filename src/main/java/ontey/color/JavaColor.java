package ontey.color;

import ontey.regex.RegexString;
import lombok.NonNull;

import java.util.List;

/**
 * A static factory class for ANSI colors and {@link JavaColor#colorize(String) colorizing} text.
 */

@SuppressWarnings("DeprecatedIsStillUsed")
public class JavaColor {
   
   // Presets
   
   /**
    * A Preset Color
    */
   
   public static final @NonNull String
     BLACK         = of(0, 0, 0),
     DARK_BLUE     = of(0, 0, 170),
     DARK_GREEN    = of(0, 170, 0),
     DARK_AQUA     = of(0, 170, 170),
     DARK_RED      = of(170, 0, 0),
     DARK_PURPLE   = of(170, 0, 170),
     GOLD          = of(255, 170, 0),
     GRAY          = of(170, 170, 170),
     DARK_GRAY     = of(85, 85, 85),
     BLUE          = of(85, 85, 255),
     GREEN         = of(85, 255, 85),
     AQUA          = of(85, 255, 255),
     RED           = of(255, 85, 85),
     LIGHT_PURPLE  = of(255, 85, 255),
     YELLOW        = of(255, 255, 85),
     WHITE         = of(255, 255, 255);
   
   /**
    * Strips all color and formatting.
    */
   
   public static final @NonNull String RESET = preset(0);
   
   /**
    * Makes text bold.
    */
   
   public static final @NonNull String BOLD = preset(1);
   
   /**
    * Dims text. Usually lower brightness or lighter color.
    * @deprecated because it only rarely works - many terminals ignore it
    */
   
   @Deprecated
   public static final @NonNull String DIM = preset(2);
   
   /**
    * Italicizes text; makes the upper half shift towards right.
    */
   
   public static final @NonNull String ITALIC = preset(3);
   
   /**
    * Underlines text.
    */
   
   public static final @NonNull String UNDERLINE = preset(4);
   
   /**
    * makes text blink (dis- and reappear in a certain interval)
    * @deprecated because it only rarely works - many terminals ignore it
    */
   
   @Deprecated
   public static final @NonNull String BLINK = preset(5);
   
   /**
    * Inverts fore- and background colors
    */
   
   public static final @NonNull String REVERSE = preset(7);
   
   /**
    * Inverts fore- and background colors
    */
   
   public static final @NonNull String INVERT = REVERSE;
   
   /**
    * Hides text. Usually makes it invisible, with it still being selectable.
    * @deprecated because it only rarely works - many terminals ignore it
    */
   
   @Deprecated
   public static final @NonNull String HIDDEN = preset(8);
   
   /**
    * Strikes through / crosses out the text.
    * <p>
    * Might be unsupported in some terminals.
    */
   
   public static final @NonNull String STRIKETHROUGH = preset(9);
   
   private static final int
     MIN_RGB = 0,
     MAX_RGB = 255;
   
   // Static factory
   private JavaColor() { }
   
   /**
    * Creates a foreground color with given {@code r}, {@code g} and {@code b} values.
    */
   
   @NonNull
   public static String of(int r, int g, int b) {
      return foreground(r, g, b);
   }
   
   /**
    * Creates a foreground color with given {@code r}, {@code g} and {@code b} values.
    */
   
   @NonNull
   public static String foreground(int r, int g, int b) {
      return asString(r, g, b);
   }
   
   /**
    * Creates a background color with given {@code r}, {@code g} and {@code b} values.
    */
   
   @NonNull
   public static String background(int r, int g, int b) {
      return asString(r, g, b);
   }
   
   /**
    * Creates a preset ANSI color escape code with the {@code identifier}. <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#Colors">ANSI-escape-codes</a>
    */
   
   @NonNull
   public static String preset(int identifier) {
      return String.format("\033[%sm", identifier);
   }
   
   @NonNull
   private static String asString(int r, int g, int b) {
      return String.format("\033[38;2;%s;%s;%sm", r, g, b);
   }
   
   /**
    * @return A colorized {@link String} that has the following patterns replaced by <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#Colors">ANSI-escape-codes</a>.
    * Ignores RGB tags with out-of-range RGB values
    * <ul>
    *    <li>Minecraft Legacy codes (&c, §c)</li>
    *    <li>RGB tags (<255,0,0>)</li>
    *    <li>Named color tags (<red>)</li>
    * </ul>
    */
   
   @NonNull
   public static String colorize(@NonNull String str) {
      return colorize(str, false);
   }
   
   /**
    * @param clampRGB Whether to clamp out-of-range RGB values using {@link JavaColor#clampRGB(int) clampRGB()}. If false, just ignores the RGB tag
    * @return A colorized {@link String} that has the following patterns replaced by <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#Colors">ANSI-escape-codes</a>.
    * <ul>
    *    <li>{@linkplain JavaColor#replaceLegacy(String) Minecraft Legacy codes (&c, §c)}</li>
    *    <li>{@linkplain JavaColor#replaceRGB(String, boolean) RGB tags (<255,0,0>)}</li>
    *    <li>{@linkplain JavaColor#replaceTags(String) Named color tags (<red>)}</li>
    * </ul>
    */
   
   @NonNull
   public static String colorize(@NonNull String str, boolean clampRGB) {
      if(str.isEmpty())
         return str; // return empty
      
      // Minecraft &-codes
      str = replaceLegacy(str);
      
      // RGB tags like <255,0,0>
      str = replaceRGB(str, clampRGB);
      
      // Named mini-message tags like <red>,
      str = replaceTags(str);
      
      return str;
   }
   
   /**
    * Replaces Legacy color codes like {@code &c} or {@code §c} with ANSI colors.
    */
   
   @NonNull
   public static String replaceLegacy(@NonNull String str) {
      return new RegexString(str).replaceAll("([&§])([0-9A-FK-ORa-fk-or])", "$2", grp -> {
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
   }
   
   /**
    * Replaces RGB tags like {@code <255,0,0>} with ANSI colors.
    * Ignores RGB tags with out-of-range RGB values
    */
   
   @NonNull
   public static String replaceRGB(@NonNull String str) {
      return replaceRGB(str, false);
   }
   
   /**
    * Replaces RGB tags like {@code <255,0,0>} with ANSI colors.
    * @param clampRGB Whether to clamp out-of-range RGB values using {@link JavaColor#clampRGB(int) clampRGB()}. If false, just ignores the RGB tag.
    */
   
   @NonNull
   public static String replaceRGB(@NonNull String str, boolean clampRGB) {
      return new RegexString(str).replaceAll("<(\\d{1,3}),(\\d{1,3}),(\\d{1,3})>", List.of("$1", "$2", "$3"), parts -> {
         int r = Integer.parseInt(parts[0]);
         int g = Integer.parseInt(parts[1]);
         int b = Integer.parseInt(parts[2]);
         
         if(!checkRGB(r, g, b)) {
            if(!clampRGB)
               return String.format("<%s,%s,%s>", r, g, b);
            
            r = clampRGB(r);
            g = clampRGB(g);
            b = clampRGB(b);
         }
         
         return of(r, g, b);
      });
   }
   
   /**
    * Replaces named tags like {@code <red>} with ANSI colors.
    */
   
   @NonNull
   public static String replaceTags(@NonNull String str) {
      return new RegexString(str).replaceAll("<([a-zA-Z_]+)>", "$1", grp ->
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
           case "reverse", "invert" -> REVERSE;
           case "hidden" -> HIDDEN;
           case "strikethrough", "s" -> STRIKETHROUGH;
           default -> "<" + grp + ">";
        });
   }
   
   /**
    * @return Whether {@code value} is in the RGB range (0-255).
    */
   
   private static boolean checkIndex(int value) {
      return value > MIN_RGB && value < MAX_RGB;
   }
   
   /**
    * @return Whether {@code r}, {@code g} and {@code b} all fulfill {@link JavaColor#checkIndex(int) checkIndex()}.
    */
   
   private static boolean checkRGB(int r, int g, int b) {
      return checkIndex(r) && checkIndex(g) && checkIndex(b);
   }
   
   /**
    * Clamps an RGB value to the min/max RGB values.
    * <p>
    * Examples:
    * <p>
    * {@code clamp(256)} --> {@code 255} (clamp)
    * <p>
    * {@code clamp(-1} --> {@code 0} (clamp)
    * <p>
    * {@code clamp(255)} --> {@code 255} (no clamp)
    */
   
   private static int clampRGB(int value) {
      return Math.max(MIN_RGB, Math.min(MAX_RGB, value));
   }
}