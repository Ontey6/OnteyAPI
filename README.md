# OnteyAPI
An API that provides many utils in many areas of plugin development and general Java.

# Compatibility
- Minecraft Server: Paper (or higher fork)
- Java Version: Java 25
- Minecraft Version: 26.1.2 (older versions may work)
- Tested On: Latest version, latest build.
- paper-plugin.yml instead of plugin.yml is recommended

# Setup (Required)
- Add dependency
- Create an OnteyPlugin

All Gradle code here is meant for the `build.gradle` file.

## Dependency
This uses the Gradle to include the code in the JAR and shade it into your own package.

Add these in this exact order, `shadowJar` has to be in between of `repositories` and `dependencies`.

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
  (only put it in one of them, use `onLoad()` if you have it, as it is called earlier)

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

# The main features
- Lightweight Command API that uses brigadier
- `OnteyPlugin` as a QoL `JavaPlugin` wrapper
- QoL Config & YAML API that are similar to Bukkit's (Huge credits to [Simple-Yaml](https://github.com/Carleslc/Simple-YAML/))
- The `ClassFinder` API that can simply find classes - even in other JARs.
