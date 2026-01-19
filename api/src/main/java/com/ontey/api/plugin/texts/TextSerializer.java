package com.ontey.api.plugin.texts;

import com.mojang.brigadier.Message;
import com.ontey.api.plugin.color.serializer.DeepMiniMessageSerializer;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A static factory class for serializing different types of text types.
 * That usually means, that something is converted to a {@link String}.
 *
 * @see TextDeserializer
 */

@NullMarked
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
   
   public static String miniMessageDeep(Component cmp) {
      return DeepMiniMessageSerializer.deepSerialize(cmp);
   }
   
   public static String component(Component cmp) {
      return PlainTextComponentSerializer.plainText().serialize(requireNonNull(cmp));
   }
   
   public static String message(Message msg) {
      return component(MessageComponentSerializer.message().deserializeOrNull(requireNonNull(msg)));
   }
   
   public static Component messageToComponent(Message msg) {
      return MessageComponentSerializer.message().deserializeOrNull(requireNonNull(msg));
   }
   
   /**
    * <b>Experimental</b>: I added this sometime back and don't remember if it's working
    * right now and I just want to push the update finally, might be removed.
    */
   
   @ApiStatus.Experimental
   public static Component editComponent(Component input, Function<String, Component> converter) {
      return converter.apply(miniMessage(input));
   }
}