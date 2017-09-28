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