package com.ontey.api.texts;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * A static factory class for deserializing different types of texts.
 *
 * @see TextSerializer
 */

public class TextDeserializer {
   
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