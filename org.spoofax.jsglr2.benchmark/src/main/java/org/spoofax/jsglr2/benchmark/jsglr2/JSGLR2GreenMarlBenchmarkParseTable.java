package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2GreenMarlBenchmarkParseTable extends JSGLR2BenchmarkParseTable {

    public JSGLR2GreenMarlBenchmarkParseTable() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.greenMarl));
    }

}
