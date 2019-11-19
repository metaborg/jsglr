package org.spoofax.jsglr2.benchmark.jsglr1;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR1SumAmbiguousBenchmark extends JSGLR1Benchmark {

    public JSGLR1SumAmbiguousBenchmark() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.sumAmbiguous));
    }

    @Param({ "20", "40", "60", "80" }) public int n;

}
