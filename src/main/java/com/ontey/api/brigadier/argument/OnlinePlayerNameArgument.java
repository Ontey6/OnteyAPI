package com.ontey.api.brigadier.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * A player found by name
 */

public class OnlinePlayerNameArgument implements CustomArgumentType<@NotNull Player, @NotNull String> {
   
   @Override
   public @NotNull Player parse(@NotNull StringReader reader) throws CommandSyntaxException {
      int cursor = reader.getCursor();
      String name = reader.readUnquotedString();
      
      Player p = Bukkit.getPlayerExact(name);
      
      if (p == null) {
         reader.setCursor(cursor);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
      }
      return p;
   }
   
   @Override
   public @NotNull ArgumentType<@NotNull String> getNativeType() {
      return StringArgumentType.word();
   }
   
   @Override
   public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
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