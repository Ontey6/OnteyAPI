package ontey.api.formatting;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for replacing tag placeholders in {@link String}s and creating a {@link Component} using {@link MiniMessage}.
 * Tag placeholders look like this: {@code <my-placeholder>}.
 * They can have arguments that are split using a colon ({@code :}).
 * Example: {@code <uuid-for-player:MinecraftPlayer>}.
 * Arguments can also use quotes (single or double): {@code <rainbow:"Isn't Rainbow-ing stuff fun?">}
 */

@ApiStatus.Experimental
public class TagFormatter {
	
	protected final List<TagResolver> tagResolvers = new ArrayList<>();
	
	public TagFormatter() {
	}
	
	public TagFormatter(@NonNull Collection<TagResolver> resolvers) {
		addTagResolvers(resolvers);
	}
	
	public void addTagResolver(@NonNull TagResolver resolver) {
		tagResolvers.add(resolver);
	}
	
	public void addTagResolvers(@NonNull Collection<TagResolver> resolvers) {
		tagResolvers.addAll(resolvers);
	}
	
	public List<TagResolver> getTagResolvers() {
		return List.copyOf(tagResolvers);
	}
	
	public TagFormattingResults format(String input) {
		MiniMessage miniMessage = MiniMessage.builder()
		  .tags(TagResolver.resolver(tagResolvers))
		  .build();
		
		return new TagFormattingResults(miniMessage, () -> miniMessage.deserialize(input));
	}
}
