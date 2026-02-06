# OnteyAPI
Especially made for Ontey's plugins, don't expect support if you use it yourself.

An API that provides many utils in many areas of plugin development and general Java.

# Compatibility
- Minecraft Server: Paper (or higher fork)
- Java Version: Java 21 (JDK 21.0.5)
- Minecraft Version: 1.21+
- paper-plugin.yml instead of plugin.yml

# Setup (Required)
- Add dependency
- Create an OnteyPlugin

All Gradle code here is meant for the `build.gradle` file.

## Dependency (Modern way)
This uses paper's `PluginLoader`, which is experimental at the time and has the following bug:
You can't use the plugins static fields in other classes as they are always null when called from there.
Very weird bug, I know, so this way is not recommended right now.

### Compile only
needed for your code to compile.
Isn't inside your JAR and doesn't require shading.

`repositories`
```kts
maven {
    url = "https://jitpack.io"
}
```

`dependencies`
```kts
compileOnly("com.github.Ontey6:OnteyAPI:1.3.2")
```

### Runtime
This is where the actual JAR is downloaded.
Use Paper's `PluginLoader` for this.

NOTE: You can not download the API with spigot's `library` feature in
plugin.yml, as that would require OnteyAPI to be available on MavenCentral.

`paper-plugin.yml`
```yaml
loader: path.to.your.CustomPluginLoader
```

`CustomPluginLoader.java`
<details>
<summary>imports</summary>

```java
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;
```
</details>

```java
public class CustomPluginLoader implements PluginLoader {
   
   @Override
   public void classloader(@NotNull PluginClasspathBuilder classpath) {
      MavenLibraryResolver resolver = new MavenLibraryResolver();
      
      resolver.addDependency(new Dependency(new DefaultArtifact("com.github.Ontey6:OnteyAPI:1.3"), null));
      resolver.addRepository(new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io").build());
      
      classpath.addLibrary(resolver);
   }
}
```

## Dependency (safe way)
This uses the Gradle to include the code in the JAR.

`repositories`
```kts
maven {
    url = "https://jitpack.io"
}
```

`dependencies`
```kts
implementation("com.github.Ontey6:OnteyAPI:1.3.2")
```

## Creating the Plugin
You will probably already have a Main class that extends `JavaPlugin`.

Simple change that class to:
- extend `OnteyPlugin` instead of `JavaPlugin`
- add `load()` at first place in your `onLoad()` or `onEnable()` method

```java
public final class Main extends OnteyPlugin {
   
   @Override
   public void onLoad() {
      load();
      
      //...
   }
}
```

Since `OnteyPlugin` extends `JavaPlugin`, no code will now not work anymore.

## Finished
And then you are finished with the setup and can start making your plugin.

# Some neat features
- ## More `getPlugin` methods in OnteyPlugin
  - `getJavaPlugin` returns a JavaPlugin (if possible, otherwise throws)
  - `getOnteyPlugin` returns an OnteyPlugin (if possible, otherwise throws)
- Show a warning when your plugin JAR is not run by paper but a user with `NoAccessUtils.exit`