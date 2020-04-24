package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2LexicalBenchmarkParseTable extends JSGLR2BenchmarkParseTable {

    public JSGLR2LexicalBenchmarkParseTable() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.lexical));
    }

    @Param({ "10000", "50000", "100000" }) public int n;

}
