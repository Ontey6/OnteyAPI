package ontey.api.formatting;

import lombok.NonNull;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.List;

import static net.kyori.adventure.text.event.ClickEvent.*;
import static net.kyori.adventure.text.minimessage.tag.Tag.styling;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

/**
 * A {@link TagFormatter} that inserts some recommended preset placeholders
 */

@ApiStatus.Experimental
public class PresetTagFormatter extends TagFormatter {
	
	@NonNull
	private static final List<@NonNull TagResolver> DEFAULT_RESOLVERS = List.of(
	  resolver("cmd", (args, _) -> styling(runCommand(pop(args)))),
	  resolver("suggest", (args, _) -> styling(suggestCommand(pop(args)))),
	  resolver("copy", (args, _) -> styling(copyToClipboard(pop(args)))),
	  resolver("url", (args, _) -> styling(openUrl(pop(args)))),
	  resolver("file", (args, _) -> styling(openFile(pop(args))))
	);
	
	public PresetTagFormatter() {
		addTagResolvers(DEFAULT_RESOLVERS);
	}
	
	public PresetTagFormatter(@NonNull List<@NonNull TagResolver> resolvers) {
		super(resolvers);
		addTagResolvers(DEFAULT_RESOLVERS);
	}
	
	@Contract(mutates = "param1")
	private static String pop(ArgumentQueue args) {
		return args.pop().value();
	}
	
	public static @NonNull List<@NonNull TagResolver> getDefaultResolvers() {
		return DEFAULT_RESOLVERS;
	}
}
