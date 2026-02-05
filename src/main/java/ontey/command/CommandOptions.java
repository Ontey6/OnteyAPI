package ontey.command;

import lombok.NonNull;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import ontey.plugin.OnteyPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Options of a {@link ConfigCommand}.
 */

public record CommandOptions(@NonNull OnteyPlugin plugin, @Nullable ConfigurationSection section) {
   
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
      return section == null ? def : plugin.getFiles().getListable(section, path, def);
   }
   
   @Nullable
   public String getMessage(@NonNull String path, @Nullable String def) {
      if(section == null)
         return def;
      
      if(section.isBoolean(path) && !section.getBoolean(path)) //requires "message: false"
         return null;
      
      return String.join("\n", getListable(path, new ArrayList<>(Collections.singletonList(def))));
   }
   
   public void sendMessage(@NonNull CommandSender sender, @NonNull String path, @Nullable String def) {
      
      String msg = getMessage(path, def);
      if(msg != null)
         sender.sendMessage(msg);
   }
   
   public void sendMessage(CommandSourceStack source, String path, String def) {
      sendMessage(source.getSender(), path, def);
   }
}