package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2LexicalBenchmarkParseTable extends JSGLR2BenchmarkParseTable {

    public JSGLR2LexicalBenchmarkParseTable() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.lexical);
    }

    @Param({ "10000", "50000", "100000" }) public int n;

}
