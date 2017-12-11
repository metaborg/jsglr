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

java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/lexical_parsing.csv "JSGLR2LexicalBenchmarkParsing"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/lexical_parsetable.csv "JSGLR2LexicalBenchmarkParseTable"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumAmbiguous_parsing.csv "JSGLR2SumAmbiguousBenchmarkParsing"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumAmbiguous_parsetable.csv "JSGLR2SumAmbiguousBenchmarkParseTable"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumNonAmbiguous_parsing.csv "JSGLR2SumNonAmbiguousBenchmarkParsing"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -p implode=false -rff $3/benchmarks/jsglr2/sumNonAmbiguous_parsetable.csv "JSGLR2SumNonAmbiguousBenchmarkParseTable"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/java_parsing.csv "JSGLR2Java8BenchmarkParsing"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/java_parsetable.csv "JSGLR2Java8BenchmarkParseTable"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/java_parsing_imploding.csv "JSGLR2Java8BenchmarkParsingAndImploding"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/javaUnrolled_parsing.csv "JSGLR2Java8UnrolledBenchmarkParsing"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/javaUnrolled_parsetable.csv "JSGLR2Java8UnrolledBenchmarkParseTable"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/javaUnrolled_parsing_imploding.csv "JSGLR2Java8UnrolledBenchmarkParsingAndImploding"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/greenmarl_parsing.csv "JSGLR2GreenMarlBenchmarkParsing"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/greenmarl_parsetable.csv "JSGLR2GreenMarlBenchmarkParseTable"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/greenmarl_parsing_imploding.csv "JSGLR2GreenMarlBenchmarkParsingAndImploding"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/webdsl_parsing.csv "JSGLR2WebDSLBenchmarkParsing"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/webdsl_parsetable.csv "JSGLR2WebDSLBenchmarkParseTable"
java -jar target/org.spoofax.jsglr2.benchmark.jar -wi $1 -i $2 -f 1 -rff $3/benchmarks/jsglr2/webdsl_parsing_imploding.csv "JSGLR2WebDSLBenchmarkParsingAndImploding"