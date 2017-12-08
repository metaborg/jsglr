package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.net.URISyntaxException;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.states.ActionsForCharacterRepresentation;
import org.spoofax.jsglr2.states.ProductionToGotoRepresentation;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2Benchmark extends BaseBenchmark {

    protected IParser<?, ?> parser; // Just parsing
    protected JSGLR2<?, ?> jsglr2; // Parsing and imploding (including tokenization)

    protected JSGLR2Benchmark(TestSet testSet) {
        super(testSet);
    }

    @Param({ "false", "true" }) public boolean implode;

    @Param({ "Separated", "DisjointSorted" }) ActionsForCharacterRepresentation actionsForCharacterRepresentation;

    @Param({ "ForLoop", "CapsuleImmutableBinaryRelation",
        "JavaHashMap" }) ProductionToGotoRepresentation productionToGotoRepresentation;

    @Param({ "ArrayList", "ArrayListHashMap",
        "LinkedHashMap" }) public ActiveStacksRepresentation activeStacksRepresentation;

    @Param({ "ArrayDeque", "LinkedHashMap" }) public ForActorStacksRepresentation forActorStacksRepresentation;

    @Param({ "Null", "Basic", "Hybrid" }) public ParseForestRepresentation parseForestRepresentation;

    @Param({ "Full", "Optimized" }) public ParseForestConstruction parseForestConstruction;

    @Param({ "Basic", "Hybrid", "BasicElkhound", "HybridElkhound" }) public StackRepresentation stackRepresentation;

    @Param({ "Basic", "Elkhound" }) public Reducing reducing;

    @Setup
    public void parserSetup() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        IParseTable parseTable = new ParseTableReader(actionsForCharacterRepresentation, productionToGotoRepresentation)
            .read(testSetReader.getParseTableTerm());

        parser = JSGLR2Variants.getParser(parseTable, activeStacksRepresentation, forActorStacksRepresentation,
            parseForestRepresentation, parseForestConstruction, stackRepresentation, reducing);
        jsglr2 = JSGLR2Variants.getJSGLR2(parseTable, activeStacksRepresentation, forActorStacksRepresentation,
            parseForestRepresentation, parseForestConstruction, stackRepresentation, reducing);
    }

    @Benchmark
    public void benchmark(Blackhole bh) throws ParseException {
        if(implode) {
            if(parseForestRepresentation == ParseForestRepresentation.Null) {
                throw new IllegalStateException("imploding requires a parse forest");
            }

            for(Input input : inputs) {
                final Object result = jsglr2.parseUnsafe(input.content, input.filename, null);
                bh.consume(result);
            }
        } else {
            for(Input input : inputs) {
                final AbstractParseForest forest = parser.parseUnsafe(input.content, input.filename, null);
                bh.consume(forest);
            }
        }
    }

}
