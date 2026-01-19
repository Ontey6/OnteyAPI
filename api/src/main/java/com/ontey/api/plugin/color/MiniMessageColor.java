package com.ontey.api.plugin.color;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

import static net.kyori.adventure.text.event.ClickEvent.*;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static net.kyori.adventure.text.minimessage.tag.Tag.styling;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

@NullMarked
public final class MiniMessageColor {
   
   public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
   
   public static Component colorize(String msg) {
      Objects.requireNonNull(msg);
      
      final TagResolver
        cmd     = resolver("cmd",     (args, ctx) -> styling(runCommand(args.pop().value()))),
        suggest = resolver("suggest", (args, ctx) -> styling(suggestCommand(args.pop().value()))),
        copy    = resolver("copy",    (args, ctx) -> styling(copyToClipboard(args.pop().value()))),
        url     = resolver("url",     (args, ctx) -> styling(openUrl(args.pop().value()))),
        uurl    = resolver("uurl",    (args, ctx) -> styling(UNDERLINED, openUrl(args.pop().value()))),
        file    = resolver("file",    (args, ctx) -> styling(openFile(args.pop().value()))),
        
        // Combined resolvers
        resolvers = resolver(cmd, suggest, copy, url, uurl, file);
      
      return MINI_MESSAGE.deserialize(msg, resolvers);
   }
}
