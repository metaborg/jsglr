CONFIG=$(CONFIG)
DIR=~/jsglr2evaluation
ITERATIONS=1 # warmup and benchmark iterations
SAMPLES=1 # number of sampled files for per file benchmarking
REPORTDIR=~/jsglr2evaluation/reports

SPOOFAX_DIR=../../..
JSGLR_DIR=$(SPOOFAX_DIR)/jsglr
MEASUREMENTS_JAR=$(JSGLR_DIR)/org.spoofax.jsglr2.measure/target/org.spoofax.jsglr2.measure-2.6.0-SNAPSHOT.jar
BENCHMARKS_JAR=$(JSGLR_DIR)/org.spoofax.jsglr2.benchmark/target/org.spoofax.jsglr2.benchmark-2.6.0-SNAPSHOT.jar

all: languages sources preProcessing measurements benchmarks postProcessing reportLatex reportR

clean: cleanLanguages cleanSources cleanPreProcessing cleanMeasurements cleanBenchmarks cleanPostProcessing cleanReports


# Pull Spoofax languages from GitHub and build them
languages: $(DIR)/languages

$(DIR)/languages: $(CONFIG) setupLanguages.sc common.sc
	amm setupLanguages.sc config=$(CONFIG) dir=$(DIR) spoofaxDir=$(SPOOFAX_DIR)

cleanLanguages:
	-rm -rf $(DIR)/languages


# Setup evaluation corpus by pulling projects from GitHub
sources: $(DIR)/sources

$(DIR)/sources: $(CONFIG) setupSources.sc common.sc
	amm setupSources.sc config=$(CONFIG) dir=$(DIR) spoofaxDir=$(SPOOFAX_DIR)

cleanSources:
	-rm -rf $(DIR)/sources


# Validate absence of invalid programs and aggregate files
preProcessing: $(DIR)/sources/validated

$(DIR)/sources/validated: $(DIR)/languages $(DIR)/sources preProcess.sc common.sc
	JAVA_OPTS="-Xmx8G" amm preProcess.sc config=$(CONFIG) dir=$(DIR) spoofaxDir=$(SPOOFAX_DIR) && touch $(DIR)/sources/validated

cleanPreProcessing:
	-rm $(DIR)/sources/validated


# Perform measurements
measurements: $(DIR)/measurements

$(DIR)/measurements: $(DIR)/sources/validated $(MEASUREMENTS_JAR) measurements.sc spoofax.sc spoofaxDeps.sc common.sc
	amm measurements.sc config=$(CONFIG) dir=$(DIR) spoofaxDir=$(SPOOFAX_DIR)

$(MEASUREMENTS_JAR): $(JSGLR_DIR)/org.spoofax.jsglr2.measure/src
	mvn -f $(JSGLR_DIR)/org.spoofax.jsglr2.measure -q install

cleanMeasurements:
	-rm -r $(DIR)/measurements
	-rm -r $(JSGLR_DIR)/org.spoofax.jsglr2.measure/target


# Performs benchmarks
benchmarks: $(DIR)/benchmarks

$(DIR)/benchmarks: $(BENCHMARKS_JAR) benchmarks.sc spoofax.sc spoofaxDeps.sc common.sc
	amm benchmarks.sc config=$(CONFIG) dir=$(DIR) spoofaxDir=$(SPOOFAX_DIR) iterations=$(ITERATIONS) samples=$(SAMPLES)

$(BENCHMARKS_JAR): $(JSGLR_DIR)/org.spoofax.jsglr2.benchmark/src
	mvn -f $(JSGLR_DIR)/org.spoofax.jsglr2.benchmark -q install

cleanBenchmarks:
	-rm -r $(DIR)/benchmarks
	-rm -r $(JSGLR_DIR)/org.spoofax.jsglr2.benchmarks/target


# Post process results from measurements and benchmarks
postProcessing: $(DIR)/results

$(DIR)/results: #$(DIR)/benchmarks $(DIR)/measurements postProcess.sc common.sc
	amm postProcess.sc config=$(CONFIG) dir=$(DIR) spoofaxDir=$(SPOOFAX_DIR) samples=$(SAMPLES)

cleanPostProcessing:
	-rm -r $(DIR)/results


# Reporting in Latex tables
reportLatex: $(DIR)/results reportLatex.sc spoofax.sc spoofaxDeps.sc common.sc
	amm reportLatex.sc config=$(CONFIG) dir=$(DIR) spoofaxDir=$(SPOOFAX_DIR) iterations=$(ITERATIONS) reportDir=$(REPORTDIR)

# Reporting in plots
reportR: report.R $(DIR)/results
	Rscript report.R $(DIR) $(REPORTDIR)

cleanReports:
	-rm -r $(REPORTDIR)
