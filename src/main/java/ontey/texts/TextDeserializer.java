package ontey.texts;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import static ontey.color.MiniMessageColor.MINI_MESSAGE;

/**
 * A static factory class for deserializing different types of texts.
 *
 * @see TextSerializer
 */

public final class TextDeserializer {
   
   @NonNull
   public static Component miniMessage(@NonNull String str) {
      return MINI_MESSAGE.deserialize(str);
   }
   
   @NonNull
   public static Component component(@NonNull String str) {
      return Component.text(str);
   }
   
   @NonNull
   public static Component component(@NonNull String str, @NonNull TextColor color) {
      return Component.text(str, color);
   }
   
   @NonNull
   public static Message message(@NonNull Component cmp) {
      return MessageComponentSerializer.message().serialize(cmp);
   }
   
   @NonNull
   public static Message message(@NonNull String str) {
      return MessageComponentSerializer.message().serialize(Component.text(str));
   }
}