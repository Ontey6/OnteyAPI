package ontey.api.classfinder;

import lombok.NonNull;
import ontey.api.check.TryCatch;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * Specifies the source and optionally, the class-loader for a ClassFinder operation.
 */

public record FinderDetails(@NonNull JarFile source, @Nullable ClassLoader loader) {
	
	public FinderDetails(@NonNull Class<?> source, @Nullable ClassLoader loader) {
		this(getJar(source), loader);
	}
	
	public FinderDetails(@NonNull JarFile source) {
		this(source, null);
	}
	
	public FinderDetails(@NonNull Class<?> source) {
		this(source, null);
	}
	
	/**
	 * @return Gets the {@link JarFile} the {@code source} is located in
	 * @throws CodeSourceLocatorException If an exception is thrown while trying to find the {@link JarFile}.
	 */
	
	private static JarFile getJar(@NonNull Class<?> source) throws CodeSourceLocatorException {
		try {
			var pd = source.getProtectionDomain();
			if(pd == null || pd.getCodeSource() == null)
				throw new IOException("Cannot determine code source for " + source.getName());
			
			URL location = pd.getCodeSource().getLocation();
			URI uri = location.toURI();
			
			File file = new File(uri);
			
			if(file.isDirectory())
				throw new IOException("Class was loaded from a directory, not a jar: " + file);
			
			return new JarFile(file);
		} catch(IOException | URISyntaxException e) {
			throw new CodeSourceLocatorException("Internal Error (probably) in the ClassFinder", e);
		}
	}
	
	/**
	 * @return the class-loader. If none was specified, returns a new
	 * {@link URLClassLoader} that loads the classes in the source's JAR.
	 */
	
	@Override
	@NonNull
	public ClassLoader loader() {
		if(loader != null)
			return loader;
		
		return TryCatch.wrapExceptions(
		  IllegalStateException::new,
		  () ->
			 new URLClassLoader(new URL[] {new File(source.getName()).toURI().toURL()}, getClass().getClassLoader())
		);
	}
}
