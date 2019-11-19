package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.annotations.Param;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2SumAmbiguousBenchmarkParseTable extends JSGLR2BenchmarkParseTable {

    public JSGLR2SumAmbiguousBenchmarkParseTable() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.sumAmbiguous));
    }

    @Param({ "20", "40", "60", "80" }) public int n;

}
