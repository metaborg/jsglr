DIR=~/jsglr2evaluation
ITERATIONS=1
REPORTDIR=~/jsglr2evaluation/reports

all: languages sources validate measurements benchmarks processResults reportLatex reportR

languages:
	amm setupLanguages.sc $(DIR)

sources:
	amm setupSources.sc $(DIR)

validate:
	JAVA_OPTS="-Xmx8G" amm validateSources.sc $(DIR)

measurements: buildMeasurements execMeasurements

buildMeasurements:
	mvn -f ../org.spoofax.jsglr2.measure -q clean install

execMeasurements:
	amm measurements.sc $(DIR)

benchmarks: buildBenchmarks execBenchmarks

buildBenchmarks:
	mvn -f ../org.spoofax.jsglr2.benchmark -q clean install

execBenchmarks:
	amm benchmarks.sc $(DIR) $(ITERATIONS)

processResults:
	amm processResults.sc $(DIR)

reportLatex:
	amm reportLatex.sc $(DIR) $(ITERATIONS) $(REPORTDIR)

reportR:
	Rscript report.R $(DIR) $(REPORTDIR)