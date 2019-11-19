package org.spoofax.jsglr2.benchmark.jsglr2.datastructures;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2ActiveStacksBenchmarkJava8 extends JSGLR2ActiveStacksBenchmark {

    public JSGLR2ActiveStacksBenchmarkJava8() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.java8));
    }

}
