package ontey.api.serialization;

/**
 * Combines the configuration-serialization classes of bukkit and OnteyAPI's config module.
 *
 * @see org.bukkit.configuration.serialization.ConfigurationSerializable
 * @see ontey.api.config.serialization.ConfigSerializable
 */

public interface ConfigSerializable extends
  org.bukkit.configuration.serialization.ConfigurationSerializable,
  ontey.api.config.serialization.ConfigSerializable {
	
}
