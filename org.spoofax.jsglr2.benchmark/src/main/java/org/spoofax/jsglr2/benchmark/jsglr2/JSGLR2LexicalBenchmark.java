package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2LexicalBenchmark extends JSGLR2Benchmark {
    
    public JSGLR2LexicalBenchmark() {
        super(TestSet.lexical);
    }
    
    @Param({"10000", "50000", "100000"})
    public int n;

}
