package com.ontey.api.plugin.texts;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;

/**
 * A static factory class for deserializing different types of texts.
 *
 * @see TextSerializer
 */

@NullMarked
public final class TextDeserializer {
   
   private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
   
   public static Component miniMessage(String str) {
      return MINI_MESSAGE.deserialize(str);
   }
   
   public static Component component(String str) {
      return Component.text(str);
   }
   
   public static Component component(String str, TextColor color) {
      return Component.text(str, color);
   }
   
   public static Message message(Component cmp) {
      return MessageComponentSerializer.message().serialize(cmp);
   }
   
   public static Message message(String str) {
      return MessageComponentSerializer.message().serialize(Component.text(str));
   }
}