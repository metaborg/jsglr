import org.metaborg.convention.Person
import org.metaborg.convention.MavenPublishConventionExtension

// Workaround for issue: https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("org.metaborg.convention.root-project")
    alias(libs.plugins.gitonium)
    alias(libs.plugins.spoofax.gradle.langspec) apply false
}

rootProjectConvention {
    // Add `publishAll` and `publish` tasks that delegate to the subprojects and included builds.
    registerPublishTasks.set(true)
}

allprojects {
    apply(plugin = "org.metaborg.gitonium")

    // Configure Gitonium before setting the version
    gitonium {
        mainBranch.set("master")
    }

    version = gitonium.version
    group = "org.metaborg.devenv"

    pluginManager.withPlugin("org.metaborg.convention.maven-publish") {
        extensions.configure(MavenPublishConventionExtension::class.java) {
            repoOwner.set("metaborg")
            repoName.set("jsglr")

            metadata {
                inceptionYear.set("2005")
                developers.set(listOf(
                    Person("jasperdenkers", "Jasper Denkers", "jasperdenkers@gmail.com"),
                    Person("mpsijm", "Maarten Sijm", "mpsijm@gmail.com"),
                    Person("karltk", "Karl Trygve Kalleberg", "karltk@gmail.com"),
                    Person("lennartcl", "Lennart Kats", "lclkats@gmail.com"),
                    Person("maartje", "Maartje de Jonge", "maartjedejonge@gmail.com"),
                    Person("Gohla", "Gabriel Konat", "gabrielkonat@gmail.com"),
                    Person("udesou", "Eduardo Souza", "eduardo7.ufv@gmail.com"),
                ))
            }
        }
    }
}
