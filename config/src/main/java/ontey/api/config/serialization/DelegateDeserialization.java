package ontey.api.config.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applies to a {@link ConfigSerializable} that will delegate all
 * deserialization to another {@link ConfigSerializable}.
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/serialization/DelegateDeserialization.java">Bukkit Source</a>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DelegateDeserialization {
	
	/**
	 * Which class should be used as a delegate for this classes
	 * deserialization
	 *
	 * @return Delegate class
	 */
	
	Class<? extends ConfigSerializable> value();
}
