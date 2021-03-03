import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    // Kotlinx serialization for any data format
    kotlin("plugin.serialization") version "1.4.21"
    // Shade the plugin
    id("com.github.johnrengelman.shadow") version "6.1.0"
    // Add maven support
    maven
    // Allow publishing
    `maven-publish`

    // Apply the application plugin to add support for building a jar
    java
    // Dokka documentation w/ kotlin
    id("org.jetbrains.dokka") version "1.4.20"
}

repositories {
    // Use jcenter for resolving dependencies.
    jcenter()

    // Use mavenCentral
    maven(url = "https://repo1.maven.org/maven2/")
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://libraries.minecraft.net")
    maven(url = "https://jitpack.io")
    maven(url = "https://jcenter.bintray.com/")
}

dependencies {
    // Align versions of all Kotlin components
    compileOnly(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    compileOnly(kotlin("stdlib"))

    // Use the Kotlin reflect library.
    compileOnly(kotlin("reflect"))

    // Use the JUpiter test library.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")

    // Compile Minestom into project
    compileOnly("com.github.Minestom:Minestom:5ffd44449d")

    // implement KStom
    compileOnly("com.github.Project-Cepi:KStom:6d054839bf")

    // import kotlinx serialization
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    // Add levelExtension
    compileOnly("com.github.Project-Cepi:LevelExtension:cfcbcd8bf7")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("item")
        mergeServiceFiles()
        minimize()

    }

    test { useJUnitPlatform() }

    build { dependsOn(shadowJar) }

}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "11" }

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_OWNER")}/${System.getenv("GITHUB_REPO")}") // Github Package
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
    from("LICENCE.md") {
        into("META-INF")
    }
}

val dokkaJavadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.get().outputDirectory.get())
    archiveClassifier.set("javadoc")
}



publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJavadocJar)

            pom {
                packaging = "jar"
                name.set(rootProject.name)
                description.set("An Example extension for Minestom")
                url.set("https://github.com/${System.getenv("GITHUB_OWNER")}/${System.getenv("GITHUB_REPO")}")
                scm {
                    url.set("https://github.com/${System.getenv("GITHUB_OWNER")}/${System.getenv("GITHUB_REPO")}")
                }
                issueManagement {
                    url.set("https://github.com/${System.getenv("GITHUB_OWNER")}/${System.getenv("GITHUB_REPO")}/issues")
                }
                licenses {
                    license {
                        name.set("MIT License") // Change when needed
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set(System.getenv("GITHUB_OWNER"))
                        name.set(System.getenv("GITHUB_OWNER"))
                    }
                }
            }

        }
    }
}