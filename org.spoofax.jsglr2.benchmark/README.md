# JSGLR2 Benchmarks

Build the project:

```
mvn clean install
```

Then run the benchmarks with (1 fork, 5 warmup iterations and 10 measurement iterations):

```
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi 5 -i 10 -f 1
```

Or just a single benchmark (e.g. Java 8):

```
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi 5 -i 10 -f 1 Java8Benchmark
```

Fastest Java 8 benchmark:

```
mvn clean install && java -Xmx2048m -Xss8096k -jar target/org.spoofax.jsglr2.benchmark.jar -wi 20 -i 20 -f 1 -p implode=false -p parseForestConstruction=Full -p parseForestRepresentation=Hybrid -p reducing=Elkhound -p stackRepresentation=HybridElkhound "JSGLR2Java8Benchmark"
```

Applicable actions on states (character classes and return type):

```
mvn clean install && java -jar target/org.spoofax.jsglr2.benchmark.jar -wi 20 -i 20 -f 1 JSGLR2Java8StateApplicableActionsBenchmark
```

Applicable actions on states (goto):

```
mvn clean install && java -jar target/org.spoofax.jsglr2.benchmark.jar -wi 20 -i 20 -f 1 JSGLR2Java8GotoBenchmark
```

For shifter:

```
mvn clean install && java -jar target/org.spoofax.jsglr2.benchmark.jar -wi 20 -i 20 -f 1 JSGLR2Java8ForShifterBenchmark
```