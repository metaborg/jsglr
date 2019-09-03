package org.spoofax.jsglr2.benchmark.jsglr2;

import java.util.Arrays;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.spoofax.jsglr2.JSGLR2Implementation;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.benchmark.BaseBenchmark;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.testset.TestSetReader;
import org.spoofax.jsglr2.tokens.TokenizerVariant;
import org.spoofax.terms.ParseError;

public abstract class JSGLR2Benchmark<Input> extends BaseBenchmark<Input> {

    protected IParser<?> parser; // Just parsing
    protected JSGLR2Implementation<?, ?, ?> jsglr2; // Parsing, imploding, and tokenization

    public JSGLR2Benchmark(TestSetReader<Input> testSetReader) {
        super(testSetReader);
    }

    abstract protected IntegrationVariant variant();

    abstract protected boolean implode();

    abstract protected Object action(Input input) throws ParseException;

    @Setup public void parserSetup() throws ParseError, ParseTableReadException {
        IntegrationVariant variant = variant();

        filterVariants(implode(), variant);

        IParseTable parseTable = variant.parseTable.parseTableReader().read(testSetReader.getParseTableTerm());

        parser = JSGLR2Variants.getParser(parseTable, variant.parser);
        jsglr2 = (JSGLR2Implementation<?, ?, ?>) JSGLR2Variants.getJSGLR2(parseTable, variant.jsglr2);
    }

    //@formatter:off
    static ParseTableVariant naiveTableVariant     = new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop);
    static ParseTableVariant bestParseTableVariant = new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.JavaHashMap);

    static ParserVariant naiveParserVariant = new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic);
    
    static ImploderVariant imploderVariant = ImploderVariant.TokenizedRecursive;
    static TokenizerVariant tokenizerVariant = TokenizerVariant.Null;
    
    static List<IntegrationVariant> benchmarkParseVariants = Arrays.asList(
        // Variants for parse table variants
        new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),                        naiveParserVariant, imploderVariant, tokenizerVariant),
        new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.JavaHashMap),                    naiveParserVariant, imploderVariant, tokenizerVariant),
        new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.ForLoop),                        naiveParserVariant, imploderVariant, tokenizerVariant),
        new IntegrationVariant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.JavaHashMap),                    naiveParserVariant, imploderVariant, tokenizerVariant),
        
        // Variants for parser variants
        // - Stack collections
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList,        ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayListHashMap, ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.LinkedHashMap,    ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList,        ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayListHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.LinkedHashMap,    ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Basic, ParseForestConstruction.Full, StackRepresentation.Basic, Reducing.Basic), imploderVariant, tokenizerVariant),
        
        // - Data structures
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Basic,  Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Hybrid, Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.Basic,  Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.Hybrid, Reducing.Basic), imploderVariant, tokenizerVariant),

        // - Elkhound
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound, Reducing.Basic),    imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound, Reducing.Elkhound), imploderVariant, tokenizerVariant),
        
        // - Parse forest construction
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.Hybrid, Reducing.Basic), imploderVariant, tokenizerVariant),

        // - Best
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Elkhound), imploderVariant, tokenizerVariant),

        // - Naive
        new IntegrationVariant(naiveTableVariant, naiveParserVariant, imploderVariant, tokenizerVariant)
    );
    
    static List<IntegrationVariant> benchmarkParseAndImplodeVariants = Arrays.asList(
        new IntegrationVariant(naiveTableVariant, naiveParserVariant, imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.Hybrid, Reducing.Basic), imploderVariant, tokenizerVariant),
        new IntegrationVariant(bestParseTableVariant, new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Elkhound), imploderVariant, tokenizerVariant)
    );
    //@formatter:on

    public static void filterVariants(boolean implode, IntegrationVariant variant) {
        if(!implode && !benchmarkParseVariants.contains(variant))
            throw new IllegalStateException("this variant is not used for benchmarking");

        if(implode && !benchmarkParseAndImplodeVariants.contains(variant))
            throw new IllegalStateException("this variant is not used for benchmarking");

        if(implode && variant.parser.parseForestRepresentation == ParseForestRepresentation.Null)
            throw new IllegalStateException("imploding requires a parse forest");
    }

    @Benchmark public void benchmark(Blackhole bh) throws ParseException {
        for(Input input : inputs)
            bh.consume(action(input));
    }

}
