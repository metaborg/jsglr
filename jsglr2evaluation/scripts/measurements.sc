import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._

import $file.spoofax, spoofax._

println("Executing measurements...")

mkdir! Suite.measurementsDir

suite.languages.foreach { language =>
    println(" " + language.name)

    if (language.sources.batch.isEmpty)
        println("  Skipped, because there are no batch sources")
    else
        timed("measure " + language.id) {
            %%(
                "mvn",
                "exec:java",
                "-Dexec.suite=\""+
                    s"language=${language.id} " +
                    s"extension=${language.extension} " +
                    s"parseTablePath=${language.parseTablePath} " +
                    s"sourcePath=${language.sourcesDir / "batch"} " +
                    s"type=multiple" +
                "\"",
                "-DreportPath=" + language.measurementsDir,
                MAVEN_OPTS="-Xmx8G -Xss64M"
            )(suite.spoofaxDir / "jsglr" / "org.spoofax.jsglr2.measure")
        }
}
