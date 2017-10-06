package org.spoofax.jsglr2.benchmark.jsglr1;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR1SumNonAmbiguousBenchmark extends JSGLR1Benchmark {
    
    public JSGLR1SumNonAmbiguousBenchmark() {
        super(TestSet.sumNonAmbiguous);
    }
    
    @Param({"1000", "2000", "4000", "8000", "16000"})
    public int n;

}
