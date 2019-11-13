package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2Java8BenchmarkParsing extends JSGLR2BenchmarkParsing {

    public JSGLR2Java8BenchmarkParsing() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.java8);
    }

}
