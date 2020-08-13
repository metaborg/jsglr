package org.spoofax.jsglr2.benchmark.jsglr2;

import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public abstract class JSGLR2BenchmarkIncrementalParsingAndImploding extends JSGLR2BenchmarkIncremental {

    @Override protected boolean implode() {
        return true;
    }

    @Override protected Object action(Blackhole bh, IncrementalStringInput input) throws ParseException {
        if(i == 0)
            return jsglr2MultiParser.parse(input.content[i]);

        if(i > 0) {
            if(parserType.setupCache)
                return prevCacheImpl.get(input).parse(input.content[i]);
            else
                return jsglr2MultiParser.parse(input.content[i]);
        }

        if(i == -2)
            return jsglr2MultiParser.parse(uniqueInputs.get(input));

        // if (i == -1)
        return jsglr2MultiParser.parse(input.content);
    }

}
