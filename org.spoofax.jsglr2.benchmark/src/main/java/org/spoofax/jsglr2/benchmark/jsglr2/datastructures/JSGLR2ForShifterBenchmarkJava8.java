package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2ForShifterBenchmarkJava8 extends JSGLR2ForShifterBenchmark {

    public JSGLR2ForShifterBenchmarkJava8() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.java8));
    }

}
