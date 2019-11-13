package org.spoofax.jsglr2.benchmark.jsglr1;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR1GreenMarlBenchmark extends JSGLR1Benchmark {

    public JSGLR1GreenMarlBenchmark() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.greenMarl);
    }

}
