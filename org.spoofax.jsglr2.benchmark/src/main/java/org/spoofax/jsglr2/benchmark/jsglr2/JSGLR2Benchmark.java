package org.spoofax.jsglr2.benchmark.jsglr2;

import java.util.Arrays;
import java.util.List;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.characterclasses.ICharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoRepresentation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.JSGLR2Variants.ParseTableVariant;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.JSGLR2Variants.Variant;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.actions.IActionsFactory;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.states.IStateFactory;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.jsglr2.testset.Input;
import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2Benchmark extends BaseBenchmark {

    protected IParser<?, ?> parser; // Just parsing
    protected JSGLR2<?, ?> jsglr2; // Parsing and imploding (including tokenization)

    protected JSGLR2Benchmark(TestSet testSet) {
        super(testSet);
    }

    abstract protected Variant variant();

    abstract protected boolean implode();

    @Setup public void parserSetup() throws ParseError, ParseTableReadException {
        Variant variant = variant();

        filterVariants(implode(), variant);

        IStateFactory stateFactory = new StateFactory(StateFactory.defaultActionsForCharacterRepresentation,
            StateFactory.defaultProductionToGotoRepresentation);

        IActionsFactory actionsFactory = new ActionsFactory(true);
        ICharacterClassFactory characterClassFactory = new CharacterClassFactory(true, true);

        IParseTable parseTable = new ParseTableReader(characterClassFactory, actionsFactory, stateFactory)
            .read(testSetReader.getParseTableTerm());

        // IParseTable parseTable = new ParseTableReader(characterClassFactory,
        // variant.parseTable.actionsForCharacterRepresentation,
        // variant.parseTable.productionToGotoRepresentation).read(testSetReader.getParseTableTerm());

        parser = JSGLR2Variants.getParser(parseTable, variant.parser);
        jsglr2 = JSGLR2Variants.getJSGLR2(parseTable, variant.parser);
    }

    //@formatter:off
    static ParserVariant naiveParserVariant = new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic);
    
    static ParseTableVariant naiveTableVariant     = new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop);
    static ParseTableVariant bestParseTableVariant = new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.JavaHashMap);
    
    static List<JSGLR2Variants.Variant> benchmarkParseVariants = Arrays.asList(
        // Variants for parse table variants
        new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),                        naiveParserVariant),
        new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.JavaHashMap),                    naiveParserVariant),
        new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.ForLoop),                        naiveParserVariant),
        new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.JavaHashMap),                    naiveParserVariant),
        
        // Variants for parser variants
        // - Stack collections
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList,        ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic,  Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayListHashMap, ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.LinkedHashMap,    ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList,        ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayListHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.LinkedHashMap,    ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic)),
        
        // - Data structures
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Basic,  Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Hybrid, Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.Basic,  Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.Hybrid, Reducing.Basic)),

        // - Elkhound
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound, Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound, Reducing.Elkhound)),
        
        // - Parse forest construction
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.Hybrid, Reducing.Basic)),

        // - Best
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Elkhound)),

        // - Naive
        new Variant(naiveTableVariant, naiveParserVariant)
    );
    
    static List<JSGLR2Variants.Variant> benchmarkParseAndImplodeVariants = Arrays.asList(
        new Variant(naiveTableVariant, naiveParserVariant),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.Hybrid, Reducing.Basic)),
        new Variant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Elkhound))
    );
    //@formatter:on

    public static void filterVariants(boolean implode, Variant variant) {
        if(!implode && !benchmarkParseVariants.contains(variant))
            throw new IllegalStateException("this variant is not used for benchmarking");

        if(implode && !benchmarkParseAndImplodeVariants.contains(variant))
            throw new IllegalStateException("this variant is not used for benchmarking");
    }

    @Benchmark public void benchmark(Blackhole bh) throws ParseException {
        if(implode()) {
            if(variant().parser.parseForestRepresentation == ParseForestRepresentation.Null)
                throw new IllegalStateException("imploding requires a parse forest");

            for(Input input : inputs)
                bh.consume(jsglr2.parseUnsafe(input.content, input.filename, null));
        } else {
            for(Input input : inputs)
                bh.consume(parser.parseUnsafe(input.content, input.filename, null));
        }
    }

}
