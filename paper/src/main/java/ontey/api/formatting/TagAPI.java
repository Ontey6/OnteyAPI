package ontey.api.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;

//TODO docs
public final class TagAPI {
	
	private TagAPI() {
		throw new UnsupportedOperationException();
	}
	
	public static Component colorizeWithPresets(String input) {
		return new PresetTagFormatter().format(input).parseResult();
	}
	
	public static Component colorizeWithPresets(String input, List<TagResolver> resolvers) {
		return new PresetTagFormatter(resolvers).format(input).parseResult();
	}
	
	public static Component colorize(String input, List<TagResolver> resolvers) {
		return new TagFormatter(resolvers).format(input).parseResult();
	}
}
