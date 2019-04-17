package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2SumNonAmbiguousBenchmarkIncrementalParsingAndImploding
    extends JSGLR2BenchmarkIncrementalParsingAndImploding {

    public JSGLR2SumNonAmbiguousBenchmarkIncrementalParsingAndImploding() {
        super(TestSet.sumNonAmbiguousIncremental);
    }

    @Param({ "4000", "8000", "16000", "32000", "64000" }) public int n;
}
