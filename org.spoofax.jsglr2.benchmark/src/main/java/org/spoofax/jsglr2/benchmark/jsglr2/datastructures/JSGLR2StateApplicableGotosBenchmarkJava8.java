package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2StateApplicableGotosBenchmarkJava8 extends JSGLR2StateApplicableGotosBenchmark {

    public JSGLR2StateApplicableGotosBenchmarkJava8() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.java8);
    }

}
