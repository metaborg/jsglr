.PHONY: all clean languages cleanLanguages sources cleanSources preProcessing cleanPreProcessing measurements cleanMeasurements benchmarks cleanBenchmarks postProcessing cleanPostProcessing reportLatex reportR cleanReports publish websiteRepo addToWebsite websitePush

DIR=~/jsglr2evaluation
SPOOFAX_DIR=../../..

JSGLR_DIR=$(SPOOFAX_DIR)/jsglr
MEASUREMENTS_JAR=$(JSGLR_DIR)/org.spoofax.jsglr2.measure/target/org.spoofax.jsglr2.measure-2.6.0-SNAPSHOT.jar
BENCHMARKS_JAR=$(JSGLR_DIR)/org.spoofax.jsglr2.benchmark/target/org.spoofax.jsglr2.benchmark-2.6.0-SNAPSHOT.jar

REPORTDIR=$(DIR)/reports

all: languages sources preProcessing measurements benchmarks postProcessing reportLatex reportR Pipfile.lock reportPy publish

clean: cleanLanguages cleanSources cleanPreProcessing cleanMeasurements cleanBenchmarks cleanPostProcessing cleanReports

cleanAll: clean all

export JSGLR2EVALUATION_WORKING_DIR=$(DIR)
export JSGLR2EVALUATION_SPOOFAX_DIR=$(SPOOFAX_DIR)
export JSGLR2EVALUATION_REPORT_DIR=$(REPORTDIR)

# Pull Spoofax languages from GitHub and build them
languages: $(DIR)/languages

$(DIR)/languages: setupLanguages.sc common.sc
	amm setupLanguages.sc

cleanLanguages:
	-rm -rf $(DIR)/languages


# Setup evaluation corpus by pulling projects from GitHub
sources: $(DIR)/sources

$(DIR)/sources: setupSources.sc common.sc
	amm setupSources.sc

cleanSources:
	-rm -rf $(DIR)/sources


# Validate absence of invalid programs and aggregate files
preProcessing: $(DIR)/sources/validated

$(DIR)/sources/validated: $(DIR)/languages $(DIR)/sources preProcess.sc common.sc
	JAVA_OPTS="-Xmx8G" amm preProcess.sc && touch $(DIR)/sources/validated

cleanPreProcessing:
	-rm $(DIR)/sources/validated


# Perform measurements
measurements: $(DIR)/measurements

$(DIR)/measurements: $(DIR)/sources/validated $(MEASUREMENTS_JAR) measurements.sc spoofax.sc spoofaxDeps.sc common.sc
	amm measurements.sc

$(MEASUREMENTS_JAR): $(JSGLR_DIR)/org.spoofax.jsglr2.measure/src
	mvn -f $(JSGLR_DIR)/org.spoofax.jsglr2.measure -q install

cleanMeasurements:
	-rm -r $(DIR)/measurements
	-rm -r $(JSGLR_DIR)/org.spoofax.jsglr2.measure/target


# Performs benchmarks
benchmarks: $(DIR)/benchmarks

$(DIR)/benchmarks: $(DIR)/sources/validated $(BENCHMARKS_JAR) benchmarks.sc spoofax.sc spoofaxDeps.sc common.sc
	amm benchmarks.sc

$(BENCHMARKS_JAR): $(JSGLR_DIR)/org.spoofax.jsglr2.benchmark/src
	mvn -f $(JSGLR_DIR)/org.spoofax.jsglr2.benchmark -q install

cleanBenchmarks:
	-rm -r $(DIR)/benchmarks
	-rm -r $(JSGLR_DIR)/org.spoofax.jsglr2.benchmarks/target


# Post process results from measurements and benchmarks
postProcessing: $(DIR)/results

$(DIR)/results: $(DIR)/benchmarks $(DIR)/measurements postProcess.sc common.sc
	amm postProcess.sc

cleanPostProcessing:
	-rm -r $(DIR)/results


# Reporting in Latex tables
reportLatex: $(DIR)/results reportLatex.sc spoofax.sc spoofaxDeps.sc common.sc
	amm reportLatex.sc

# Reporting in plots
reportR: $(DIR)/results report.R
	Rscript report.R $(DIR) $(REPORTDIR)

Pipfile.lock:
	pipenv install

reportPy: $(DIR)/results report.py Pipfile.lock
	pipenv run report $(DIR) $(REPORTDIR)

cleanReports:
	-rm -r $(REPORTDIR)


# Archive and publish reports to website
archive: $(DIR)/archive.tar.gz

$(DIR)/archive.tar.gz: $(DIR)/measurements $(DIR)/benchmarks $(DIR)/results $(DIR)/reports
	cd $(DIR) && tar -czf archive.tar.gz measurements benchmarks results -C $(REPORTDIR)/.. reports

# Publish on website
publish: websiteRepo addToWebsite websitePush

websiteRepo:
	if [ ! -d $(DIR)/website/.git ]; then git clone https://$(GITHUB_TOKEN)@github.com/metaborg/jsglr2evaluation-site.git $(DIR)/website; fi;
	cd $(DIR)/website && git pull

addToWebsite: $(DIR)/archive.tar.gz
	amm addToWebsite.sc

websitePush:
	cd $(DIR)/website && \
		git add -A && \
		git commit -m "Evaluation results" && \
		git push https://$(GITHUB_TOKEN)@github.com/metaborg/jsglr2evaluation-site.git

cleanM2:
	-rm -r $(DIR)/m2-repo