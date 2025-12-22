# OnteyAPI
Especially made for Ontey's plugins, don't expect support if you use it yourself.

An API that upon loaded, directly hooks into your plugin and provides a base that holds many static fields.
You could say, the API is part of your plugin.

# Setup (Required)
- Add dependency
- Shading
- Creating an OnteyPlugin

# Dependency
Simply use `jitpack`

### Gradle (Should work groovy + kts)

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

### Maven
in `repositories` section
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

in `dependencies` section
```xml
<dependency>
    <groupId>com.github.Ontey6</groupId>
    <artifactId>OnteyAPI</artifactId>
    <version>VERSION</version>
</dependency>
```

# Shading
Especially here this is very important as many things are static
for easy access, so not shading could make two plugins collide
and make one of them simply use the other one's provided resources.

### Gradle
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
    // this is just a preset, change to you liking
    relocate("com.ontey.api", "com.ontey.<YOUR PLUGIN NAME>.lib.api")

    destinationDirectory = file("...")
    archiveFileName = "...-${version}.jar"
}

dependencies {
    //...
}
```

### Maven

Before `repositories` (important)
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.6.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>

                        <!-- this is just a preset, change to your liking -->
                        <relocations>
                            <relocation>
                                <pattern>com.ontey.api</pattern>
                                <shadedPattern>
                                    com.ontey.&lt;YOUR_PLUGIN_NAME&gt;.lib.api
                                </shadedPattern>
                            </relocation>
                        </relocations>

                        <outputDirectory>...</outputDirectory>
                        <finalName>...-${project.version}</finalName>

                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>

<repositories>
    <!-- ... -->
</repositories>

<dependencies>
    <!-- ... -->
</dependencies>
```

# Creating the Plugin
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

**PRO TIP**: if you need a custom Logger name, use `load(String name)` instead of plain `load()`

# Finished
And then you are finished with the setup and can start making your plugin.