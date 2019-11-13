package org.spoofax.jsglr2.benchmark.jsglr2;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.testset.TestSet;

public class JSGLR2WebDSLBenchmarkParsing extends JSGLR2BenchmarkParsing {

    public JSGLR2WebDSLBenchmarkParsing() {
        this.testSetReader = new BenchmarkTestSetReader<>(TestSet.webDSL);
    }

}
