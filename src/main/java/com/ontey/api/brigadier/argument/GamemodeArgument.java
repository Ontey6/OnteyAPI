package com.ontey.api.brigadier.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * A gamemode that can be called by full name or abbreviation
 */

public class GamemodeArgument implements CustomArgumentType<@NotNull GameMode, @NotNull String> {
   
   @Override
   public @NotNull GameMode parse(@NotNull StringReader reader) throws CommandSyntaxException {
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
   public @NotNull ArgumentType<String> getNativeType() {
      return StringArgumentType.word();
   }
   
   @Override
   public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
      for(String sug : List.of("survival", "adventure", "creative", "spectator"))
         if(sug.toLowerCase().startsWith(builder.getRemainingLowerCase()))
            builder.suggest(sug);
      
      return builder.buildFuture();
   }
}
