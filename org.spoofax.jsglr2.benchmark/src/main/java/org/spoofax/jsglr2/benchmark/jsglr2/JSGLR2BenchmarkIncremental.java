package org.spoofax.jsglr2.benchmark.jsglr2;

import java.util.*;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variant;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.imploder.incremental.IncrementalImplodeInput;
import org.spoofax.jsglr2.imploder.incremental.IncrementalTreeImploder;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public abstract class JSGLR2BenchmarkIncremental extends JSGLR2Benchmark<String[], IncrementalStringInput> {

    public enum ParserType {
        Batch(false,
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted,
                ProductionToGotoRepresentation.JavaHashMap), JSGLR2Variant.Preset.standard.variant)),
        Incremental(true,
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted,
                ProductionToGotoRepresentation.JavaHashMap), JSGLR2Variant.Preset.incremental.variant)),
        IncrementalNoCache(false,
            new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted,
                ProductionToGotoRepresentation.JavaHashMap), JSGLR2Variant.Preset.incremental.variant));

        boolean setupCache;
        IntegrationVariant integrationVariant;

        ParserType(boolean setupCache, IntegrationVariant integrationVariant) {
            this.setupCache = setupCache;
            this.integrationVariant = integrationVariant;
        }
    }

    @Param({ "Batch", "Incremental", "IncrementalNoCache" }) public ParserType parserType;

    @Param({ "-1" }) public int i;

    @Override protected IntegrationVariant variant() {
        return parserType.integrationVariant;
    }

    Map<IncrementalStringInput, String> prevString = new HashMap<>();
    Map<IncrementalStringInput, IncrementalParseForest> prevResult = new HashMap<>();
    Map<IncrementalStringInput, WeakHashMap<IParseNode<IParseForest, IDerivation<IParseForest>>, TreeImploder.SubTree<IStrategoTerm>>> prevMap =
        new HashMap<>();

    Map<IncrementalStringInput, List<String>> uniqueInputs = new HashMap<>();

    protected boolean shouldSetupCache() {
        return parserType.setupCache && i > 0;
    }

    @Setup public void setupCache() throws ParseException {
        if(i == -2) {
            for(IncrementalStringInput input : inputs) {
                List<String> res = new ArrayList<>();
                String prev = null;
                for(String s : input.content) {
                    if(s.length() == 0)
                        continue;
                    if(!s.equals(prev)) {
                        res.add(s);
                        prev = s;
                    }
                }
                uniqueInputs.put(input, res);
            }
        }
        if(shouldSetupCache()) {
            for(IncrementalStringInput input : inputs) {
                String content = input.content[i - 1];
                prevString.put(input, content);
                prevResult.put(input,
                    ((IncrementalParseForest) jsglr2.parser.parseUnsafe(content, input.fileName, null)));
                if(implode()) {
                    IncrementalTreeImploder<IParseForest, IParseNode<IParseForest, IDerivation<IParseForest>>, IDerivation<IParseForest>, IStrategoTerm, IncrementalImplodeInput<IParseNode<IParseForest, IDerivation<IParseForest>>, IStrategoTerm>> imploder =
                        (IncrementalTreeImploder) jsglr2.imploder;
                    imploder.implode(prevString.get(input), input.fileName, prevResult.get(input));
                    prevMap.put(input, imploder.getFromCache(input.fileName));
                }
            }
        }
    }

    // Only used in ManualBenchmark
    Iterable<IncrementalStringInput> getInputs() {
        return inputs;
    }

}
