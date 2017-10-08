package org.spoofax.jsglr2.benchmark.jsglr1;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR1LexicalBenchmark extends JSGLR1Benchmark {
    
    public JSGLR1LexicalBenchmark() {
        super(TestSet.lexical);
    }
    
    @Param({"10000", "50000", "100000"})
    public int n;

}
