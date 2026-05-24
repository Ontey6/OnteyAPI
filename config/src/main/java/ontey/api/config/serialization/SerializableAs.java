package ontey.api.config.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an "alias" that a {@link ConfigSerializable} may be
 * stored as.
 * If this is not present on a {@link ConfigSerializable} class, it
 * will use the fully qualified name of the class.
 * <br>
 * This value will be stored in the configuration so that the configuration
 * deserialization can determine what type it is.
 * <br>
 * Using this annotation on any other class than a {@link
 * ConfigSerializable} will have no effect.
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/serialization/SerializableAs.java">Bukkit Source</a>
 * @see ConfigSerialization#registerClass(Class, String)
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializableAs {
	
	/**
	 * This is the name your class will be stored and retrieved as.
	 * <br>
	 * This name MUST be unique. We recommend using names such as
	 * "MyPluginThing" instead of "Thing".
	 *
	 * @return Name to serialize the class as.
	 */
	
	String value();
}
