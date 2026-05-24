package ontey.api.config.serialization;

import java.util.Map;

/**
 * Represents an object that may be serialized.
 * <br>
 * These objects MUST implement one of the following, in addition to the
 * methods as defined by this interface:
 * <ul>
 * <li>A static method "deserialize" that accepts a single {@link Map}&lt;
 * {@link String}, {@link Object} and returns the class.</li>
 * <li>A static method "valueOf" that accepts a single {@link Map}&lt;{@link
 * String}, {@link Object} and returns the class.</li>
 * <li>A constructor that accepts a single {@link Map}&lt;{@link String},
 * {@link Object}.</li>
 * </ul>
 * In addition to implementing this interface, you must register the class
 * with {@link ConfigSerialization#registerClass(Class)}.
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/serialization/ConfigurationSerializable.java">Bukkit Source</a>
 * @see DelegateDeserialization
 * @see SerializableAs
 */

public interface ConfigSerializable {
	
	/**
	 * Creates a Map representation of this class.
	 * <br>
	 * This class must provide a method to restore this class, as defined in
	 * the {@link ConfigSerializable} interface Javadocs.
	 *
	 * @return Map containing the current state of this class
	 */
	
	Map<String, Object> serialize();
}
