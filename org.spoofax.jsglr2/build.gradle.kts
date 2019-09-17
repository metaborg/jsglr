plugins {
  id("org.metaborg.gradle.config.java-library")
  id("org.metaborg.gradle.config.junit-testing")
}

dependencies {
  api(platform("org.metaborg:parent:$version"))

  api("org.metaborg:org.spoofax.terms:$version")
  api(project(":org.spoofax.jsglr"))
  compileOnly("com.google.code.findbugs:jsr305")
  api("org.metaborg:sdf2table:$version")
  testCompileOnly("junit:junit")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.1.0")
  api("org.metaborg:org.metaborg.parsetable:$version")
}

// Copy test resources into classes directory, to make them accessible as classloader resources at runtime.
val copyTestResourcesTask = tasks.create<Copy>("copyTestResources") {
  from("$projectDir/src/test/resources")
  into("$buildDir/classes/java/test")
}
tasks.getByName("processTestResources").dependsOn(copyTestResourcesTask)
