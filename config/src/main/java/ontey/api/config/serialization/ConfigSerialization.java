package ontey.api.config.serialization;

import lombok.NonNull;
import ontey.api.config.Config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for storing and retrieving classes for {@link Config}.
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/serialization/ConfigurationSerialization.java">Bukkit Source</a>
 */

public class ConfigSerialization {
	
	public static final String SERIALIZED_TYPE_KEY = "==";
	
	private static final Map<String, Class<? extends ConfigSerializable>> aliases = new HashMap<>();
	
	private final Class<? extends ConfigSerializable> clazz;
	
	protected ConfigSerialization(Class<? extends ConfigSerializable> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Attempts to deserialize the given arguments into a new instance of the
	 * given class.
	 * <br>
	 * The class must implement {@link ConfigSerializable}, including
	 * the extra methods as specified in the Javadoc of
	 * ConfigurationSerializable.
	 * <br>
	 * If a new instance could not be made, an example being the class not
	 * fully implementing the interface, null will be returned.
	 *
	 * @param args Arguments for deserialization
	 * @param clazz Class to deserialize into
	 * @return New instance of the specified class
	 */
	
	public static ConfigSerializable deserializeObject(Map<String, ?> args, Class<? extends ConfigSerializable> clazz) {
		return new ConfigSerialization(clazz).deserialize(args);
	}
	
	/**
	 * Attempts to deserialize the given arguments into a new instance of the
	 * given class.
	 * <br>
	 * The class must implement {@link ConfigSerializable}, including
	 * the extra methods as specified in the Javadoc of
	 * ConfigurationSerializable.
	 * <br>
	 * If a new instance could not be made, an example being the class not
	 * fully implementing the interface, null will be returned.
	 *
	 * @param args Arguments for deserialization
	 * @return New instance of the specified class
	 */
	
	public static ConfigSerializable deserializeObject(Map<String, ?> args) {
		Class<? extends ConfigSerializable> clazz;
		
		if(args.containsKey(ConfigSerialization.SERIALIZED_TYPE_KEY)) {
			try {
				final String alias = (String) args.get(ConfigSerialization.SERIALIZED_TYPE_KEY);
				
				if(alias == null)
					throw new IllegalArgumentException("Cannot have null alias");
				
				clazz = ConfigSerialization.getClassByAlias(alias);
				if(clazz == null)
					throw new IllegalArgumentException("Specified class does not exist ('" + alias + "')");
			} catch(ClassCastException ex) {
				ex.fillInStackTrace();
				throw ex;
			}
		} else
			throw new IllegalArgumentException("Args doesn't contain type key ('" + ConfigSerialization.SERIALIZED_TYPE_KEY + "')");
		
		return new ConfigSerialization(clazz).deserialize(args);
	}
	
	/**
	 * Registers the given {@link ConfigSerializable} class by its
	 * alias
	 *
	 * @param clazz Class to register
	 */
	
	public static void registerClass(Class<? extends ConfigSerializable> clazz) {
		final DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
		
		if(delegate == null) {
			ConfigSerialization.registerClass(clazz, ConfigSerialization.getAlias(clazz));
			ConfigSerialization.registerClass(clazz, clazz.getName());
		}
	}
	
	/**
	 * Registers the given alias to the specified {@link
	 * ConfigSerializable} class
	 *
	 * @param clazz Class to register
	 * @param alias Alias to register as
	 * @see SerializableAs
	 */
	
	public static void registerClass(Class<? extends ConfigSerializable> clazz, String alias) {
		ConfigSerialization.aliases.put(alias, clazz);
	}
	
	/**
	 * Unregisters the specified alias to a {@link ConfigSerializable}
	 *
	 * @param alias Alias to unregister
	 */
	
	public static void unregisterClass(String alias) {
		ConfigSerialization.aliases.remove(alias);
	}
	
	/**
	 * Unregisters any aliases for the specified {@link
	 * ConfigSerializable} class
	 *
	 * @param clazz Class to unregister
	 */
	
	public static void unregisterClass(Class<? extends ConfigSerializable> clazz) {
		while(true) {
			if(!ConfigSerialization.aliases.values().remove(clazz)) break;
			// Continue unregistering remaining aliases
		}
	}
	
	/**
	 * Attempts to get a registered {@link ConfigSerializable} class by
	 * its alias
	 *
	 * @param alias Alias of the serializable
	 * @return Registered class, or null if not found
	 */
	
	public static Class<? extends ConfigSerializable> getClassByAlias(String alias) {
		return ConfigSerialization.aliases.get(alias);
	}
	
	/**
	 * Gets the correct alias for the given {@link ConfigSerializable}
	 * class
	 *
	 * @param clazz Class to get alias for
	 * @return Alias to use for the class
	 */
	
	public static String getAlias(Class<? extends ConfigSerializable> clazz) {
		DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
		
		if(delegate != null && delegate.value() != clazz) {
			return ConfigSerialization.getAlias(delegate.value());
		}
		
		final SerializableAs alias = clazz.getAnnotation(SerializableAs.class);
		
		if(alias != null) {
			return alias.value();
		}
		
		return clazz.getName();
	}
	
	public ConfigSerializable deserialize(@NonNull Map<String, ?> args) {
		ConfigSerializable result = null;
		Method method;
		
		method = this.getMethod("deserialize", true);
		
		if(method != null) {
			result = this.deserializeViaMethod(method, args);
		}
		
		if(result == null) {
			method = this.getMethod("valueOf", true);
			
			if(method != null) {
				result = this.deserializeViaMethod(method, args);
			}
		}
		
		if(result == null) {
			final Constructor<? extends ConfigSerializable> constructor = this.getConstructor();
			
			if(constructor != null) {
				result = this.deserializeViaCtor(constructor, args);
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("SameParameterValue")
	protected Method getMethod(String name, boolean isStatic) {
		try {
			final Method method = this.clazz.getDeclaredMethod(name, Map.class);
			
			if(!ConfigSerializable.class.isAssignableFrom(method.getReturnType())) {
				return null;
			}
			if(Modifier.isStatic(method.getModifiers()) != isStatic) {
				return null;
			}
			
			return method;
		} catch(NoSuchMethodException | SecurityException ex) {
			return null;
		}
	}
	
	protected Constructor<? extends ConfigSerializable> getConstructor() {
		try {
			return this.clazz.getConstructor(Map.class);
		} catch(NoSuchMethodException | SecurityException ex) {
			return null;
		}
	}
	
	protected ConfigSerializable deserializeViaMethod(Method method, Map<String, ?> args) {
		try {
			final ConfigSerializable result = (ConfigSerializable) method.invoke(null, args);
			
			if(result == null) {
				Logger.getLogger(ConfigSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method + "' of " + this.clazz + " for deserialization: method returned null");
			} else {
				return result;
			}
		} catch(Throwable ex) {
			Logger.getLogger(ConfigSerialization.class.getName()).log(
			  Level.SEVERE,
			  "Could not call method '" + method.toString() + "' of " + this.clazz + " for deserialization",
			  ex instanceof InvocationTargetException ? ex.getCause() : ex);
		}
		
		return null;
	}
	
	protected ConfigSerializable deserializeViaCtor(Constructor<? extends ConfigSerializable> ctor, Map<String, ?> args) {
		try {
			return ctor.newInstance(args);
		} catch(Throwable ex) {
			Logger.getLogger(ConfigSerialization.class.getName()).log(
			  Level.SEVERE,
			  "Could not call constructor '" + ctor.toString() + "' of " + this.clazz + " for deserialization",
			  ex instanceof InvocationTargetException ? ex.getCause() : ex);
		}
		
		return null;
	}
}
