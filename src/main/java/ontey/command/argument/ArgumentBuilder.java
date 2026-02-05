package ontey.command.argument;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class ArgumentBuilder<T extends ArgumentBuilder<T>> extends com.mojang.brigadier.builder.ArgumentBuilder<CommandSourceStack, T> {
   
   @Nullable
   protected String permission;
   
   public T permission(@Nullable String permission) {
      this.permission = permission;
      return getThis();
   }
   
   @Override
   public Predicate<CommandSourceStack> getRequirement() {
      if(super.getRequirement() == null)
         return null;
      
      return super.getRequirement().and(src -> permission == null || src.getSender().hasPermission(permission));
   }
}
