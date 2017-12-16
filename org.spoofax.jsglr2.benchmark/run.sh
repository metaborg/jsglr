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
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr1/java.csv "JSGLR1Java8Benchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr1/javaUnrolled.csv "JSGLR1Java8UnrolledBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr1/greenmarl.csv "JSGLR1GreenMarlBenchmark"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr1/webdsl.csv "JSGLR1WebDSLBenchmark"

java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/lexical.csv "JSGLR2Lexical"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumAmbiguous.csv "JSGLR2SumAmbiguous"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumNonAmbiguous.csv "JSGLR2SumNonAmbiguous"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/java.csv "JSGLR2Java8"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/javaUnrolled.csv "JSGLR2UnrolledJava8"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/greenmarl.csv "JSGLR2GreenMarl"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/webdsl.csv "JSGLR2WebDSLBenchmark"
