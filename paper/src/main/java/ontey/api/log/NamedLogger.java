package ontey.api.log;

import lombok.NonNull;
import lombok.Setter;

import java.util.logging.Logger;

/**
 * A Logger designed to be simple.
 * Only covers a few logging methods.
 */

public class NamedLogger {
	
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	
	@NonNull
	private final String prefix;
	
	/**
	 * Whether {@link #debug(String...) debug output} is enabled.
	 */
	
	@Setter
	private boolean debug = false;
	
	public NamedLogger(@NonNull String name) {
		this.prefix = "[" + name + "]";
	}
	
	/**
	 * Whether {@link #debug(String...) debug output} is enabled.
	 */
	
	public boolean getDebug() {
		return debug;
	}
	
	/**
	 * Prints message(s) with log level = Info.
	 * Used for general logging.
	 */
	
	public void info(@NonNull String @NonNull ... messages) {
		for(String message : messages)
			LOGGER.info(prefix + " " + message);
	}
	
	/**
	 * Prints message(s) with log level = Warning.
	 * Used for warnings, e.g. when a recommended (not required) plugin is missing.
	 */
	
	public void warn(@NonNull String @NonNull ... messages) {
		for(String message : messages)
			LOGGER.warning(prefix + " " + message);
	}
	
	/**
	 * Prints message(s) with log level = Severe.
	 * Used for errors, e.g. when a required plugin is missing.
	 */
	
	public void error(@NonNull String @NonNull ... messages) {
		for(String message : messages)
			LOGGER.severe(prefix + " " + message);
	}
	
	/**
	 * Prints message(s) with log level = Info if {@link #debug} is enabled.
	 * Used for debug logging,
	 */
	
	public void debug(@NonNull String @NonNull ... messages) {
		if(debug)
			for(String message : messages)
				LOGGER.info(prefix + " " + message);
	}
}
