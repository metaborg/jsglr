package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2GreenMarlBenchmarkParsing extends JSGLR2BenchmarkParsing {

    public JSGLR2GreenMarlBenchmarkParsing() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.greenMarl));
    }

}
