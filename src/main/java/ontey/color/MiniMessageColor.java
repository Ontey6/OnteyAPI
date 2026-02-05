package ontey.color;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Objects;

import static net.kyori.adventure.text.event.ClickEvent.*;
import net.kyori.adventure.text.event.ClickEvent;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static net.kyori.adventure.text.minimessage.tag.Tag.styling;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

/**
 * A coloring method that adds shortcuts for {@link MiniMessage} {@link ClickEvent ClickEvents}.
 */

public final class MiniMessageColor {
   
   @NonNull
   public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
   
   @NonNull
   public static Component colorize(@NonNull String msg) {
      Objects.requireNonNull(msg);
      
      final TagResolver
        cmd     = resolver("cmd",     (args, ctx) -> styling(runCommand(str(args)))),
        suggest = resolver("suggest", (args, ctx) -> styling(suggestCommand(str(args)))),
        copy    = resolver("copy",    (args, ctx) -> styling(copyToClipboard(str(args)))),
        url     = resolver("url",     (args, ctx) -> styling(openUrl(str(args)))),
        uurl    = resolver("uurl",    (args, ctx) -> styling(UNDERLINED, openUrl(str(args)))),
        file    = resolver("file",    (args, ctx) -> styling(openFile(str(args)))),
        
        // Combined resolvers
        resolvers = resolver(cmd, suggest, copy, url, uurl, file);
      
      return MINI_MESSAGE.deserialize(msg, resolvers);
   }
   
   private static String str(ArgumentQueue args) {
      return args.pop().value();
   }
}
