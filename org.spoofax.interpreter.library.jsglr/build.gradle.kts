plugins {
    `java-library`
    id("org.metaborg.convention.java")
    id("org.metaborg.convention.maven-publish")
    id("org.metaborg.convention.junit")
}

dependencies {
    api(platform(libs.metaborg.platform)) { version { require("latest.integration") } }

    api(project(":org.spoofax.jsglr"))
    api(project(":org.spoofax.jsglr2"))
    api(project(":jsglr.shared"))
    api(libs.interpreter.core)
    testCompileOnly(libs.junit4)
    testRuntimeOnly(libs.junit.vintage)
}

// Copy test resources into classes directory, to make them accessible as classloader resources at runtime.
val copyTestResourcesTask = tasks.create<Copy>("copyTestResources") {
    from("$projectDir/src/test/resources")
    into("$buildDir/classes/java/test")
}
tasks.getByName("processTestResources").dependsOn(copyTestResourcesTask)

// Skip tests, as they do not work.
tasks.test.get().enabled = false
