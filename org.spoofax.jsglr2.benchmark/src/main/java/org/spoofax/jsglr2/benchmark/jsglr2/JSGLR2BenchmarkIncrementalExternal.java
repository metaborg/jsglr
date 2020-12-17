package org.spoofax.jsglr2.benchmark.jsglr2;

import java.util.Map;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetWithParseTableReader;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public class JSGLR2BenchmarkIncrementalExternal extends JSGLR2BenchmarkIncremental {

    public JSGLR2BenchmarkIncrementalExternal() {
        this(TestSet.parseArgs(System.getProperty("testSet").split(" ")));
    }

    public JSGLR2BenchmarkIncrementalExternal(Map<String, String> args) {
        TestSetWithParseTable<String[], IncrementalStringInput> testSet =
            TestSet.fromArgsWithParseTableIncremental(args);

        setTestSetReader(new BenchmarkTestSetWithParseTableReader<>(testSet));
    }

    @Param({ "false", "true" }) public boolean implode;

    @Override protected boolean implode() {
        return implode;
    }

    @Override public void setupCache() throws ParseException {
        // Hack: the original intent of the JSGLR2IncrementalBenchmark was to read in all files upon setup.
        // However, for the external benchmark, only two versions of these files are read.
        // Therefore, we have to overwrite the `setupCache` function, but copy/pasting is bad,
        // so we just pretend that the iteration number (`i`) is temporarily something different. O:)
        int oldI = i;
        i = Math.min(i, 1);
        super.setupCache();
        i = oldI;
    }

    @Override protected Object action(Blackhole bh, IncrementalStringInput input) throws ParseException {
        if(implode) {
            if(i == 0)
                return jsglr2MultiParser.parse(input.content[i]);

            if(i > 0) {
                if(parserType.setupCache && prevCacheImpl.containsKey(input))
                    return prevCacheImpl.get(input).parse(input.content[1]);
                else
                    return jsglr2MultiParser.parse(input.content[1]);
            }

            if(i == -2)
                return jsglr2MultiParser.parse(uniqueInputs.get(input));

            // if (i == -1)
            return jsglr2MultiParser.parse(input.content);
        } else {
            if(i >= 0) {
                String s = input.content[i > 0 ? 1 : 0];
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
                    bh.consume(
                        previousResult = jsglr2.parser.parseUnsafe(content, null, previousInput, previousResult));
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
}
