# YAPGP - Yet Another PlantUML Gradle Plugin

This plugin allows you to convert [PlantUML](https://plantuml.com) (_.puml_) files into one of the two supported output formats:
* _.svg_
* _.png_

The main difference between YAPGP and other PlantUML plugins is that it does not require the PlantUML dependency,
instead, it calls a web renderer service to render the PlantUML diagrams and saves them as files.

This plugin is based on https://github.com/red-green-coding/plantuml-gradle-plugin

## Adding the plugin

### Kotlin

Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):
```kotlin
plugins {
    id("tech.kocel.yapgp") version "x.y.z"
}
```

Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):
````kotlin
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("tech.kocel:yapgp:x.y.z")
  }
}

apply(plugin = "tech.kocel.yapgp")
````
### Groovy

Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):
```groovy
plugins {
    id "tech.kocel.yapgp" version "x.y.z"
}
```

Using [legacy plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):
````groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "tech.kocel:yapgp:x.y.z"
  }
}

apply plugin: "tech.kocel.yapgp"
````

## Plugin usage

After [adding the plugin](#adding-the-plugin) to your build you can configure the plugin using the extension block `plantuml` 

Check the supported output formats [here](https://github.com/plantuml/plantuml/blob/master/src/net/sourceforge/plantuml/FileFormat.java#L64).

### Kotlin

```kotlin
plantuml {
    options {
        // where should the .svg be generated to (defaults to build/plantuml)
        outputDir = project.file("svg")
        
        // output format (lowercase, defaults to svg)
        format = "svg"
    }

    diagrams {
        create("File1") {
            // .puml sourcefile, this can be also omitted and defaults to _<name>.puml_.
            sourceFile = project.file("doc/File1.puml")
        }

        // this will just look for the file File2.puml
        create("File2")

        // add additional files here
    }
}
```

### Groovy

```groovy
plantuml {
    options {
        // where should the .svg be generated to (defaults to build/plantuml)
        outputDir = project.file("svg")

        // output format (lowercase, defaults to svg)
        format = "svg"
    }

    diagrams {
        File1 {
            // .puml sourcefile, this can be also omitted and defaults to _<name>.puml_.
            sourceFile = project.file("doc/File1.puml")
        }

        // this will just look for the file File2.puml
        File2

        // add additional files here
    }
}
```
