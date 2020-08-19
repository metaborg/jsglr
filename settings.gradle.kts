rootProject.name = "jsglr"

pluginManagement {
  repositories {
    maven("https://artifacts.metaborg.org/content/groups/public/")
  }
}

if(org.gradle.util.VersionNumber.parse(gradle.gradleVersion).major < 6) {
  enableFeaturePreview("GRADLE_METADATA")
}

include("org.spoofax.jsglr")
include("org.spoofax.jsglr2")
include("org.spoofax.interpreter.library.jsglr")
