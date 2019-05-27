#!/usr/bin/env bash

# These benchmarks have been used for Incremental SGLR.

set -e

mvn -f ../org.spoofax.jsglr2/pom.xml clean install
mvn -f ../org.spoofax.jsglr2.integration/pom.xml clean install
mvn clean install

timestamp=$(date +%F-%T)
jargs="-Xmx2048m -Xss2000000k -jar target/org.spoofax.jsglr2.benchmark.jar -wi 20 -i 20 -f 1"
export PATH=/usr/lib/jvm/java-8-openjdk-amd64/bin/:$PATH

# Note that the JMH matches on prefix. So .....Parsing will also match .....ParsingAndImploding
for benchmark in "JSGLR2SumNonAmbiguousBenchmarkIncrementalParsing" ;
do
    java $jargs -rff ${benchmark}-${timestamp}.csv ${benchmark} |& tee ${benchmark}-${timestamp}.log
done

