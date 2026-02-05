package ontey.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ontey.check.Checker;
import ontey.plugin.OnteyPlugin;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static ontey.config.Config.cutYamlSuffix;

@RequiredArgsConstructor(onConstructor_ = @ApiStatus.Internal)
public class ConfigManager {

   private final OnteyPlugin plugin;
   
   public static final String PATTERN = "[a-zA-Z0-9._-]+(?<!.y(a)?ml)";

   @NonNull
   @Getter
   private final Map<@NonNull String, @NonNull Config> configs = new HashMap<>();
   
   /**
    * Adds a config.
    * @param identifier The identifier. doesn't need the file ending like {@code .yml}
    * @return The created config
    */

   @NonNull
   public Config addConfig(@NonNull @Pattern(PATTERN) String identifier) {
      Checker.checkArgument(identifier.matches(PATTERN), "identifier doesn't match pattern: " + PATTERN + " (" + identifier + ")");
      
      return addConfig(new Config(file(identifier + ".yml")));
   }

   @NonNull
   public Config addConfig(@NonNull @Pattern(PATTERN) String first, @NonNull String @NonNull ... path) {
      //noinspection PatternValidation
      Config config = addConfig(first + "/" + String.join("/", path));

      return addConfig(config);
   }

   public Config addConfig(@NonNull Config config) {
      configs.put(cutYamlSuffix(config.getFile().getName()), config);
      return config;
   }

   @NonNull
   public Config getConfig(@NonNull String identifier) {
      var config = configs.get(identifier);

      if(config == null)
         throw new IllegalArgumentException("No config with identifier '" + identifier + "'");

      return config;
   }
   
   private File file(String child) {
      return new File(plugin.getDataFolder(), child);
   }
}
