package ontey.api.color;

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

import static net.kyori.adventure.text.event.ClickEvent.*;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static net.kyori.adventure.text.minimessage.tag.Tag.styling;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

/**
 * A coloring method that adds shortcuts for {@link MiniMessage} {@link ClickEvent}s.
 */

public final class MiniMessageColor {
	
	@NonNull
	public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
	
	private static final TagResolver
	  resolvers = resolver(
	  resolver("cmd", (args, _) -> styling(runCommand(pop(args)))),
	  resolver("ucmd", (args, _) -> styling(UNDERLINED, runCommand(pop(args)))),
	  resolver("suggest", (args, _) -> styling(suggestCommand(pop(args)))),
	  resolver("usuggest", (args, _) -> styling(UNDERLINED, suggestCommand(pop(args)))),
	  resolver("copy", (args, _) -> styling(copyToClipboard(pop(args)))),
	  resolver("ucopy", (args, _) -> styling(UNDERLINED, copyToClipboard(pop(args)))),
	  resolver("url", (args, _) -> styling(openUrl(pop(args)))),
	  resolver("uurl", (args, _) -> styling(UNDERLINED, openUrl(pop(args)))),
	  resolver("file", (args, _) -> styling(openFile(pop(args)))),
	  resolver("ufile", (args, _) -> styling(UNDERLINED, openFile(pop(args))))
	);
	
	@NonNull
	public static Component colorize(@NonNull String msg) {
		return MINI_MESSAGE.deserialize(msg, resolvers);
	}
	
	/**
	 * TODO test
	 *
	 * @param player
	 * @return
	 */
	
	@ApiStatus.Experimental
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
