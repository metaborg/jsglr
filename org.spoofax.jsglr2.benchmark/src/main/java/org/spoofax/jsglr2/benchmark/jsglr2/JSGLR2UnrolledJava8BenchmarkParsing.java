package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2UnrolledJava8BenchmarkParsing extends JSGLR2BenchmarkParsing {

    public JSGLR2UnrolledJava8BenchmarkParsing() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.java8Unrolled));
    }

}
