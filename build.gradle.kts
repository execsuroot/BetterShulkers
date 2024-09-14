import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("com.gradleup.shadow") version "8.3.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "tech.execsuroot"
version = "1.0.1"
description = "Better shulkers for your Minecraft server."

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    // Lombok
    val lombokVersion = "1.18.32"
    compileOnly("org.projectlombok", "lombok", lombokVersion)
    annotationProcessor("org.projectlombok", "lombok", lombokVersion)
    // Config
    implementation("de.exlll", "configlib-yaml", "4.5.0")
}

paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

tasks {
    runServer {
        minecraftVersion("1.21.1")
    }

    processResources {
        filesMatching("paper-plugin.yml") {
            expand("version" to project.version, "description" to project.description)
        }
    }

    create<ShadowJar>("relocatedShadowJar") {
        group = "shadow"
        from(shadowJar)
        archiveClassifier.set("relocated")

        relocate("de.exlll.configlib", "${project.group}.exlll.configlib")
        relocate("org.snakeyaml", "${project.group}.snakeyaml")
    }
}