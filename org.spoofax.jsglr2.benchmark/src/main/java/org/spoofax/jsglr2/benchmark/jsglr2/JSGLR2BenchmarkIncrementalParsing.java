package org.spoofax.jsglr2.benchmark.jsglr2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.incremental.IncrementalParser;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

public abstract class JSGLR2BenchmarkIncrementalParsing extends JSGLR2Benchmark<String[], IncrementalStringInput> {

    protected JSGLR2BenchmarkIncrementalParsing(TestSet<String[], IncrementalStringInput> testSet) {
        super(new BenchmarkTestSetReader<>(testSet));
    }

    @Param({ "false" }) public boolean implode;

    @Param({ "DisjointSorted" }) ActionsForCharacterRepresentation actionsForCharacterRepresentation;

    @Param({ "JavaHashMap" }) ProductionToGotoRepresentation productionToGotoRepresentation;

    @Param({ "ArrayList" }) public ActiveStacksRepresentation activeStacksRepresentation;

    @Param({ "ArrayDeque" }) public ForActorStacksRepresentation forActorStacksRepresentation;

    @Param({ "Hybrid", "Incremental" }) public ParseForestRepresentation parseForestRepresentation;

    @Param({ "Full" }) public ParseForestConstruction parseForestConstruction;

    @Param({ "Hybrid" }) public StackRepresentation stackRepresentation;

    @Param({ "Basic" }) public Reducing reducing;

    @Param({ "TokenizedRecursive" }) public ImploderVariant imploder;

    @Param({ "-1" }) public int i;

    Map<IncrementalStringInput, String> prevString = new HashMap<>();
    Map<IncrementalStringInput, IncrementalParseForest> prevResult = new HashMap<>();
    Map<IncrementalStringInput, List<String>> uniqueInputs = new HashMap<>();

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
        if(i > 0) {
            for(IncrementalStringInput input : inputs) {
                if(jsglr2.parser instanceof IncrementalParser) {
                    String content = input.content[i - 1];
                    prevString.put(input, content);
                    prevResult.put(input,
                        ((IncrementalParseForest) jsglr2.parser.parseUnsafe(content, input.filename, null)));
                }
            }
        }
    }

    @Override protected IntegrationVariant variant() {
        if(implode)
            throw new IllegalStateException("this variant is not used for benchmarking");

        return new IntegrationVariant(
            new ParseTableVariant(actionsForCharacterRepresentation, productionToGotoRepresentation),
            new ParserVariant(activeStacksRepresentation, forActorStacksRepresentation, parseForestRepresentation,
                parseForestConstruction, stackRepresentation, reducing),
            imploder, TokenizerVariant.Null);
    }

    @Override protected boolean implode() {
        return implode;
    }

    @Override protected Object action(Blackhole bh, IncrementalStringInput input) throws ParseException {
        if(jsglr2.parser instanceof IncrementalParser) {
            IncrementalParser parser = (IncrementalParser) jsglr2.parser;
            parser.clearCache();
            if(i > 0)
                parser.addToCache(input.filename, prevString.get(input), prevResult.get(input));
        }

        if(i >= 0)
            return jsglr2.parser.parseUnsafe(input.content[i], input.filename, null);

        if(i == -2) {
            for(String content : uniqueInputs.get(input)) {
                bh.consume(jsglr2.parser.parseUnsafe(content, input.filename, null));
            }
            return null;
        }

        for(String content : input.content) {
            bh.consume(jsglr2.parser.parseUnsafe(content, input.filename, null));
        }
        return null;
    }

}
