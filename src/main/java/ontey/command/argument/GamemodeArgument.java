package ontey.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import lombok.NonNull;
import org.bukkit.GameMode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A gamemode that can be called by full name or abbreviation
 */

@Deprecated(forRemoval = true)
public class GamemodeArgument implements CustomArgumentType<GameMode, String> {
   
   @Override
   @NonNull
   public GameMode parse(@NonNull StringReader reader) throws CommandSyntaxException {
      int cursor = reader.getCursor();
      String name = reader.readUnquotedString();
      
      return switch(name) {
         case "survival", "s", "0" -> GameMode.SURVIVAL;
         case "creative", "c", "1" -> GameMode.CREATIVE;
         case "adventure", "a", "2" -> GameMode.ADVENTURE;
         case "spectator", "sp", "spec" -> GameMode.SPECTATOR;
         default -> {
            reader.setCursor(cursor);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
         }
      };
   }
   
   @Override
   @NonNull
   public ArgumentType<String> getNativeType() {
      return StringArgumentType.word();
   }
   
   @Override
   @NonNull
   public <S> CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
      for(String sug : List.of("survival", "adventure", "creative", "spectator"))
         if(sug.toLowerCase().startsWith(builder.getRemainingLowerCase()))
            builder.suggest(sug);
      
      return builder.buildFuture();
   }
}
