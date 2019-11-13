package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public abstract class JSGLR2BenchmarkIncrementalParsing extends JSGLR2BenchmarkIncremental {

    protected JSGLR2BenchmarkIncrementalParsing(TestSet<String[], IncrementalStringInput> testSet) {
        super(new BenchmarkTestSetReader<>(testSet));
    }

    @Override protected boolean implode() {
        return false;
    }

    @Override protected Object action(Blackhole bh, IncrementalStringInput input) throws ParseException {
        correctCache(input);

        if(i >= 0)
            return jsglr2.parser.parseUnsafe(input.content[i], input.filename, null);

        if(i == -2) {
            for(String content : uniqueInputs.get(input)) {
                bh.consume(jsglr2.parser.parseUnsafe(content, input.filename, null));
            }
            return null;
        }

        // if (i == -1)
        for(String content : input.content) {
            possiblyClearCache();
            bh.consume(jsglr2.parser.parseUnsafe(content, input.filename, null));
        }
        return null;
    }

}
