package com.ontey.api.color;

import com.ontey.api.regex.RegexString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;

import static net.kyori.adventure.text.event.ClickEvent.*;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static net.kyori.adventure.text.minimessage.tag.Tag.styling;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

public final class MiniMessageColor {
   
   public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
   
   public static Component colorize(String msg) {
      final String ERR = "Value Expected";
      
      TagResolver
        cmd = resolver("cmd", (args, ctx) -> styling(runCommand(args.popOr(ERR).value()))),
        
        suggest = resolver("suggest", (args, ctx) -> styling(ClickEvent.suggestCommand(args.popOr(ERR).value()))),
        
        copy = resolver("copy", (args, ctx) -> styling(copyToClipboard(args.popOr(ERR).value()))),
        
        url = resolver("url", (args, ctx) -> styling(openUrl(args.popOr(ERR).value()))),
        
        uurl = resolver("uurl", (args, ctx) -> styling(UNDERLINED, openUrl(args.popOr(ERR).value()))),
        
        // Combined resolvers
        resolvers = resolver(cmd, suggest, copy, url, uurl);
      
      MiniMessage.miniMessage().deserializeToTree("");
      
      return MINI_MESSAGE.deserialize(msg, resolvers);
   }
   
   public static String replace(String str) {
      if(str == null)
         return "";
      
      return str
        // Legacy
        .replaceAll("§([0-9a-fk-or])", "&$1")
        .replaceAll("(?<![&\\\\])&(/)?0", "<$1black>")
        .replaceAll("(?<![&\\\\])&(/)?1", "<$1dark_blue>")
        .replaceAll("(?<![&\\\\])&(/)?2", "<$1dark_green>")
        .replaceAll("(?<![&\\\\])&(/)?3", "<$1dark_aqua>")
        .replaceAll("(?<![&\\\\])&(/)?4", "<$1dark_red>")
        .replaceAll("(?<![&\\\\])&(/)?5", "<$1dark_purple>")
        .replaceAll("(?<![&\\\\])&(/)?6", "<$1gold>")
        .replaceAll("(?<![&\\\\])&(/)?7", "<$1gray>")
        .replaceAll("(?<![&\\\\])&(/)?8", "<$1dark_gray>")
        .replaceAll("(?<![&\\\\])&(/)?9", "<$1blue>")
        .replaceAll("(?<![&\\\\])&(/)?a", "<$1green>")
        .replaceAll("(?<![&\\\\])&(/)?b", "<$1aqua>")
        .replaceAll("(?<![&\\\\])&(/)?c", "<$1red>")
        .replaceAll("(?<![&\\\\])&(/)?d", "<$1light_purple>")
        .replaceAll("(?<![&\\\\])&(/)?e", "<$1yellow>")
        .replaceAll("(?<![&\\\\])&(/)?f", "<$1white>")
        
        .replaceAll("(?<![&\\\\])&(/)?k", "<$1obfuscated>")
        .replaceAll("(?<![&\\\\])&(/)?l", "<$1bold>")
        .replaceAll("(?<![&\\\\])&(/)?m", "<$1strikethrough>")
        .replaceAll("(?<![&\\\\])&(/)?n", "<$1underlined>")
        .replaceAll("(?<![&\\\\])&(/)?o", "<$1italic>")
        .replaceAll("(?<![&\\\\])&(/)?r", "<$1reset>")
        
        // Legacy Hex
        .replaceAll("(?<![&\\\\])(?<!&)&(/)?#([A-Fa-f0-9]{6})", "<$1#$2>")
        .replaceAll("(?<![&\\\\])(?<!§)§(/)?#([A-Fa-f0-9]{6})", "<$1#$2>")
        
        /*
        // Mini-Message abbreviations
        .replaceAll("(?<!\\\\)<cmd:([\"'])([^>]+)\\1>", "<click:run_command:$1$2$1>")
        .replaceAll("(?<!\\\\)<suggest:([\"'])([^>]+)\\1>", "<click:suggest_command:$1$2$1>")
        .replaceAll("(?<!\\\\)<copy:([\"'])([^>]+)\\1>", "<click:copy_to_clipboard:$1$2$1>")
        .replaceAll("(?<!\\\\)<url:([\"'])([^>]+)\\1>", "<click:open_url:$1$2$1>")
        .replaceAll("(?<!\\\\)<uurl:([\"'])([^>]+)\\1>", "<u><click:open_url:$1$2$1>")
        
        .replaceAll("(?<!\\\\)<cmd:([^>\\s]+)>", "<click:run_command:$1>")
        .replaceAll("(?<!\\\\)<suggest:([^>\\s]+)>", "<click:suggest_command:$1>")
        .replaceAll("(?<!\\\\)<copy:([^>\\s]+)>", "<click:copy_to_clipboard:$1>")
        .replaceAll("(?<!\\\\)<url:([^>\\s]+)>", "<click:open_url:$1>")
        .replaceAll("(?<!\\\\)<uurl:([^>\\s]+)>", "<u><click:open_url:$1>")
        
        .replaceAll("(?<!\\\\)</cmd>", "</click:run_command>")
        .replaceAll("(?<!\\\\)</suggest>", "</click:suggest_command>")
        .replaceAll("(?<!\\\\)</copy>", "</click:copy_to_clipboard>")
        .replaceAll("(?<!\\\\)</url>", "</click:open_url>")
        .replaceAll("(?<!\\\\)</uurl>", "</click:open_url></u>")
         */
        
        // Escaped
        .replaceAll("[&\\\\]&([0-9a-fk-or])", "&$1")
        .replaceAll("[&\\\\]&/([0-9a-fk-or])", "&/$1")
        
        .replaceAll("[&\\\\]&(/)?#([A-Fa-f0-9]{6})", "&$1#$2")
        .replaceAll("[§\\\\]§(/)?#([A-Fa-f0-9]{6})", "§$1#$2");
        
        /*
        .replaceAll("\\\\<(cmd|suggest|copy|url|uurl)([+:;,=-])?([\"'])([^>]+)\\1>", "<$1$2$3$4$3>")
        .replaceAll("\\\\<(cmd|suggest|copy|url|uurl)([+:;,=-])([^>\\s]+)>", "<$1$2$3>")
        .replaceAll("\\\\</(cmd|suggest|copy|url|uurl)>", "</$1>");
         */
   }
   
   private static String deserializeRgbToHex(String str) {
      return new RegexString(str)
        .replaceAll(
          "(?<!\\\\)<(\\s)?(/)?(\\s)??(\\d{1,3})[-,](\\s)?(\\d{1,3})[-,](\\s)?(\\d{1,3})(\\s)?>",
          List.of("$2", "$4", "$6", "$8"),
          rgb -> {
             String escapable = rgb[0];
             int r = Integer.parseInt(rgb[1]);
             int g = Integer.parseInt(rgb[2]);
             int b = Integer.parseInt(rgb[3]);
             System.out.println(escapable);
             return String.format("<%s#%02x%02x%02x>", escapable, r, g, b);
          }
        )
        .replaceAll("\\\\(<(\\s)?(/)?(\\s)??(\\d{1,3})[-,](\\s)?(\\d{1,3})[-,](\\s)?(\\d{1,3})(\\s)?>)", "$1");
   }
}
