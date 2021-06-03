package org.spoofax.jsglr2.benchmark.jsglr2;

import java.util.*;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.spoofax.jsglr2.JSGLR2ImplementationWithCache;
import org.spoofax.jsglr2.benchmark.jsglr2.util.JSGLR2MultiParser;
import org.spoofax.jsglr2.benchmark.jsglr2.util.JSGLR2PersistentCache;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

import static org.spoofax.jsglr2.JSGLR2Variant.Preset.*;

public abstract class JSGLR2BenchmarkIncremental extends JSGLR2Benchmark<String[], IncrementalStringInput> {

    public enum ParserType {
        Standard(false, new IntegrationVariant(standard.variant)),
        Elkhound(false, new IntegrationVariant(elkhound.variant)),
        Incremental(true, new IntegrationVariant(incremental.variant)),
        IncrementalNoCache(false, new IntegrationVariant(incremental.variant));

        boolean setupCache;
        IntegrationVariant integrationVariant;

        ParserType(boolean setupCache, IntegrationVariant integrationVariant) {
            this.setupCache = setupCache;
            this.integrationVariant = integrationVariant;
        }
    }

    @Param({ "Standard", "Elkhound", "Incremental", "IncrementalNoCache" }) public ParserType parserType;

    @Param({ "-1" }) public int i;

    @Override protected IntegrationVariant variant() {
        return parserType.integrationVariant;
    }

    // Only used for parsing-only benchmarks
    Map<IncrementalStringInput, String> prevString = new HashMap<>();
    // Only used for parsing-only benchmarks
    Map<IncrementalStringInput, IParseForest> prevParse = new HashMap<>();
    // Only used for parsing-and-imploding benchmarks
    Map<IncrementalStringInput, JSGLR2PersistentCache<?, ?, ?, ?, ?, ?>> prevCacheImpl = new HashMap<>();
    // Only used for parsing-and-imploding benchmarks
    JSGLR2MultiParser<?> jsglr2MultiParser;

    Map<IncrementalStringInput, String[]> uniqueInputs = new HashMap<>();

    protected boolean shouldSetupCache() {
        return parserType.setupCache && i > 0;
    }

    @Setup public void setupCache() throws ParseException {
        if(implode())
            jsglr2MultiParser = new JSGLR2MultiParser<>(jsglr2);
        if(i == -2) {
            for(IncrementalStringInput input : inputs) {
                List<String> res = new ArrayList<>();
                String prev = null;
                for(String s : input.content) {
                    if(!Objects.equals(prev, s)) {
                        res.add(s);
                        prev = s;
                    }
                }
                uniqueInputs.put(input, res.toArray(new String[0]));
            }
        }
        if(shouldSetupCache()) {
            for(IncrementalStringInput input : inputs) {
                String content = input.content[i - 1];
                if(content == null)
                    continue;
                if(!implode()) {
                    prevString.put(input, content);
                    prevParse.put(input, jsglr2.parser.parseUnsafe(content, null));
                } else {
                    @SuppressWarnings({ "rawtypes", "unchecked" }) JSGLR2PersistentCache impl =
                        new JSGLR2PersistentCache<>(((JSGLR2ImplementationWithCache) jsglr2), content);
                    prevCacheImpl.put(input, impl);
                }
            }
        }
    }

}
