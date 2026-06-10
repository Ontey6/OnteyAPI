package ontey.api.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Experimental
public record TagFormattingResults(MiniMessage miniMessage, Supplier<Component> resultSupplier) {
	
	public Component parseResult() {
		return resultSupplier.get();
	}
	
	public TagResolver tags() {
		return miniMessage.tags();
	}
}
