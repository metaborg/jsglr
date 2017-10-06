package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2SumAmbiguousBenchmark extends JSGLR2Benchmark {
    
    public JSGLR2SumAmbiguousBenchmark() {
        super(TestSet.sumAmbiguous);
    }
    
    @Param({"10", "20", "30", "40", "50", "60", "70", "80"})
    public int n;

}
