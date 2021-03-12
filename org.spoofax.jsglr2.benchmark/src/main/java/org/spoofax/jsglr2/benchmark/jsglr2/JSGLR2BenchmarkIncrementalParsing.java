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
        if(i >= 0) {
            String s = input.content[i];
            if(s == null)
                return null;
            return jsglr2.parser.parseUnsafe(s, null, prevString.get(input), prevParse.get(input));
        }

        String previousInput = null;
        IParseForest previousResult = null;

        if(i == -2) {
            for(String content : uniqueInputs.get(input)) {
                if(content == null)
                    continue;
                bh.consume(previousResult = jsglr2.parser.parseUnsafe(content, null, previousInput, previousResult));
                previousInput = content;
            }
            return null;
        }

        // if (i == -1)
        for(String content : input.content) {
            if(content == null)
                continue;
            bh.consume(previousResult = jsglr2.parser.parseUnsafe(content, null, previousInput, previousResult));
            previousInput = content;
        }
        return null;
    }

}
