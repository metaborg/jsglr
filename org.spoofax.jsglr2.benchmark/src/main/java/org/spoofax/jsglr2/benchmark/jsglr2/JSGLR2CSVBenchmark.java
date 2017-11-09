package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2CSVBenchmark extends JSGLR2Benchmark {

    public JSGLR2CSVBenchmark() {
        super(TestSet.csv);
    }

    @Param({ "1000", "2000", "4000" }) public int n;

}
