import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def execBenchmarks(implicit args: Args) = {
    println("Executing benchmarks...")

    config.languages.foreach { language =>
        println(" " + language.name)

        val benchmarksMvnDir = (pwd / up / "org.spoofax.jsglr2.benchmark")

        val warmupIterations = args.iterations
        val benchmarkIterations = args.iterations

        mkdir! benchmarksDir

        def benchmark(resultsPath: Path, sourcePath: Path, cardinality: String) =
            %%(
                "java", "-jar", "target/org.spoofax.jsglr2.benchmark.jar",
                "-wi", warmupIterations,
                "-i", benchmarkIterations,
                "-f", 1,
                "-rff", resultsPath,
                "JSGLR2BenchmarkParsingExternal",
                "-jvmArgs=\"-Dlanguage=" + language.id + " " + language.extension + " " + language.parseTablePath + " " + sourcePath + " " + cardinality + "\""
            )(benchmarksMvnDir)

        timed(s"benchmark [batch] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
            benchmark(language.benchmarksPath, language.sourcesDir, "multiple")
        }

        timed(s"benchmark [per file] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
            val files = ls.rec! language.sourcesDir

            mkdir! (Args.benchmarksDir / language.id)

            files.filterNot(_.last.toString.startsWith(".")).foreach { file =>
                benchmark(language.benchmarksPath(file.last.toString), file, "single")
            }
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(execBenchmarks(_))