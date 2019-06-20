package org.spoofax.jsglr2.benchmark.jsglr2;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.benchmark.BenchmarkTestSetReader;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.imploder.incremental.IncrementalImplodeInput;
import org.spoofax.jsglr2.imploder.incremental.IncrementalTreeImploder;
import org.spoofax.jsglr2.incremental.IncrementalParser;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

public abstract class JSGLR2BenchmarkIncrementalParsingAndImploding
    extends JSGLR2Benchmark<String[], IncrementalStringInput> {

    protected JSGLR2BenchmarkIncrementalParsingAndImploding(TestSet<String[], IncrementalStringInput> testSet) {
        super(new BenchmarkTestSetReader<>(testSet));
    }

    @Param({ "true" }) public boolean implode;

    @Param({ "DisjointSorted" }) ActionsForCharacterRepresentation actionsForCharacterRepresentation;

    @Param({ "JavaHashMap" }) ProductionToGotoRepresentation productionToGotoRepresentation;

    @Param({ "ArrayList" }) public ActiveStacksRepresentation activeStacksRepresentation;

    @Param({ "ArrayDeque" }) public ForActorStacksRepresentation forActorStacksRepresentation;

    @Param({ "Hybrid", "Incremental" }) public ParseForestRepresentation parseForestRepresentation;

    @Param({ "Full" }) public ParseForestConstruction parseForestConstruction;

    @Param({ "Hybrid" }) public StackRepresentation stackRepresentation;

    @Param({ "Basic" }) public Reducing reducing;

    @Param({ "TokenizedRecursive", "RecursiveIncremental" }) public ImploderVariant imploder;

    @Param({ "-1" }) public int i;

    Map<IncrementalStringInput, String> prevString = new HashMap<>();
    Map<IncrementalStringInput, IncrementalParseForest> prevResult = new HashMap<>();
    Map<IncrementalStringInput, WeakHashMap<IParseNode<IParseForest, IDerivation<IParseForest>>, TreeImploder.SubTree<IStrategoTerm>>> prevMap =
        new HashMap<>();

    @Setup public void setupCache() throws ParseException {
        if(i > 0) {
            for(IncrementalStringInput input : inputs) {
                if(jsglr2.parser instanceof IncrementalParser) {
                    String content = input.content[i - 1];
                    prevString.put(input, content);
                    prevResult.put(input,
                        ((IncrementalParseForest) jsglr2.parser.parseUnsafe(content, input.filename, null)));
                }
                if(jsglr2.imploder instanceof IncrementalTreeImploder) {
                    IncrementalTreeImploder<IParseForest, IParseNode<IParseForest, IDerivation<IParseForest>>, IDerivation<IParseForest>, IStrategoTerm, IncrementalImplodeInput<IParseNode<IParseForest, IDerivation<IParseForest>>, IStrategoTerm>> imploder =
                        (IncrementalTreeImploder) jsglr2.imploder;
                    imploder.implode(prevString.get(input), input.filename, prevResult.get(input));
                    prevMap.put(input, imploder.getFromCache(input.filename));
                }
            }
        }
    }

    @Override protected IntegrationVariant variant() {
        if(!implode)
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

    @Setup(Level.Invocation) public void setupRun() {
        if(jsglr2.parser instanceof IncrementalParser) {
            IncrementalParser parser = (IncrementalParser) jsglr2.parser;
            parser.clearCache();
            for(IncrementalStringInput input : inputs) {
                if(i > 0)
                    parser.addToCache(input.filename, prevString.get(input), prevResult.get(input));
            }
        }

        if(jsglr2.imploder instanceof IncrementalTreeImploder) {
            IncrementalTreeImploder imploder = (IncrementalTreeImploder) jsglr2.imploder;
            imploder.clearCache();
            for(IncrementalStringInput input : inputs) {
                if(i > 0)
                    imploder.addToCache(input.filename, new WeakHashMap<>(prevMap.get(input)));
            }
        }
    }

    @Override protected Object action(Blackhole bh, IncrementalStringInput input) throws ParseException {
        if(i >= 0)
            return jsglr2.parseUnsafe(input.content[i], input.filename, null);

        for(String content : input.content) {
            bh.consume(jsglr2.parseUnsafe(content, input.filename, null));
        }
        return null;
    }

}
