package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2UnrolledJava8BenchmarkParseTable extends JSGLR2BenchmarkParseTable {

    public JSGLR2UnrolledJava8BenchmarkParseTable() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.java8Unrolled);
    }

}
