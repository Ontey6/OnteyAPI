# OnteyAPI

An API that provides many utils in many areas of plugin development and general Java.

# Compatibility

- Minecraft Server: Paper (or higher fork)
- Java Version: Java 25 (JDK 21.0.5)
- Minecraft Version: 26.1.2 (older versions may work)
- paper-plugin.yml instead of plugin.yml is recommended

# Setup (Required)

- Add dependency
- Create an OnteyPlugin

All Gradle code here is meant for the `build.gradle` file.

## Dependency (Modern way)

In 1.21.5, there seemed to be a bug regarding the usage of paper's PluginLoader that made it unusable.
I am not sure if it was a bug or user-error and I can only test its existence when the next version is out.

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
compileOnly("com.github.Ontey6:OnteyAPI:1.3.3")
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
		
		// Add such a dependency for every module you want to use.
		// Sadly, that is currently the only way.
		resolver.addDependency(new Dependency(new DefaultArtifact("com.github.Ontey6:OnteyAPI:2.0"), null));
		resolver.addRepository(new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io").build());
		
		classpath.addLibrary(resolver);
	}
}
```

## Dependency (safe way)

This uses the Gradle to include the code in the JAR.

`plugins`

```kts
id("com.gradleup.shadow") version "9.3.0"
```

`repositories`

```kts
maven {
    url = "https://jitpack.io"
}
```

`shading`

```kts
tasks.shadowJar {
    relocate("ontey.api", "relocated.YOUR_GROUP.ontey.api")
}
```

`dependencies`

```kts
implementation("com.github.Ontey6:OnteyAPI:2.0")
```

## Creating the Plugin

You will probably already have a Main class that extends `JavaPlugin`.

Simple change that class to:

- extend `OnteyPlugin` instead of `JavaPlugin`
- add `load()` at first place in your `onLoad()` or `onEnable()` method
  (only one of them, use `onLoad()` if you have it, as it is called earlier)

```java
public final class Main extends OnteyPlugin {
	
	@Override
	public void onLoad() {
		load();
		
		//...
	}
}
```

Since `OnteyPlugin` extends `JavaPlugin`, all code will remain working (very few exceptions).

## Finished

And then you are finished with the setup and can start making your plugin.

# Some neat features

- ## More `getPlugin` methods in OnteyPlugin
    - `getJavaPlugin` returns a JavaPlugin (if possible, otherwise throws)
    - `getOnteyPlugin` returns an OnteyPlugin (if possible, otherwise throws)
- Show a warning when your plugin JAR is not run by paper but a user with `NoAccessUtils.exit`