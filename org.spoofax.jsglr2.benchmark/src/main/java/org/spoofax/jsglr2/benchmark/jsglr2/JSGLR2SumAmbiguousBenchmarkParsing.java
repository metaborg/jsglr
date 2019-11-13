package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2SumAmbiguousBenchmarkParsing extends JSGLR2BenchmarkParsing {

    public JSGLR2SumAmbiguousBenchmarkParsing() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.sumAmbiguous);
    }

    @Param({ "20", "40", "60", "80" }) public int n;

}
