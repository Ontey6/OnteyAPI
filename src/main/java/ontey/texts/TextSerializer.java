package ontey.texts;

import com.mojang.brigadier.Message;
import lombok.NonNull;
import ontey.color.serializer.DeepMiniMessageSerializer;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static ontey.color.MiniMessageColor.MINI_MESSAGE;

/**
 * A static factory class for serializing different types of text types.
 * That usually means, that something is converted to a {@link String}.
 *
 * @see TextDeserializer
 */

public final class TextSerializer {
   
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
   
   @NonNull
   public static String miniMessage(@NonNull Component cmp) {
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
   
   public static String miniMessageDeep(@NonNull Component cmp) {
      return DeepMiniMessageSerializer.deepSerialize(cmp);
   }
   
   public static String component(@NonNull Component cmp) {
      return PlainTextComponentSerializer.plainText().serialize(requireNonNull(cmp));
   }
   
   public static String message(@NonNull Message msg) {
      return component(MessageComponentSerializer.message().deserializeOrNull(requireNonNull(msg)));
   }
   
   public static Component messageToComponent(@NonNull Message msg) {
      return MessageComponentSerializer.message().deserializeOrNull(requireNonNull(msg));
   }
   
   /**
    * <b>Experimental</b>: I added this sometime back and don't remember if it's working
    * right now and I just want to push the update finally, might be removed.
    */
   
   @ApiStatus.Experimental
   public static Component editComponent(@NonNull Component input, @NonNull Function<@NonNull String, @NonNull Component> converter) {
      return converter.apply(miniMessage(input));
   }
}