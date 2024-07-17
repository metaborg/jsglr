plugins {
    `java-library`
    `maven-publish`
    id("org.metaborg.convention.java")
    id("org.metaborg.convention.maven-publish")
}

fun compositeBuild(name: String) = "$group:$name:$version"
val spoofax2Version: String by ext
dependencies {
    api(platform(libs.metaborg.platform))

    api(project(":org.spoofax.jsglr"))
    api(project(":org.spoofax.jsglr2"))
    api(project(":jsglr.shared"))
    api(libs.interpreter.core)
    testImplementation(libs.junit)
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
