plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "tech.kocel"
version = "0.1.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation("commons-codec:commons-codec:1.16.1")

    val kotestVersion = "5.8.1"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}

gradlePlugin {
    plugins {
        create("plantuml") {
            id = "tech.kocel.yapgp"
            displayName = "Yet Another PlantUML Gradle Plugin - YAPGP"
            tags = listOf("plantuml", "puml", "svg")
            website = "https://github.com/kkocel/yapgp"
            vcsUrl = "https://github.com/kkocel/yapgp.git"
            description = "Converts PlantUML .puml files to one of the supported output formats - svg, png, txt. " +
                "It sends the .puml file to the PlantUML server and saves the result."
            implementationClass = "tech.kocel.yapgp.PlantumlGradlePlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            artifactId = "yapgp"
        }
    }

    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../build/local-plugin-repository")
        }
    }
}

// Add a source set for the functional test suite
val functionalTestSourceSet =
    sourceSets.create("functionalTest") {
    }

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
// Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.withType(Test::class.java) {
    testLogging.showStandardStreams = true
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        allWarningsAsErrors = true

        jvmTarget = "1.8"
        apiVersion = "1.9"
        languageVersion = "1.9"

        incremental = true
        freeCompilerArgs =
            listOf(
                "-Xjsr305=strict",
            )
    }
}

tasks {
    validatePlugins {
        enableStricterValidation.set(true)
        failOnWarning.set(true)
    }
    jar {
        from(sourceSets.main.map { it.allSource })
        manifest.attributes.apply {
            put("Implementation-Title", "Gradle Kotlin DSL (${project.name})")
            put("Implementation-Version", archiveVersion.get())
        }
    }
}
