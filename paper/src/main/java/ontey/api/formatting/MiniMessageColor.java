package ontey.api.formatting;

import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ontey.api.check.Nullity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A coloring method that adds shortcuts for {@link MiniMessage} {@link ClickEvent}s.
 *
 * @deprecated - Use Tag API instead ({@link PresetTagFormatter#format(String)}{@code .parseResult()} is the equivalent of {@link MiniMessageColor#colorize(String)})
 */

@ApiStatus.Experimental
@Deprecated(forRemoval = true)
public final class MiniMessageColor {
	
	@Deprecated(forRemoval = true)
	public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
	
	private static final PresetTagFormatter FORMATTER = new PresetTagFormatter();
	
	@NonNull
	@Deprecated(forRemoval = true)
	public static Component colorize(@NonNull String msg) {
		return FORMATTER.format(msg).parseResult();
	}
	
	/**
	 * @deprecated - Use Tag API instead
	 */
	
	@Deprecated(forRemoval = true)
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
				
				return Tag.inserting(colorize(Nullity.nonNullOr(returnValue, expansionName + "_" + argsBuilder)));
			} else
				throw ctx.newException("PlaceholderAPI not found", args);
		});
	}
	
	@Contract(mutates = "param1")
	private static String pop(ArgumentQueue args) {
		return args.pop().value();
	}
}
