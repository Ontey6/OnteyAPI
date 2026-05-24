package ontey.api.classfinder;

import java.io.Serial;

/**
 * An exception thrown when an  exception occurs while finding the
 * {@link java.util.jar.JarFile} that a {@link Class} is located in.
 * Can't be created without a cause.
 */

public class CodeSourceLocatorException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = 3210856451289754231L;
	
	public CodeSourceLocatorException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CodeSourceLocatorException(Throwable cause) {
		super(cause);
	}
}
