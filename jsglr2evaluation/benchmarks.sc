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

        mkdir! language.benchmarksDir

        def benchmark(name: String, resultsPath: Path, sourcePath: Path, cardinality: String, testSetArgs: Seq[String], params: Map[String, String] = Map.empty) =
            println(%%(
                Seq(
                    "java", "-jar", "target/org.spoofax.jsglr2.benchmark.jar",
                    "-wi", warmupIterations.toString,
                    "-i", benchmarkIterations.toString,
                    "-f", 1.toString,
                    "-rff", resultsPath.toString,
                    name,
                    "-jvmArgs=\"-DtestSet=" + testSetArgs.mkString(" ") + "\""
                ) ++ params.toSeq.flatMap {
                    case (param, value) => Seq("-p", s"$param=$value")
                }
            )(benchmarksMvnDir))

        def benchmarkJSGLR(name: String, resultsPath: Path, sourcePath: Path, cardinality: String, params: Map[String, String] = Map.empty) =
            benchmark(
                name,
                resultsPath,
                sourcePath,
                cardinality,
                Seq(
                    s"language=${language.id}",
                    s"extension=${language.extension}",
                    s"parseTablePath=${language.parseTablePath}",
                    s"sourcePath=${sourcePath}",
                    s"type=${cardinality}"
                ),
                params
            )

        def benchmarkANTLR(name: String, resultsPath: Path, sourcePath: Path, cardinality: String) =
            benchmark(
                name,
                resultsPath,
                sourcePath,
                cardinality,
                Seq(
                    s"language=${language.id}",
                    s"extension=${language.extension}",
                    s"sourcePath=${sourcePath}",
                    s"type=${cardinality}"
                ),
                Map.empty
            )

        timed(s"benchmark [JSGLR2/batch] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
            benchmarkJSGLR("JSGLR2BenchmarkExternal", language.benchmarksDir / "jsglr2.csv", language.sourcesDir, "multiple", Map("implode" -> "true"))
        }

        timed(s"benchmark [JSGLR1/batch] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
            benchmarkJSGLR("JSGLR1BenchmarkExternal", language.benchmarksDir / "jsglr1.csv", language.sourcesDir, "multiple", Map("implode" -> "true"))
        }

        language.antlrBenchmark.foreach { antlrBenchmark =>
            timed(s"benchmark [ANTLR/batch] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
                benchmarkANTLR(antlrBenchmark, language.benchmarksDir / "antlr.csv", language.sourcesDir, "multiple")
            }
        }

        timed(s"benchmark [JSGLR2/perFile] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
            val files = ls.rec! language.sourcesDir sortBy(-_.size)
            val fileCount = files.size
            val samples = 10
            val step = fileCount / samples

            mkdir! (language.benchmarksDir / "perFile")

            for (i <- 0 until samples) {
                val file = files(i * step)

                benchmarkJSGLR("JSGLR2BenchmarkExternal", language.benchmarksDir / "perFile" / s"${file.last.toString}.csv", file, "single", Map("variant" -> "standard"))
            }
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(execBenchmarks(_))