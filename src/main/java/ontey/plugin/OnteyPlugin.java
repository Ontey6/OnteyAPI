package ontey.plugin;

import ontey.command.*;
import ontey.check.Checker;
import ontey.check.Nullity;
import ontey.classfinder.*;
import ontey.config.*;
import ontey.file.*;
import ontey.loader.Loader;
import ontey.loader.SingleClassLoader;
import ontey.loader.SubclassLoader;
import ontey.log.NamedLogger;
import ontey.command.argument.Arg;
import io.papermc.paper.plugin.configuration.PluginMeta;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class OnteyPlugin extends JavaPlugin {
   
   public OnteyPlugin() {
      this(true);
   }
   
   public OnteyPlugin(boolean loadDefaultLoaders) {
      if(loadDefaultLoaders) {
         registerCommands();
         registerConfigs();
         registerEvents();
         // unfinished
         //registerEvent(new MenuClickListener());
      }
   }
   
   /**
    * The plugin manager of the server.
    * Can tell you info about, query, en/disable plugins and more.
    * @see PluginManager
    */
   
   @Getter(AccessLevel.PROTECTED) // static getter protected
   @NonNull
   private static final PluginManager pluginManager = Bukkit.getPluginManager();
   
   /**
    * The service(s) manager of the server.
    * Used to register instances of your plugin's classes and provide them to other plugins which use your plugin's API.
    */
   
   @Getter(AccessLevel.PROTECTED) // static getter protected
   @NonNull
   private static final ServicesManager servicesManager = Bukkit.getServicesManager();
   
   /**
    * The {@link FileLog} of this plugin.
    * Used to save stack-traces of {@linkplain Throwable exceptions} to files.
    */
   
   @Getter
   @NonNull
   private final FileLog fileLog = new FileLog(this);
   
   /**
    * A shortcut for {@link #getPluginMeta()}
    */
   
   @Getter
   @NonNull
   private final PluginMeta meta = getPluginMeta();
   
   /**
    * The logger of this plugin that you should use.
    * can't be called {@code getLogger()} because of {@link #getLogger()} in {@link JavaPlugin}.
    */
   
   @Getter
   @NonNull
   private final NamedLogger log = new NamedLogger(getLoggerPrefix());
   
   /**
    * The {@link CommandRegistry command registry} of this plugin.
    */
   
   @Getter(onMethod_ = @ApiStatus.Internal)
   @NonNull
   private final CommandRegistry commandRegistry = new CommandRegistry(this);
   
   /**
    * The {@link ConfigManager} of this plugin
    */

   @Getter
   @NonNull
   private final ConfigManager configManager = new ConfigManager(this);

   /**
    * The command-file of this plugin.
    * Used to store all {@link ConfigCommand ConfigCommands}
    */

   @Getter
   @NonNull
   private final Config commandsConfig = configManager.addConfig("commands");
   
   /**
    * The {@link SubclassLoader loaders} of this plugin.
    * Includes
    */
   
   @Getter
   @NonNull
   private final Set<Loader> loaders = new HashSet<>();
   
   /**
    * Gets the version of this plugin.
    * Shortcut for {@link PluginMeta#getVersion getVersion()} in {@link PluginMeta}.
    */
   
   @NonNull
   public String getVersion() {
      return meta.getVersion();
   }
   
   /**
    * Gets the logger prefix of this plugin.
    * If there is none specified, uses the plugin's name.
    * Shortcut for {@link PluginMeta#getLoggerPrefix getDisplayName()} in {@link PluginMeta}.
    */
   
   @NonNull
   public String getLoggerPrefix() {
      return Nullity.nonNullOr(meta.getLoggerPrefix(), getName());
   }
   
   /**
    * Gets the display name of this plugin.
    * Shortcut for {@link PluginMeta#getDisplayName getDisplayName()} in {@link PluginMeta}.
    */
   
   @NonNull
   public String getDisplayName() {
      return meta.getDisplayName();
   }
   
   /**
    * Registers the specified {@link Listener listener}.
    */
   
   public void registerEvent(@NonNull Listener listener) {
      pluginManager.registerEvents(listener, this);
   }
   
   public void registerEvents() {
      registerSubclassLoader(Listener.class, this::registerEvent);
   }
   
   /**
    * Registers the command in this plugin's {@link #commandRegistry}.
    */
   
   public void registerCommand(@NonNull Command cmd) {
      cmd.register(commandRegistry);
   }
   
   /**
    * Registers the registerer.
    * The registerer needs to have the annotation {@link CommandName @CommandName} which specifies the name.
    *
    * @throws IllegalArgumentException if the registerer is not annotated with {@link CommandName @CommandName}.
    */
   
   public void registerCommand(@NonNull CommandRegisterer registerer) {
      Checker.checkArgument(
        registerer.getClass().isAnnotationPresent(CommandName.class),
        "registerer needs to be annotated with @CommandName in order to be registered without name"
      );
      
      String name = registerer.getClass().getAnnotation(CommandName.class).value();
      
      registerCommand(name, registerer);
   }
   
   /**
    * Registers the registerer with the specified name, ignores the registerer's name if it has one.
    */
   
   public void registerCommand(@NonNull String name, @NonNull CommandRegisterer registerer) {
      Command cmd = new Command(this, name);
      var root = Arg.literal(name);
      
      registerer.register(cmd, root);
      
      cmd
        .setRoot(root)
        .register(commandRegistry);
   }
   
   /**
    * Registers the registerer with the specified name, ignores the registerer's name if it has one.
    */
   
   public void registerCommand(@NonNull String name, @NonNull MiscCommandRegisterer registerer) {
      var root = Arg.literal(name);
      Command cmd = registerer.register(this, root);
      
      cmd
        .setRoot(root)
        .register(commandRegistry);
   }
   
   /**
    * Registers the registerer.
    * The registerer needs to have the annotation {@link CommandName @CommandName} which specifies the name.
    *
    * @throws IllegalArgumentException if the registerer is not annotated with {@link CommandName @CommandName}.
    */
   
   public void registerCommand(@NonNull MiscCommandRegisterer registerer) {
      var clazz = registerer.getClass();
      
      Checker.checkArgument(
        clazz.isAnnotationPresent(CommandName.class),
        "registerer needs to be annotated with @CommandName in order to be registered without name"
      );
      
      String name = clazz.getAnnotation(CommandName.class).value();
      
      registerCommand(name, registerer);
   }
   
   /**
    * Registers all {@link CommandRegisterer commands} in this plugin's JAR.
    * Requires each one to be annotated with {@link CommandName @CommandName}, otherwise skips that one.
    */
   
   public void registerCommands() {
      registerSubclassLoader(CommandRegisterer.class, this::registerCommand);
      
      registerSubclassLoader(MiscCommandRegisterer.class, this::registerCommand);
   }
   
   /**
    * Registers a config.
    * @return The registered config
    */

   @NonNull
   public Config registerConfig(@NonNull Config config) {
      return configManager.addConfig(config);
   }

   /**
    * Registers a config with the given identifier.
    * <p>
    * Example: {@code config.yml}
    * @return The registered config
    */

   @NonNull
   public Config registerConfig(@NonNull @Pattern(ConfigManager.PATTERN) String identifier) {
      //noinspection PatternValidation
      return configManager.addConfig(identifier);
   }

   /**
    * Registers a config with a path.
    * <p>
    * Example: {@code language, en_us} --> {@code Path.of(language, en_us.yml)}
    * @return The registered config
    */

   @NonNull
   public Config registerConfig(@Pattern(ConfigManager.PATTERN) String first, @NonNull String @NonNull ... path) {
      //noinspection PatternValidation
      return configManager.addConfig(first, path);
   }
   
   /**
    * Registers all {@link Config configs} in this plugin's JAR
    */

   public void registerConfigs() {
      registerSubclassLoader(Config.class, this::registerConfig);
   }

   /**
    * @return The config with the identifier
    */

   @NonNull
   public Config getConfig(@NonNull String identifier) {
      return configManager.getConfig(identifier);
   }
   
   /**
    * Registers a {@link SubclassLoader} that looks for the specified super class and runs the action.
    *
    * @see SubclassLoader
    */
   
   public <T> void registerSubclassLoader(Class<T> superClass, Consumer<T> action) {
      loaders.add(new SubclassLoader<>(this, new FinderDetails(getClass()), superClass, action));
   }
   
   /**
    * Registers a {@link SubclassLoader} that looks for the specified super class and runs the action.
    *
    * @see SubclassLoader
    */
   
   public <T> void registerSingleClassLoader(Class<T> superClass, Consumer<T> action) {
      loaders.add(new SingleClassLoader<>(this, superClass, action));
   }
   
   /**
    * Registers a loader.
    *
    * @see Loader
    */
   
   public void registerLoader(Loader loader) {
      loaders.add(loader);
   }
   
   /**
    * Loads all loaders of this plugin.
    * Should be called after initializing you plugin instance,
    * logger and whatever else you use in constructors of e.g.
    * {@link Config}, {@link CommandRegisterer} or other classes.
    */
   
   public void load() {
      // Loads all loaders
      for(var loader : loaders)
         loader.load();
      
      // unfinished
      //MenuAPI.load(plugin);
   }
   
   /**
    * @return A {@link JavaPlugin} that is loaded on this server.
    * @throws NullPointerException if there is no plugin with this name on the server.
    * @throws IllegalArgumentException if {@link PluginManager#getPlugin PluginManager.getPlugin(name)}
    *         returns a different instance of {@link Plugin} that {@link JavaPlugin}.
    */
   
   @NonNull
   protected static JavaPlugin getJavaPlugin(@NonNull String name) {
      Plugin base = pluginManager.getPlugin(name);
      
      Checker.checkNonNull(base, "Plugin " + name + " is not active on this server!");
      
      if(base instanceof final JavaPlugin plugin)
         return plugin;
      
      throw new IllegalArgumentException("Plugin " + name + " is not a JavaPlugin, but an instance of: " + base.getClass().getSimpleName());
   }
   
   /**
    * @return An {@link OnteyPlugin} that is loaded on this server.
    * @throws NullPointerException if there is no plugin with this name on the server.
    * @throws IllegalArgumentException if {@link PluginManager#getPlugin PluginManager.getPlugin(name)}
    *         returns a different instance of {@link Plugin} that {@link OnteyPlugin}.
    */
   
   @NonNull
   protected static OnteyPlugin getOnteyPlugin(@NonNull String name) {
      JavaPlugin base = getJavaPlugin(name);
      
      if(base instanceof final OnteyPlugin plugin)
         return plugin;
      
      throw new IllegalArgumentException("Plugin " + name + " is not a JavaPlugin, but an instance of: " + base.getClass().getSimpleName());
   }
}
