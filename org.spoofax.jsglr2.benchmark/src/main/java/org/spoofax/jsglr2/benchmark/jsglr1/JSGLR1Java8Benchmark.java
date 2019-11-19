package org.spoofax.jsglr2.benchmark.jsglr1;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR1Java8Benchmark extends JSGLR1Benchmark {

    public JSGLR1Java8Benchmark() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.java8));
    }

}
