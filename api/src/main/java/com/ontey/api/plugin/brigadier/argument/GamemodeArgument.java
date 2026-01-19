package com.ontey.api.plugin.brigadier.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.GameMode;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A gamemode that can be called by full name or abbreviation
 */

@NullMarked
public class GamemodeArgument implements CustomArgumentType<GameMode, String> {
   
   @Override
   public GameMode parse(StringReader reader) throws CommandSyntaxException {
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
   public ArgumentType<String> getNativeType() {
      return StringArgumentType.word();
   }
   
   @Override
   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      for(String sug : List.of("survival", "adventure", "creative", "spectator"))
         if(sug.toLowerCase().startsWith(builder.getRemainingLowerCase()))
            builder.suggest(sug);
      
      return builder.buildFuture();
   }
}
