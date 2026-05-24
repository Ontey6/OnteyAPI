package ontey.api.config.exception;

import ontey.api.config.Config;

import java.io.IOException;

/**
 * Exception thrown when attempting to load an invalid {@link Config}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/InvalidConfigurationException.java">Bukkit Source</a>
 */

public class InvalidConfigException extends IOException {
	
	/**
	 * Creates a new instance of InvalidConfigurationException without a
	 * message or cause.
	 */
	
	public InvalidConfigException() {
	}
	
	/**
	 * Constructs an instance of InvalidConfigurationException with the
	 * specified message.
	 *
	 * @param msg The details of the exception.
	 */
	
	public InvalidConfigException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs an instance of InvalidConfigurationException with the
	 * specified cause.
	 *
	 * @param cause The cause of the exception.
	 */
	
	public InvalidConfigException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs an instance of InvalidConfigurationException with the
	 * specified message and cause.
	 *
	 * @param cause The cause of the exception.
	 * @param msg The details of the exception.
	 */
	
	public InvalidConfigException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
