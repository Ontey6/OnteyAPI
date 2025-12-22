# OnteyAPI
Especially made for Ontey's plugins, don't expect support if you use it yourself.

An API that upon loaded, directly hooks into your plugin and provides a base that holds many static fields.
You could say, the API is part of your plugin.

# Compatibility
- Minecraft Server: Paper (or higher fork)
- Java version: Java 21
- Build System: Gradle only

# Setup (Required)
- Add dependency
- Shading
- Configure Paperweight Userdev
- Creating an OnteyPlugin

All gradle code here is meant for the `build.gradle` file.

## Dependency
Simply use `jitpack`

in `repositories` section
```kts
maven {
    url = "https://jitpack.io"
}
```

in `dependencies` section
```kts
implementation("com.github.Ontey6:OnteyAPI:VERSION")
```

## Shading
Especially here this is very important as many things are static
for easy access, so not shading could make two plugins collide
and make one of them simply use the other one's provided resources.

in `plugins` section
```kts
id("com.gradleup.shadow") version "9.3.0"
```

After `repositories` (important)
```kts
repositories {
    //...
}

tasks.shadowJar {
    // this replacement name is just a preset, change to you liking
    relocate("com.ontey.api", "com.ontey.<YOUR_PLUGIN_NAME>.lib.api")

    destinationDirectory = file("...")
    archiveFileName = "...-${version}.jar"
}

dependencies {
    //...
}
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

Since `OnteyPlugin` extends `JavaPlugin`, no code will now not work anymore (Except maybe inner-class use of the `JavaPlugin#logger` field)

**PRO TIP**: if you need a custom Logger name, use `load(String name)` instead of plain `load()`

## Paperweight Userdev
Since this API uses paper's NMS provider called paperweight userdev, and the overall
situation with custom servers like paper is very obscure, you have to re-obfuscate
your plugin jar, which can be easily done with a gradle plugin.

in `plugins`
```kts
id("io.papermc.paperweight.userdev") version("2.0.0-beta.19")
```

in `dependencies`
```kts
paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
```

Also, to build your jar you now need to use paperweight's `reobfJar` task instead of just `build`.

## Finished
And then you are finished with the setup and can start making your plugin.