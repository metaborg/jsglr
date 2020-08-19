plugins {
  id("org.metaborg.gradle.config.root-project") version "0.3.21"
  id("org.metaborg.gitonium") version "0.1.3"
}

subprojects {
  metaborg {
    configureSubProject()
  }
}

allprojects {
  version = "2.6.0-SNAPSHOT" // Override version from Git, as Spoofax Core uses a different versioning scheme.
}
