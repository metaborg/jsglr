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
        mkdir! benchmarksDirANTLR

        def benchmark(name: String, resultsPath: Path, sourcePath: Path, cardinality: String, testSetArgs: Seq[String], params: Map[String, String] = Map.empty) =
            %%(
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
            )(benchmarksMvnDir)

        def benchmarkJSGLR(resultsPath: Path, sourcePath: Path, cardinality: String, params: Map[String, String] = Map.empty) =
            benchmark(
                "JSGLR2BenchmarkExternal",
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

        timed(s"benchmark [batch] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
            println(benchmarkJSGLR(language.benchmarksPath, language.sourcesDir, "multiple"), Map("implode" -> "true"))
        }

        language.antlrBenchmark.foreach { antlrBenchmark =>
            timed(s"benchmark [batch/ANTLR] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
                println(benchmarkANTLR(antlrBenchmark, language.benchmarksPathANTLR, language.sourcesDir, "multiple"))
            }
        }

        timed(s"benchmark [per file] (w: $warmupIterations, i: $benchmarkIterations) " + language.id) {
            val files = ls.rec! language.sourcesDir

            mkdir! (Args.benchmarksDir / language.id)

            files.filterNot(_.last.toString.startsWith(".")).foreach { file =>
                benchmarkJSGLR(language.benchmarksPath(file.last.toString), file, "single", Map("variant" -> "standard"))
            }
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(execBenchmarks(_))