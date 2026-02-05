package ontey.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * A player found by only name, not selectors
 */

@Deprecated(forRemoval = true)
public class OnlinePlayerNameArgument implements CustomArgumentType<Player, String> {
   
   @Override
   @NonNull
   public Player parse(@NonNull StringReader reader) throws CommandSyntaxException {
      int cursor = reader.getCursor();
      String name = reader.readUnquotedString();
      
      Player p = Bukkit.getPlayerExact(name);
      
      if(p == null) {
         reader.setCursor(cursor);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
      }
      return p;
   }
   
   @Override
   @NonNull
   public ArgumentType<String> getNativeType() {
      return StringArgumentType.word();
   }
   
   @Override
   @NonNull
   public <S> CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
      if(!(context.getSource() instanceof CommandSourceStack))
         throw new IllegalStateException("Expected CommandSourceStack, got " + context.getSource().getClass());
      
      String input = builder.getRemainingLowerCase();
      
      for(Player player : Bukkit.getOnlinePlayers()) {
         String name = player.getName();
         if(name.toLowerCase().startsWith(input))
            builder.suggest(name);
      }
      
      return builder.buildFuture();
   }
}