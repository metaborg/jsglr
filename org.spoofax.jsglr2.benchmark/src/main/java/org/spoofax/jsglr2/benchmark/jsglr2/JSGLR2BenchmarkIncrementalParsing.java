package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public abstract class JSGLR2BenchmarkIncrementalParsing extends JSGLR2BenchmarkIncremental {

    @Override protected boolean implode() {
        return false;
    }

    @Override protected Object action(Blackhole bh, IncrementalStringInput input) throws ParseException {
        if(i >= 0)
            return jsglr2.parser.parseUnsafe(input.content[i], input.fileName, null, prevString.get(input),
                prevParse.get(input));

        String previousInput = null;
        IParseForest previousResult = null;

        if(i == -2) {
            for(String content : uniqueInputs.get(input)) {
                bh.consume(previousResult =
                    jsglr2.parser.parseUnsafe(content, input.fileName, null, previousInput, previousResult));
                previousInput = content;
            }
            return null;
        }

        // if (i == -1)
        for(String content : input.content) {
            bh.consume(previousResult =
                jsglr2.parser.parseUnsafe(content, input.fileName, null, previousInput, previousResult));
            previousInput = content;
        }
        return null;
    }

}
