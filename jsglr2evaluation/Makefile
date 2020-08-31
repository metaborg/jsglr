DIR=~/jsglr2evaluation
ITERATIONS=1 # warmup and benchmark iterations
SAMPLES=1 # number of sampled files for per file benchmarking
REPORTDIR=~/jsglr2evaluation/reports

MEASUREMENTS_JAR=../org.spoofax.jsglr2.measure/target/org.spoofax.jsglr2.measure-2.6.0-SNAPSHOT.jar
BENCHMARKS_JAR=../org.spoofax.jsglr2.benchmark/target/org.spoofax.jsglr2.benchmark-2.6.0-SNAPSHOT.jar

all: languages sources preProcessing measurements benchmarks postProcessing reportLatex reportR

clean: cleanLanguages cleanSources cleanPreProcessing cleanMeasurements cleanBenchmarks cleanPostProcessing cleanReports


# Pull Spoofax languages from GitHub and build them
languages: $(DIR)/languages

$(DIR)/languages: config.yml setupLanguages.sc
	amm setupLanguages.sc dir=$(DIR)

cleanLanguages:
	-rm -rf $(DIR)/languages


# Setup evaluation corpus by pulling projects from GitHub
sources: $(DIR)/sources

$(DIR)/sources: config.yml setupSources.sc
	amm setupSources.sc dir=$(DIR)

cleanSources:
	-rm -rf $(DIR)/sources


# Validate absence of invalid programs and aggregate files
preProcessing: $(DIR)/sources/validated

$(DIR)/sources/validated: $(DIR)/languages $(DIR)/sources preProcess.sc
	JAVA_OPTS="-Xmx8G" amm preProcess.sc dir=$(DIR) && touch $(DIR)/sources/validated

cleanPreProcessing:
	-rm $(DIR)/sources/validated


# Perform measurements
measurements: $(DIR)/measurements

$(DIR)/measurements: $(DIR)/sources/validated $(MEASUREMENTS_JAR) measurements.sc
	amm measurements.sc dir=$(DIR)

$(MEASUREMENTS_JAR): ../org.spoofax.jsglr2.measure/src
	mvn -f ../org.spoofax.jsglr2.measure -q install

cleanMeasurements:
	-rm -r $(DIR)/measurements
	-rm -r ../org.spoofax.jsglr2.measure/target


# Performs benchmarks
benchmarks: $(DIR)/benchmarks

$(DIR)/benchmarks: $(BENCHMARKS_JAR) benchmarks.sc
	amm benchmarks.sc dir=$(DIR) iterations=$(ITERATIONS) samples=$(SAMPLES)

$(BENCHMARKS_JAR): ../org.spoofax.jsglr2.benchmark/src
	mvn -f ../org.spoofax.jsglr2.benchmark -q install

cleanBenchmarks:
	-rm -r $(DIR)/benchmarks
	-rm -r ../org.spoofax.jsglr2.benchmarks/target


# Post process results from measurements and benchmarks
postProcessing: $(DIR)/results

$(DIR)/results: $(DIR)/benchmarks $(DIR)/measurements postProcess.sc
	amm postProcess.sc dir=$(DIR) samples=$(SAMPLES)

cleanPostProcessing:
	-rm -r $(DIR)/results


# Reporting in Latex tables
reportLatex: reportLatex.sc $(DIR)/results
	amm reportLatex.sc dir=$(DIR) iterations=$(ITERATIONS) reportDir=$(REPORTDIR)

# Reporting in plots
reportR: report.R $(DIR)/results
	Rscript report.R $(DIR) $(REPORTDIR)

cleanReports:
	-rm -r $(REPORTDIR)
