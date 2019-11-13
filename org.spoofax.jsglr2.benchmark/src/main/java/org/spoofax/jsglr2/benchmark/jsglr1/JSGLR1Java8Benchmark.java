package org.spoofax.jsglr2.benchmark.jsglr1;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR1Java8Benchmark extends JSGLR1Benchmark {

    public JSGLR1Java8Benchmark() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.java8);
    }

}
