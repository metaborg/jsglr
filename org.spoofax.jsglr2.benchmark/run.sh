# Command line arguments:
# 1: warmup iterations
# 2: benchmark iterations
# 3: reports path

mvn clean install

mkdir -p $3/benchmarks/jsglr1
mkdir -p $3/benchmarks/jsglr2

java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr1/lexical.csv "JSGLR1LexicalBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr1/sumAmbiguous.csv "JSGLR1SumAmbiguousBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr1/sumNonAmbiguous.csv "JSGLR1SumNonAmbiguousBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr1/java8.csv "JSGLR1Java8Benchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr1/greenmarl.csv "JSGLR1GreenMarlBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr1/webdsl.csv "JSGLR1WebDSLBenchmark"

java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/lexical.csv "JSGLR2LexicalBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumAmbiguous.csv "JSGLR2SumAmbiguousBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumNonAmbiguous.csv "JSGLR2SumNonAmbiguousBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/java8.csv "JSGLR2Java8Benchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/greenmarl.csv "JSGLR2GreenMarlBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/webdsl.csv "JSGLR2WebDSLBenchmark"