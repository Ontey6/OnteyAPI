package ontey.api.text;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import ontey.api.check.Nullity;
import ontey.api.color.serializer.DeepMiniMessageSerializer;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

import static ontey.api.color.MiniMessageColor.MINI_MESSAGE;

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
	 * <br>
	 * <blockquote>
	 * Example:
	 * <pre>{@code
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
	 * <br>
	 * <blockquote>
	 * Example:
	 * <pre>{@code
	 *          Component cmp = Component.text("Hello, <green>World!", NamedTextColor.RED);
	 *          String serialized = TextSerializer.miniMessage(cmp);
	 *          System.out.println(serialized);
	 *       }</pre>
	 * Outputs: {@code <red>Hello, <green>World!}
	 * </blockquote>
	 */
	
	@NonNull
	public static String miniMessageDeep(@NonNull Component cmp) {
		return DeepMiniMessageSerializer.deepSerialize(cmp);
	}
	
	@NonNull
	public static String component(@NonNull Component cmp) {
		return PlainTextComponentSerializer.plainText().serialize(cmp);
	}
	
	@NonNull
	public static String messageToString(@NonNull Message msg) {
		return component(MessageComponentSerializer.message().deserialize(Nullity.nonNull(msg)));
	}
	
	@NonNull
	public static Component messageToComponent(@NonNull Message msg) {
		return MessageComponentSerializer.message().deserialize(Nullity.nonNull(msg));
	}
	
	/**
	 * <b>Experimental</b>: I added this sometime back and don't remember if it's working/safe
	 * right now and I just want to push the update finally, might be removed.
	 */
	@ApiStatus.Experimental
	@NonNull
	public static Component editComponent(@NonNull Component input, @NonNull Function<@NonNull String, @NonNull Component> converter) {
		return converter.apply(miniMessage(input));
	}
}