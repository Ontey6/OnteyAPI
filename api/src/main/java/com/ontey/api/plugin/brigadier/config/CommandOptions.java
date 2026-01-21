package com.ontey.api.plugin.brigadier.config;

import com.ontey.api.plugin.files.Files;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.ontey.api.plugin.files.Files.singletonList;

public record CommandOptions(@Nullable ConfigurationSection section) {
   
   @Contract("_, !null -> !null")
   public <T> T get(String path, T def) {
      try {
         if(section == null)
            return def;
         //noinspection unchecked
         return (T) section.get(path, def);
      } catch(ClassCastException e) {
         return def;
      }
   }
   
   @NotNull
   public List<String> getListable(String path, List<String> def) {
      return section == null ? def : Files.getListable(section, path, def);
   }
   
   @Nullable
   public String getMessage(String path, String def) {
      if(section == null)
         return def;
      if(section.isBoolean(path) && !section.getBoolean(path))
         return null;
      return String.join("\n", getListable(path, singletonList(def)));
   }
   
   public void sendMessage(CommandSender sender, String path, String def) {
      String msg = getMessage(path, def);
      if(msg != null)
         sender.sendMessage(msg);
   }
   
   public void sendMessage(CommandSourceStack source, String path, String def) {
      sendMessage(source.getSender(), path, def);
   }
}