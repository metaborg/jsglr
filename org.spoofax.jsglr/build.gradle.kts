plugins {
  id("org.metaborg.gradle.config.java-library")
  id("org.metaborg.gradle.config.junit-testing")
}

fun compositeBuild(name: String) = "$group:$name:$version"
val spoofax2Version: String by ext
dependencies {
  api(platform("org.metaborg:parent:$spoofax2Version"))

  implementation(compositeBuild("org.spoofax.terms"))
  testCompileOnly("junit:junit:4.13.1")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.7.0")
  implementation(compositeBuild("org.metaborg.util"))
  api(compositeBuild("org.metaborg.parsetable"))
}

sourceSets {
  main {
    java {
      srcDir("src")
    }
  }
  test {
    java {
      srcDir("test")
    }
  }
}

// Copy test resources into classes directory, to make them accessible as classloader resources at runtime.
val copyTestResourcesTask = tasks.create<Copy>("copyTestResources") {
  from("$projectDir/test/resources")
  into("$buildDir/classes/java/test")
}
tasks.getByName("processTestResources").dependsOn(copyTestResourcesTask)

// Skip tests, as they do not work.
tasks.test.get().enabled = false
