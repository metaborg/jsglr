package org.spoofax.jsglr2.benchmark.jsglr1;

import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.StringInput;

public class JSGLR1BenchmarkExternal extends JSGLR1Benchmark {

    public JSGLR1BenchmarkExternal() {
        String[] args = System.getProperty("testSet").split(" ");

        TestSetWithParseTable<String, StringInput> testSet = TestSet.fromArgsWithParseTable(TestSet.parseArgs(args));

        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(testSet));
    }

}
