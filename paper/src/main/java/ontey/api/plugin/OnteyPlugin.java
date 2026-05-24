package ontey.api.plugin;

import io.papermc.paper.plugin.configuration.PluginMeta;
import lombok.Getter;
import lombok.NonNull;
import ontey.api.check.Checker;
import ontey.api.check.Nullity;
import ontey.api.classfinder.FinderDetails;
import ontey.api.command.Command;
import ontey.api.command.ConfigCommand;
import ontey.api.command.config.CommandConfig;
import ontey.api.command.registry.CommandRegistry;
import ontey.api.command.registry.RegistryCommand;
import ontey.api.config.yaml.file.YamlFile;
import ontey.api.filelog.FileLog;
import ontey.api.loader.AutoRegistered;
import ontey.api.loader.Loaders;
import ontey.api.log.NamedLogger;
import ontey.api.plugin.config.ConfigManager;
import ontey.api.scheduler.OnteyScheduler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class OnteyPlugin extends JavaPlugin {
	
	/**
	 * The plugin manager of the server.
	 * Can tell you info about, query, enable/disable plugins and more.
	 *
	 * @see PluginManager
	 */
	
	@Getter
	@NonNull
	private static final PluginManager pluginManager = Bukkit.getPluginManager();
	
	/**
	 * The service(s) manager of the server.
	 * Used to register instances of your plugin's classes and provide them to other plugins which use your plugin's API.
	 */
	
	@Getter
	@NonNull
	private static final ServicesManager servicesManager = Bukkit.getServicesManager();
	
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
	 * The {@link FileLog} of this plugin.
	 * Used to save stack-traces of {@linkplain Throwable exceptions} to files.
	 */
	
	@Getter
	@NonNull
	private final FileLog fileLog = new FileLog(log, getDataFolder());
	
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
	 * The Scheduler of this plugin.
	 * Used to run tasks on the server.
	 */
	
	@Getter
	@NonNull
	private final OnteyScheduler scheduler = new OnteyScheduler(this);
	
	/**
	 * The command-file of this plugin.
	 * Used to store all {@link ConfigCommand ConfigCommands}
	 */
	
	@Getter
	@NonNull
	private final YamlFile commandsConfig = configManager.registerConfig("commands");
	
	/**
	 * The loaders of this plugin.
	 */
	
	@Getter
	@NonNull
	private final Set<Runnable> loaders = new HashSet<>();
	
	public OnteyPlugin() {
		this(true);
	}
	
	public OnteyPlugin(boolean loadDefaultLoaders) {
		registerSerializable(CommandConfig.class);
		
		if(loadDefaultLoaders) {
			registerCommands();
			registerSerializables();
			registerListeners();
			// unfinished
			//registerEvent(new MenuClickListener());
		}
	}
	
	/**
	 * @return A {@link JavaPlugin} that is loaded on this server.
	 * @throws NullPointerException if there is no plugin with this name on the server.
	 * @throws IllegalArgumentException if {@link PluginManager#getPlugin PluginManager.getPlugin(name)}
	 * returns a different instance of {@link Plugin} that {@link JavaPlugin}.
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
	 * returns a different instance of {@link Plugin} that {@link OnteyPlugin}.
	 */
	
	@NonNull
	protected static OnteyPlugin getOnteyPlugin(@NonNull String name) {
		JavaPlugin base = getJavaPlugin(name);
		
		if(base instanceof final OnteyPlugin plugin)
			return plugin;
		
		throw new IllegalArgumentException("Plugin " + name + " is not a JavaPlugin, but an instance of: " + base.getClass().getSimpleName());
	}
	
	/**
	 * Registers a {@link ConfigurationSerializable} in the {@link ConfigurationSerialization}.
	 */
	
	public static void registerSerializable(Class<? extends ConfigurationSerializable> clazz) {
		ConfigurationSerialization.registerClass(clazz);
	}
	
	/**
	 * Registers a {@link ConfigurationSerializable} in the {@link ConfigurationSerialization}.
	 */
	
	public static void registerSerializable(ConfigurationSerializable serializable) {
		registerSerializable(serializable.getClass());
	}
	
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
	
	public void registerListener(@NonNull Listener listener) {
		pluginManager.registerEvents(listener, this);
	}
	
	/**
	 * Registers all {@link Listener listeners} in this plugin's JAR.
	 */
	
	public void registerListeners() {
		registerSubclassLoader(Listener.class, clazz -> {
			if(!clazz.isAnnotationPresent(AutoRegistered.class))
				return;
			
			try {
				registerListener(clazz.getConstructor().newInstance());
			} catch(Exception e) {
				getLog().warn("Listener class " + clazz.getName() + " failed to auto-register");
			}
		});
	}
	
	public void registerCommand(RegistryCommand command) {
		commandRegistry.register(command);
	}
	
	public void registerCommand(Command command) {
		registerCommand(command.build());
	}
	
	/**
	 * Registers all {@link Command}s in this plugin's JAR that are {@link AutoRegistered}.
	 */
	
	public void registerCommands() {
		registerSubclassLoader(Command.class, clazz -> {
			if(!clazz.isAnnotationPresent(AutoRegistered.class) || clazz.equals(ConfigCommand.class))
				return;
			
			try {
				registerCommand(clazz.getConstructor().newInstance());
			} catch(InstantiationException | IllegalAccessException | InvocationTargetException |
			        NoSuchMethodException e) {
				throw new RuntimeException("Command " + clazz.getName() + " could not be auto-registered", e);
			}
		});
	}
	
	/**
	 * Registers a config.
	 *
	 * @return The registered config
	 */
	
	@NonNull
	public YamlFile registerConfig(@NonNull YamlFile config) {
		return configManager.registerConfig(config);
	}
	
	/**
	 * Registers a config with the given identifier.
	 * <br>
	 * Example: {@code config.yml}
	 *
	 * @return The registered config
	 */
	
	@NonNull
	public YamlFile registerConfig(@NonNull @ConfigManager.IdentifierPattern String identifier, @NonNull String path) {
		return configManager.registerConfig(identifier, path);
	}
	
	/**
	 * Registers a config with a path.
	 * <br>
	 * Example: {@code language, en_us} --> {@code Path.of(language, en_us.yml)}
	 *
	 * @return The registered config
	 * @deprecated - Use {@link #registerConfig(String, String)} instead
	 */
	
	@NonNull
	@Deprecated
	public YamlFile registerConfig(@ConfigManager.IdentifierPattern String first, @NonNull String @NonNull ... path) {
		return registerConfig(first, String.join("/", path));
	}
	
	/**
	 * @return The config with the identifier
	 */
	
	@NonNull
	public YamlFile getConfig(@NonNull String identifier) {
		return configManager.getConfig(identifier);
	}
	
	/**
	 * Registers a loader that registers all {@link ConfigurationSerializable} in the {@link ConfigurationSerialization}.
	 */
	
	public void registerSerializables() {
		registerSubclassLoader(ConfigurationSerializable.class, OnteyPlugin::registerSerializable);
	}
	
	/**
	 * Registers a loader.
	 */
	
	public void registerLoader(Runnable loader) {
		loaders.add(loader);
	}
	
	public <T> void registerSubclassLoader(Class<T> clazz, Consumer<Class<? extends T>> action) {
		registerLoader(Loaders.createSubclassLoader(new FinderDetails(this.getClass()), clazz, action));
	}
	
	/**
	 * Loads all loaders of this plugin.
	 * Should be called after initializing you plugin instance,
	 * logger and whatever else you use in constructors of e.g.
	 * {@link Command} or other classes.
	 */
	
	public void load() {
		for(var loader : loaders)
			loader.run();
	}
}
