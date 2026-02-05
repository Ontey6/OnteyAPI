package ontey.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RequiredArgumentBuilder<T> extends ArgumentBuilder<RequiredArgumentBuilder<T>> {
   private final String name;
   private final ArgumentType<T> type;
   private SuggestionProvider<CommandSourceStack> suggestionsProvider = null;
   
   public RequiredArgumentBuilder<T> suggests(final SuggestionProvider<CommandSourceStack> provider) {
      this.suggestionsProvider = provider;
      return getThis();
   }
   
   @Override
   protected RequiredArgumentBuilder<T> getThis() {
      return this;
   }
   
   public ArgumentCommandNode<CommandSourceStack, T> build() {
      final var result = new ArgumentCommandNode<>(getName(), getType(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider());
      
      for(final CommandNode<CommandSourceStack> argument : getArguments())
         result.addChild(argument);
      
      return result;
   }
}
