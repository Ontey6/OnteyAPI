package ontey.api.command.config;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.NonNull;
import ontey.api.command.ConfigCommand;
import ontey.api.config.ConfigSection;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Options of a {@link ConfigCommand}.
 */

public record CommandOptions(@Nullable ConfigSection section) {
	
	@Contract("_, !null -> !null")
	public <T> T get(@NonNull String path, @Nullable T def) {
		try {
			if(section == null)
				return def;
			//noinspection unchecked
			return (T) section.get(path, def);
		} catch(ClassCastException e) {
			return def;
		}
	}
	
	public List<@Nullable String> getListable(@NonNull String path, List<@Nullable String> def) {
		if(section == null)
			return def;
		
		if(section.isList(path))
			return section.getStringList(path);
		
		if(section.isSection(path))
			return def;
		
		return new ArrayList<>(List.of(section.getString(path)));
	}
	
	@Nullable
	public String getMessage(@NonNull String path, @Nullable String def) {
		if(section == null)
			return def;
		
		//noinspection PointlessBooleanExpression adds clarity
		if(section.getBoolean(path, true) == false) //requires "message: false"
			return null;
		
		return String.join("\n", getListable(path, new ArrayList<>(Collections.singletonList(def))));
	}
	
	public void sendMessage(@NonNull CommandSender sender, @NonNull String path, @Nullable String def) {
		
		String msg = get(path, def);
		if(msg != null)
			sender.sendMessage(msg);
	}
	
	public void sendMessage(@NonNull CommandSourceStack source, @NonNull String path, @Nullable String def) {
		sendMessage(source.getSender(), path, def);
	}
}