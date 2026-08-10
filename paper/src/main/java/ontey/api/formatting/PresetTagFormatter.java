package ontey.api.formatting;

import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ontey.api.check.Nullity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.List;

import static net.kyori.adventure.text.event.ClickEvent.*;
import static net.kyori.adventure.text.minimessage.tag.Tag.styling;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;
import static ontey.api.formatting.TagAPI.colorize;

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
	
	private static TagResolver createPapiResolver(Player player) {
		return TagResolver.resolver("papi", (args, ctx) -> {
			if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
				var expansionName = pop(args);
				
				var expansion = PlaceholderAPIPlugin.getInstance().getLocalExpansionManager().getExpansion(expansionName);
				
				if(expansion == null)
					throw ctx.newException("Expansion not found", args);
				
				StringBuilder argsBuilder = new StringBuilder();
				
				while(args.hasNext()) {
					String arg = pop(args);
					
					argsBuilder
					  .append(arg)
					  .append('_');
				}
				
				argsBuilder.deleteCharAt(argsBuilder.length() - 1);
				
				String returnValue = expansion.onPlaceholderRequest(player, argsBuilder.toString());
				
				return Tag.inserting(colorize(Nullity.nonNullOr(returnValue, expansionName + "_" + argsBuilder), List.of()));
			} else
				throw ctx.newException("PlaceholderAPI not found", args);
		});
	}
	
	@Contract(mutates = "param1")
	private static String pop(ArgumentQueue args) {
		return args.pop().value();
	}
	
	public static @NonNull List<@NonNull TagResolver> getDefaultResolvers() {
		return DEFAULT_RESOLVERS;
	}
}
