package ontey.classfinder;

import ontey.OnteyAPI;
import ontey.check.TryCatch;
import lombok.NonNull;
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
    * @return the class-loader. If none was specified, returns a new
    * {@link URLClassLoader} that loads the classes in the source's JAR.
    */
   
   @Override
   @NonNull
   public ClassLoader loader() {
      if(loader != null)
         return loader;
      
      return TryCatch.wrapExceptions(() ->
        new URLClassLoader(new URL[] {new File(source.getName()).toURI().toURL()}, OnteyAPI.class.getClassLoader())
      );
   }
   
   private static JarFile getJar(@NonNull Class<?> source) {
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
         throw new RuntimeException("Internal Error (probably) in the ClassFinder", e);
      }
   }
}
