package ontey.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.NonNull;
import ontey.command.argument.LiteralArgumentBuilder;
import ontey.check.Checker;
import ontey.plugin.OnteyPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that associated with an {@link OnteyPlugin}
 */

public class Command {
   
   /**
    * A command's return values for success.
    * Just for code-readability, value doesn't have any meaning
    */
   
   public static final int SUCCESS = 1;
   
   /**
    * A command's return values for fail.
    * Just for code-readability, value doesn't have any meaning
    */
   
   public static final int FAIL = 0;
   
   @Getter(onMethod_ = @NonNull)
   @UnknownNullability
   protected LiteralCommandNode<CommandSourceStack> root;
   
   @Getter(onMethod_ = @NonNull)
   protected String description;
   
   @Nullable
   @Getter
   protected String permission;
   
   @Getter(onMethod_ = @NonNull)
   protected final List<String> names = new ArrayList<>();
   
   /**
    * The identifier of this command. Can not be changed by users whatsoever
    */
   
   @NonNull
   @Getter
   protected final String identifier;
   
   protected final OnteyPlugin plugin;
   
   public Command(@NonNull OnteyPlugin plugin, @NonNull String name) {
      this.identifier = name;
      this.plugin = plugin;
      names.addFirst(name);
   }
   
   /**
    * @return The "name" of this command; first value of names.
    * @throws IllegalStateException if names is empty
    * @apiNote This method was made for only registration as brigadier (and like every other Command API)
    *          has a system where every command has one name and can have multiple aliases.
    *          Usually, just use {@link #getIdentifier()} instead.
    */
   
   // should only be called in registration
   @NonNull
   public String getName() {
      if(names.isEmpty())
         throw new IllegalStateException("getName() called when names are empty, use getIdentifier!");
      
      return names.getFirst();
   }
   
   /**
    * @return The "aliases" of this command; all names values except for the first one
    * @apiNote This method was made for only registration as brigadier (and like every other Command API)
    *          has a system where every command has one name and can have multiple aliases.
    *          Other that {@link #getName()}, this method has no side effects and can safely be used.
    */
   
   @NonNull
   @Contract(pure = true)
   public List<String> getAliases() {
      if(names.size() < 2)
         return new ArrayList<>(0);
      
      return names.subList(1, names.size());
   }
   
   /**
    * Sets the root of this command.
    */
   
   @NonNull
   public Command setRoot(@NonNull LiteralArgumentBuilder root) {
      this.root = root
        .requires(source -> permission == null || source.getSender().hasPermission(permission))
        .build();
      
      return this;
   }
   
   /**
    * Adds aliases to this command
    */
   
   @NonNull
   public Command addAliases(@NonNull List<String> aliases) {
      names.addAll(aliases);
      
      return this;
   }
   
   /**
    * Adds aliases to this command
    */
   
   @NonNull
   public Command addAliases(@NonNull String @NonNull ... aliases) {
      return addAliases(List.of(aliases));
   }
   
   /**
    * Sets the permission of this command, can be null to be ignored ({@link #setRoot})
    */
   
   @NonNull
   public Command setPermission(@Nullable String permission) {
      this.permission = permission;
      
      return this;
   }
   
   /**
    * Sets the description of this command
    */
   
   @NonNull
   public Command setDescription(@NonNull String description) {
      this.description = description;
      
      return this;
   }
   
   /**
    * Registers this command to the specified registry.
    * The {@link #root} of this command has to be non-null for this.
    */
   
   public void register(@NonNull CommandRegistry registry) {
      if(names.isEmpty())
         return;
      
      Checker.checkNonNull(root, "The root hasn't been set at registration. Dev's fault");
      
      registry.addCommand(this);
   }
}
