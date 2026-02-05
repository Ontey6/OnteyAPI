package ontey.log;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Deprecated(forRemoval = true)
public class PluginLogger extends NamedLogger {
   
   public PluginLogger(JavaPlugin plugin) {
      super(Objects.requireNonNullElse(plugin.getPluginMeta().getLoggerPrefix(), plugin.getName()));
   }
}
