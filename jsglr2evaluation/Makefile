DIR=~/jsglr2evaluation
ITERATIONS=1 # warmup and benchmark iterations
SAMPLES=1 # number of sampled files for per file benchmarking
REPORTDIR=~/jsglr2evaluation/reports

all: languages sources validate measurements benchmarks processResults reportLatex reportR

languages:
	amm setupLanguages.sc dir=$(DIR)

sources:
	amm setupSources.sc dir=$(DIR)

validate:
	JAVA_OPTS="-Xmx8G" amm validate.sc dir=$(DIR)

measurements: buildMeasurements execMeasurements

buildMeasurements:
	mvn -f ../org.spoofax.jsglr2.measure -q clean install

execMeasurements:
	amm measurements.sc dir=$(DIR)

benchmarks: buildBenchmarks execBenchmarks

buildBenchmarks:
	mvn -f ../org.spoofax.jsglr2.benchmark -q clean install

execBenchmarks:
	amm benchmarks.sc dir=$(DIR) iterations=$(ITERATIONS) samples=$(SAMPLES)

processResults:
	amm processResults.sc dir=$(DIR)

reportLatex:
	amm reportLatex.sc dir=$(DIR) iterations=$(ITERATIONS) reportDir=$(REPORTDIR)

reportR:
	Rscript report.R $(DIR) $(REPORTDIR)