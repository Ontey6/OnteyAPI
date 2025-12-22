package com.ontey.api.texts;

import com.mojang.brigadier.Message;
import com.ontey.api.color.minimessage.DeepMiniMessageSerializer;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

/**
 * A static factory class for serializing different types of text types.
 * That usually means, that something is converted to a {@link String}.
 *
 * @see TextDeserializer
 */

public final class TextSerializer {
   
   private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
   
   /**
    * Converts a {@link Component} to a {@link String} that can be
    * edited and then deserialized to a {@link Component} with a {@link TextDeserializer}.
    * The output will be an uncolored literal String even if deserialized with MiniMessage again.
    * <p>
    * <blockquote>
    * Example:
    *    <pre>{@code
    *          Component cmp = Component.text("Hello, <green>World!", NamedTextColor.RED);
    *          String serialized = TextSerializer.miniMessage(cmp);
    *          System.out.println(serialized);
    *       }</pre>
    * Outputs: {@code <red>Hello, \<green>World!}
    * </blockquote>
    */
   
   @Contract("null -> null; !null -> !null")
   public static String miniMessage(Component cmp) {
      return MINI_MESSAGE.serializeOrNull(cmp);
   }
   
   /**
    * Converts a {@link Component} to a {@link String} that can be
    * edited and then deserialized to a {@link Component} with a {@link TextDeserializer}.
    * If the output is deserialized with MiniMessage again into a {@link Component} {@code out}, {@code cmp} is equal to {@code out}
    * <p>
    * <blockquote>
    * Example:
    *    <pre>{@code
    *          Component cmp = Component.text("Hello, <green>World!", NamedTextColor.RED);
    *          String serialized = TextSerializer.miniMessage(cmp);
    *          System.out.println(serialized);
    *       }</pre>
    * Outputs: {@code <red>Hello, <green>World!}
    * </blockquote>
    */
   
   @Contract("null -> null; !null -> !null")
   public static String miniMessageDeep(Component cmp) {
      return DeepMiniMessageSerializer.deepSerialize(cmp);
   }
   
   @Contract("null -> null; !null -> !null")
   public static String component(Component cmp) {
      return PlainTextComponentSerializer.plainText().serializeOrNull(cmp);
   }
   
   @Contract("null -> null; !null -> !null")
   public static String message(Message msg) {
      return component(MessageComponentSerializer.message().deserializeOrNull(msg));
   }
   
   @Contract("null -> null; !null -> !null")
   public static Component messageToComponent(Message msg) {
      return MessageComponentSerializer.message().deserializeOrNull(msg);
   }
   
   public static Component editComponent(Component input, Function<String, Component> converter) {
      return converter.apply(miniMessage(input));
   }
}