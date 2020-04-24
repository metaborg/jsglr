package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2StateApplicableGotosBenchmarkJava8 extends JSGLR2StateApplicableGotosBenchmark {

    public JSGLR2StateApplicableGotosBenchmarkJava8() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.java8));
    }

}
