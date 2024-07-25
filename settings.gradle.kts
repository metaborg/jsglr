rootProject.name = "jsglr-project"

dependencyResolutionManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        maven("https://artifacts.metaborg.org/content/groups/public/")
        gradlePluginPortal()
    }
}

plugins {
    id("org.metaborg.convention.settings") version "latest.integration"
}

include("org.spoofax.jsglr")
include("org.spoofax.jsglr2")
include("org.spoofax.interpreter.library.jsglr")
include("jsglr.shared")
