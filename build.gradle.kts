import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.jetbrains.dokka") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.0"
    `maven-publish`

    // Apply the application plugin to add support for building a jar
    java
}

repositories {
    // Use mavenCentral
    mavenCentral()

    maven(url = "https://jitpack.io")
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    // Align versions of all Kotlin components
    compileOnly(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK standard library.
    compileOnly(kotlin("stdlib"))

    // Use the Kotlin reflect library.
    compileOnly(kotlin("reflect"))

    // Use the JUpiter test library.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // Compile Minestom into project
    compileOnly("com.github.Minestom", "Minestom", "4ee5cbe424")

    // implement KStom
    compileOnly("com.github.Project-Cepi:KStom:05b5e1f2a1")

    // add energy
    compileOnly("com.github.Project-Cepi:EnergyExtension:f2bcdfc79e")

    // import kotlinx serialization
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

    // Add Kepi
    compileOnly("com.github.Project-Cepi:Kepi:071b288ed0")

    // Add Gooey
    compileOnly("com.github.Project-Cepi:Gooey:91e5ca7944")

    // Add projectiles
    compileOnly("com.github.Project-Cepi:Projectiles:8191a5b6f8")

    // Add mobs
    compileOnly("com.github.Project-Cepi:MobExtension:0ed0fc125e")

    // Add actions
    compileOnly("com.github.Project-Cepi:Actions:3cccca74c8")

    // Add canvas
    implementation("com.mattworzala:canvas:1.1.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

// Take gradle.properties and apply it to resources.
tasks {
    processResources {
        // Apply properties to extension.json
        filesMatching("META-INF/extension.json") {
            expand(project.properties)
        }
    }

    // Set name, minimize, and merge service files
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set(project.name)
        mergeServiceFiles()
        minimize()
    }

    withType<Test> { useJUnitPlatform() }

    // Make build depend on shadowJar as shading dependencies will most likely be required.
    build { dependsOn(shadowJar) }

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "17" }
val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.properties["group"] as? String?
            artifactId = project.name
            version = project.properties["version"] as? String?

            from(components["java"])
        }
    }
}
