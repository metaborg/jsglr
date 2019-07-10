#!/usr/bin/env bash

# These benchmarks have been used for Incremental SGLR.

set -e

mvn -f ../../sdf/org.metaborg.tableinterfaces/pom.xml clean install
mvn -f ../../sdf/org.metaborg.characterclasses/pom.xml clean install
mvn -f ../../sdf/org.metaborg.sdf2table/pom.xml clean install
mvn -f ../org.spoofax.jsglr/pom.xml clean install
mvn -f ../org.spoofax.jsglr2/pom.xml clean install
mvn -f ../org.spoofax.jsglr2.integration/pom.xml clean install
mvn clean install

timestamp=$(date +%F-%T)
jargs="-Xmx2048m -Xss2000000k -jar target/org.spoofax.jsglr2.benchmark.jar -wi 20 -i 20 -f 1"
export PATH=/usr/lib/jvm/java-8-openjdk-amd64/bin/:$PATH

for benchmark in \
    "JSGLR2Java8GitBenchmarkIncrementalParsing.benchmark" \
; do
    java ${jargs} -rff ${benchmark}-${timestamp}.csv ${benchmark} |& tee ${benchmark}-${timestamp}.log
done

true || \
    "JSGLR2SumNonAmbiguousBenchmarkIncrementalParsing.benchmark" \
    "JSGLR2SumNonAmbiguousBenchmarkIncrementalParsingAndImploding.benchmark" \
    "JSGLR2Java8BenchmarkIncrementalParsing.benchmark" \
    "JSGLR2Java8BenchmarkIncrementalParsingAndImploding.benchmark" \
    "JSGLR2OCamlGitBenchmarkIncrementalParsing.benchmark" \
