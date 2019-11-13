package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2GreenMarlBenchmarkParseTable extends JSGLR2BenchmarkParseTable {

    public JSGLR2GreenMarlBenchmarkParseTable() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.greenMarl);
    }

}
