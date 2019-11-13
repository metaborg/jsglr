package org.spoofax.jsglr2.benchmark.jsglr1;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR1WebDSLBenchmark extends JSGLR1Benchmark {

    public JSGLR1WebDSLBenchmark() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.webDSL);
    }

}
