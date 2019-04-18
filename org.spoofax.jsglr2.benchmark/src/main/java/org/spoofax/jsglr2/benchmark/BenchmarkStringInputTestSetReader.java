package org.spoofax.jsglr2.benchmark;

import org.spoofax.jsglr2.testset.StringInput;
import org.spoofax.jsglr2.testset.TestSet;

public class BenchmarkStringInputTestSetReader extends BenchmarkTestSetReader<StringInput> {
    public BenchmarkStringInputTestSetReader(TestSet testSet) {
        super(testSet);
    }

    @Override protected StringInput getInput(String filename, String input) {
        return new StringInput(filename, input);
    }
}
