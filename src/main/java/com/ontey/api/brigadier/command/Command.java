package com.ontey.api.brigadier.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ontey.api.brigadier.registry.CommandRegistry;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Command {
   
   /**
    * A command's return values for success.
    * Just for code-readability, value doesn't have any meaning
    */
   
   public static final int SUCCESS = 1;
   
   /**
    * A command's return values for success.
    * Just for code-readability, value doesn't have any meaning
    */
   
   public static final int FAIL = 0;
   
   @Getter
   protected LiteralCommandNode<CommandSourceStack> root;
   
   @Getter
   protected String description;
   
   @Nullable
   @Getter
   protected String permission;
   
   /*
    * 1.1-alpha. Combined name, aliases and enabled into names
    *
    * The name is just names.getFirst()
    *
    * The aliases are
    *
    * To disable command, just set names to an empty list.
    */
   
   @NotNull
   @Getter
   protected final List<String> names = new ArrayList<>();
   
   public final String identifier;
   
   public Command(String name) {
      this.identifier = name;
      names.addFirst(name);
   }
   
   // should only be called in registration
   public String getName() {
      // to make things easier
      if(names.isEmpty())
         return identifier;
      
      return names.getFirst();
   }
   
   public List<String> getAliases() {
      if(names.size() < 2)
         return new ArrayList<>(0);
      
      return names.subList(1, names.size());
   }
   
   public Command setRoot(LiteralArgumentBuilder<CommandSourceStack> root) {
      this.root = root
        .requires(source -> permission == null || source.getSender().hasPermission(permission))
        .build();
      
      return this;
   }
   
   public Command addAliases(List<String> aliases) {
      Objects.requireNonNull(aliases);
      
      names.addAll(aliases);
      
      return this;
   }
   
   public Command addAliases(String... aliases) {
      return addAliases(List.of(aliases));
   }
   
   public Command setPermission(@Nullable String perm) {
      this.permission = perm;
      
      return this;
   }
   
   public Command setDescription(@NotNull String desc) {
      Objects.requireNonNull(desc);
      
      this.description = desc;
      
      return this;
   }
   
   public void register() {
      // disabled
      if(names.isEmpty())
         return;
      
      Objects.requireNonNull(root, "The root hasn't been set at registration. Dev's fault");
      
      CommandRegistry.commands.add(this);
   }
}