package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2WebDSLBenchmarkParsingAndImploding extends JSGLR2BenchmarkParsingAndImploding {

    public JSGLR2WebDSLBenchmarkParsingAndImploding() {
        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(TestSet.webDSL));
    }

}
