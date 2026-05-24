package ontey.api.color.serializer;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus;

import static ontey.api.color.MiniMessageColor.MINI_MESSAGE;

/**
 * A converter from Component to MiniMessage String.
 * <br>
 * Adds colors/formatting from tags in the {@code text} while keeping formatting from the {@link Component}.
 * <br>
 * Especially useful for paper's {@link AsyncChatEvent#renderer()} which gives you a component that may already be
 * colored (e.g. by another plugin), because you may want to replace mini-message tags that the player typed as text.
 */

public class DeepMiniMessageSerializer implements ComponentSerializer<Component, Component, String> {
	
	public static final char
	  ESCAPE = '\\',
	  TAG_START = '<';
	
	private static final DeepMiniMessageSerializer instance = new DeepMiniMessageSerializer();
	
	private DeepMiniMessageSerializer() {
	}
	
	/**
	 * @return The serializer instance.
	 */
	
	@NonNull
	public static DeepMiniMessageSerializer deepMiniMessage() {
		return instance;
	}
	
	/**
	 * Deep-serializes the component.
	 * Adds colors/formatting from mini-message tags in the
	 * {@code text} while keeping formatting from the {@link Component}.
	 */
	
	@NonNull
	public static String deepSerialize(@NonNull Component component) {
		return instance.serialize(component);
	}
	
	/**
	 * Deserializes the input using {@link MiniMessage}.
	 * This method is obsolete, use {@link MiniMessage#deserialize(Object)}
	 */
	
	@Override
	@NonNull
	@ApiStatus.Obsolete
	public Component deserialize(@NonNull String input) {
		return MINI_MESSAGE.deserialize(input);
	}
	
	/**
	 * Deep-serializes the component.
	 * Adds colors/formatting from mini-message tags in the
	 * {@code text} while keeping formatting from the {@link Component}.
	 */
	
	@Override
	@NonNull
	public String serialize(@NonNull Component component) {
		String str = MINI_MESSAGE.serializeOrNull(component);
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			
			if(c == ESCAPE && i + 1 < str.length()) {
				char next = str.charAt(i + 1);
				
				if(next == ESCAPE || next == TAG_START) {
					sb.append(ESCAPE);
					if(next == ESCAPE)
						i++;
					continue;
				}
			}
			
			sb.append(c);
		}
		
		return sb.toString();
	}
}