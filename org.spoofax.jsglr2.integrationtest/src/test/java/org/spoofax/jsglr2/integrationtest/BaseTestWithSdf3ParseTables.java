package org.spoofax.jsglr2.integrationtest;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.metaborg.core.MetaborgException;
import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableVariant;
import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.sdf2table.io.ParseTableIO;
import org.metaborg.sdf2table.parsetable.ParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integration.Sdf3ToParseTable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public abstract class BaseTestWithSdf3ParseTables extends BaseTest {

    protected String sdf3Resource;
    protected static Table<String, ParseTableVariant, IParseTable> parseTableTable = HashBasedTable.create();
    protected static Table<String, ParseTableVariant, Iterable<ParseTableWithOrigin>> parseTablesCache =
        HashBasedTable.create();

    protected BaseTestWithSdf3ParseTables(String sdf3Resource) {
        this.sdf3Resource = sdf3Resource;
    }

    protected static Sdf3ToParseTable sdf3ToParseTable;

    @BeforeAll public static void setup() throws MetaborgException {
        sdf3ToParseTable = new Sdf3ToParseTable(
            resource -> BaseTestWithSdf3ParseTables.class.getClassLoader().getResource(resource).getPath());
    }

    public ParseTableWithOrigin getParseTable(ParseTableVariant variant) throws Exception {
        if(!parseTableTable.contains(sdf3Resource, variant)) {
            parseTableTable.put(sdf3Resource, variant, getParseTable(variant, sdf3Resource));
        }
        return new ParseTableWithOrigin(parseTableTable.get(sdf3Resource, variant), ParseTableOrigin.Sdf3Generation);
    }

    public IParseTable getParseTable(ParseTableVariant variant, String sdf3Resource) throws Exception {
        return sdf3ToParseTable.getParseTable(variant, sdf3Resource);
    }

    @Override public Iterable<ParseTableWithOrigin> getParseTables(ParseTableVariant variant) throws Exception {
        if(!parseTablesCache.contains(sdf3Resource, variant)) {
            ParseTableWithOrigin parseTableWithOrigin = getParseTable(variant);

            IStrategoTerm parseTableTerm = ParseTableIO.generateATerm((ParseTable) parseTableWithOrigin.parseTable);

            ParseTableWithOrigin parseTableWithOriginSerializedDeserialized =
                new ParseTableWithOrigin(variant.parseTableReader().read(parseTableTerm), ParseTableOrigin.ATerm);

            // Ensure that the parse table that directly comes from the generation behaves the same after
            // serialization/deserialization to/from term format
            parseTablesCache.put(sdf3Resource, variant,
                Arrays.asList(parseTableWithOrigin, parseTableWithOriginSerializedDeserialized));
        }
        return parseTablesCache.get(sdf3Resource, variant);
    }

    @TestFactory public Stream<DynamicTest> testParseTableConsistency() throws Exception {
        Iterator<ParseTableWithOrigin> parseTables = getParseTables(ParseTableVariant.standard()).iterator();

        ParseTableWithOrigin parseTableWithOrigin = parseTables.next();

        Stream<DynamicTest> tests = Stream.empty();

        while(parseTables.hasNext()) {
            ParseTableWithOrigin other = parseTables.next();

            tests = Stream.concat(tests, testParseTableConsistency(parseTableWithOrigin, other));
        }

        return tests;
    }

    private Stream<DynamicTest> testParseTableConsistency(ParseTableWithOrigin a, ParseTableWithOrigin b) {
        Stream.Builder<DynamicTest> tests = Stream.builder();

        tests.accept(testConsistency("number of productions", a, b, parseTable -> parseTable.productions().size()));

        for(int i = 0; i < a.parseTable.productions().size(); i++) {
            // @formatter:off
            tests.accept(testProductionConsistency("id", a, b, i, IProduction::id));
            tests.accept(testProductionConsistency("sort", a, b, i, IProduction::sort));
            tests.accept(testProductionConsistency("start symbol sort", a, b, i, IProduction::startSymbolSort));
            tests.accept(testProductionConsistency("constructor", a, b, i, IProduction::constructor));
            tests.accept(testProductionConsistency("concrete syntax context", a, b, i, IProduction::concreteSyntaxContext));
            tests.accept(testProductionConsistency("is context-free", a, b, i, IProduction::isContextFree));
            tests.accept(testProductionConsistency("is layout", a, b, i, IProduction::isLayout));
            tests.accept(testProductionConsistency("is literal", a, b, i, IProduction::isLiteral));
            tests.accept(testProductionConsistency("is lexical", a, b, i, IProduction::isLexical));
            tests.accept(testProductionConsistency("is skippable in parse forest", a, b, i, IProduction::isSkippableInParseForest));
            tests.accept(testProductionConsistency("is list", a, b, i, IProduction::isList));
            tests.accept(testProductionConsistency("is optional", a, b, i, IProduction::isOptional));
            tests.accept(testProductionConsistency("is recovery", a, b, i, IProduction::isRecovery));
            tests.accept(testProductionConsistency("is water", a, b, i, IProduction::isWater));
            tests.accept(testProductionConsistency("is insertion", a, b, i, IProduction::isInsertion));
            tests.accept(testProductionConsistency("is completion", a, b, i, IProduction::isCompletion));
            tests.accept(testProductionConsistency("is string literal", a, b, i, IProduction::isStringLiteral));
            tests.accept(testProductionConsistency("is number literal", a, b, i, IProduction::isNumberLiteral));
            tests.accept(testProductionConsistency("is operator", a, b, i, IProduction::isOperator));
            tests.accept(testProductionConsistency("is ignore layout constraint", a, b, i, IProduction::isIgnoreLayoutConstraint));
            tests.accept(testProductionConsistency("is longest-match", a, b, i, IProduction::isLongestMatch));
            tests.accept(testProductionConsistency("is bracket", a, b, i, IProduction::isBracket));
            // @formatter:on
        }

        return tests.build();
    }

    private <T> DynamicTest testConsistency(String property, ParseTableWithOrigin a, ParseTableWithOrigin b,
        Function<IParseTable, T> get) {
        return DynamicTest.dynamicTest(property, () -> {
            T ta = get.apply(a.parseTable);
            T tb = get.apply(b.parseTable);

            if(!Objects.equals(ta, tb))
                fail("Inconsistent " + property + ": " + ta + " (" + a.origin.name() + ") != " + tb + " ("
                    + b.origin.name() + ")");
        });
    }

    private <T> DynamicTest testProductionConsistency(String property, ParseTableWithOrigin a, ParseTableWithOrigin b,
        int index, Function<IProduction, T> get) {
        IProduction production = a.parseTable.productions().get(index);

        return testConsistency("production (" + production.id() + ": " + production.toString() + ") " + property, a, b,
            parseTable -> get.apply(parseTable.productions().get(index)));
    }

}
