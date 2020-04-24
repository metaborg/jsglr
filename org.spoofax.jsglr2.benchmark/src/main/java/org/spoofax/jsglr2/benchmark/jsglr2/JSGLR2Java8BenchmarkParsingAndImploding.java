package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2Java8BenchmarkParsingAndImploding extends JSGLR2BenchmarkParsingAndImploding {

    public JSGLR2Java8BenchmarkParsingAndImploding() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.java8));
    }

}
