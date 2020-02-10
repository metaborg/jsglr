package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2StateApplicableActionsBenchmarkJava8 extends JSGLR2StateApplicableActionsBenchmark {

    public JSGLR2StateApplicableActionsBenchmarkJava8() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.java8));
    }

}
