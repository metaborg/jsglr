plugins {
    id("org.metaborg.gradle.config.java-library")
    id("org.metaborg.gradle.config.junit-testing")
}

fun compositeBuild(name: String) = "$group:$name:$version"
val spoofax2Version: String by ext
dependencies {
    api(platform("org.metaborg:parent:$spoofax2Version"))

    api(compositeBuild("org.spoofax.terms"))
    api(compositeBuild("jsglr.shared"))
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation(compositeBuild("sdf2table"))
    testCompileOnly("junit:junit")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.7.0")
    api(compositeBuild("org.metaborg.parsetable"))
}

// Copy test resources into classes directory, to make them accessible as classloader resources at runtime.
val copyTestResourcesTask = tasks.create<Copy>("copyTestResources") {
    from("$projectDir/src/test/resources")
    into("$buildDir/classes/java/test")
}
tasks.getByName("processTestResources").dependsOn(copyTestResourcesTask)
