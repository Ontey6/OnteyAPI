package ontey.command.argument;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class LiteralArgumentBuilder extends ArgumentBuilder<LiteralArgumentBuilder> {
   
   @Getter
   private final String literal;
   
   @Override
   protected LiteralArgumentBuilder getThis() {
      return this;
   }
   
   @Override
   public LiteralCommandNode<CommandSourceStack> build() {
      final var result = new LiteralCommandNode<>(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());
      
      for(final CommandNode<CommandSourceStack> argument : getArguments())
         result.addChild(argument);
      
      return result;
   }
}
