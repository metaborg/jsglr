package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2UnrolledJava8BenchmarkParsingAndImploding extends JSGLR2BenchmarkParsingAndImploding {

    public JSGLR2UnrolledJava8BenchmarkParsingAndImploding() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.java8Unrolled);
    }

}
