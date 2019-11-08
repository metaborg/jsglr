package org.spoofax.jsglr2.integrationtest;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.JSGLR2Success;
import org.spoofax.jsglr2.integration.IntegrationVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.integration.WithParseTable;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.util.AstUtilities;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.sort;
import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTest implements WithParseTable {

    private static TermReader termReader = new TermReader(new TermFactory());
    private static AstUtilities astUtilities = new AstUtilities();

    protected BaseTest() {
    }

    public TermReader getTermReader() {
        return termReader;
    }

    protected Iterable<ParseTableWithOrigin> getParseTablesOrFailOnException(ParseTableVariant variant) {
        try {
            return getParseTables(variant);
        } catch(Exception e) {
            e.printStackTrace();

            fail("Exception during reading parse table: " + e.getMessage());

            return null;
        }
    }

    class TestVariant {

        IntegrationVariant variant;
        ParseTableWithOrigin parseTableWithOrigin;

        TestVariant(IntegrationVariant variant, ParseTableWithOrigin parseTableWithOrigin) {
            this.variant = variant;
            this.parseTableWithOrigin = parseTableWithOrigin;
        }

        String name() {
            return variant.name() + "(parseTableOrigin:" + parseTableWithOrigin.origin + ")";
        }

        IParser<?> parser() {
            return variant.parser.getParser(parseTableWithOrigin.parseTable);
        }

        JSGLR2<IStrategoTerm> jsglr2() {
            return variant.jsglr2.getJSGLR2(parseTableWithOrigin.parseTable);
        }

    }

    protected Iterable<TestVariant> getTestVariants(Predicate<TestVariant> filter) {
        List<TestVariant> testVariants = new ArrayList<>();

        for(IntegrationVariant variant : IntegrationVariant.testVariants()) {
            for(ParseTableWithOrigin parseTableWithOrigin : getParseTablesOrFailOnException(variant.parseTable)) {
                TestVariant testVariant = new TestVariant(variant, parseTableWithOrigin);

                // data-dependent, layout-sensitive, and composite parsers are incompatible with Aterm parse table
                if((variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.DataDependent)
                    || variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.LayoutSensitive)
                    || variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.Composite))
                    && parseTableWithOrigin.origin.equals(ParseTableOrigin.ATerm)) {
                    continue;
                }

                if(filter.test(testVariant))
                    testVariants.add(testVariant);
            }
        }

        return testVariants;
    }

    protected Iterable<TestVariant> getTestVariants() {
        return getTestVariants(testVariant -> true);
    }

    protected void testParseSuccess(String inputString) {
        testParseSuccess(inputString, getTestVariants());
    }

    protected void testParseSuccess(String inputString, Iterable<TestVariant> variants) {
        for(TestVariant variant : variants) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals(true, parseResult.isSuccess(), "Variant '" + variant.name() + "' failed parsing: ");
        }
    }

    protected void testParseFailure(String inputString) {
        testParseFailure(inputString, getTestVariants());
    }

    protected void testParseFailure(String inputString, Iterable<TestVariant> variants) {
        for(TestVariant variant : variants) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals(false, parseResult.isSuccess(), "Variant '" + variant.name() + "' should fail: ");
        }
    }

    protected void testSuccessByAstString(String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, null, false);
    }

    protected void testSuccessByExpansions(String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, null, true);
    }

    protected void testIncrementalSuccessByExpansions(String[] inputStrings, String[] expectedOutputAstStrings) {
        testIncrementalSuccess(inputStrings, expectedOutputAstStrings, null, true);
    }

    protected void testSuccessByAstString(String startSymbol, String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, startSymbol, false);
    }

    protected void testSuccessByExpansions(String startSymbol, String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, startSymbol, true);
    }

    private void testSuccess(String inputString, String expectedOutputAstString, String startSymbol,
        boolean equalityByExpansions) {
        for(TestVariant variant : getTestVariants()) {
            IStrategoTerm actualOutputAst = testSuccess(variant, startSymbol, inputString);

            assertEqualAST("Variant '" + variant.name() + "' has incorrect AST", expectedOutputAstString,
                actualOutputAst, equalityByExpansions);
        }
    }

    protected IStrategoTerm testSuccess(TestVariant variant, String startSymbol, String inputString) {
        return testSuccess("Variant '" + variant.name() + "' failed parsing: ",
            "Variant '" + variant.name() + "' failed imploding: ", variant.jsglr2(), "", startSymbol, inputString);
    }

    private IStrategoTerm testSuccess(String parseFailMessage, String implodeFailMessage, JSGLR2<IStrategoTerm> jsglr2,
        String filename, String startSymbol, String inputString) {
        try {
            IStrategoTerm result = jsglr2.parseUnsafe(inputString, filename, startSymbol);

            // Fail here if imploding or tokenization failed
            assertNotNull(result, implodeFailMessage);

            return result;
        } catch(ParseException e) {
            // Fail here if parsing failed
            fail(parseFailMessage + e.failureType);
        }
        return null;
    }

    protected Predicate<TestVariant> isIncrementalVariant =
        testVariant -> testVariant.variant.parser.parseForestRepresentation == ParseForestRepresentation.Incremental;

    private void testIncrementalSuccess(String[] inputStrings, String[] expectedOutputAstStrings, String startSymbol,
        boolean equalityByExpansions) {
        testIncrementalSuccess(inputStrings, expectedOutputAstStrings, startSymbol, equalityByExpansions,
            getTestVariants(isIncrementalVariant));
    }

    private void testIncrementalSuccess(String[] inputStrings, String[] expectedOutputAstStrings, String startSymbol,
        boolean equalityByExpansions, Iterable<TestVariant> variants) {
        for(TestVariant variant : variants) {
            IStrategoTerm actualOutputAst;
            String filename = "" + System.nanoTime(); // To ensure the results will be cached
            for(int i = 0; i < expectedOutputAstStrings.length; i++) {
                String inputString = inputStrings[i];
                actualOutputAst = testSuccess("Variant '" + variant.name() + "' failed parsing at update " + i + ": ",
                    "Variant '" + variant.name() + "' failed imploding at update " + i + ": ", variant.jsglr2(),
                    filename, startSymbol, inputString);
                assertEqualAST("Variant '" + variant.name() + "' has incorrect AST at update " + i + ": ",
                    expectedOutputAstStrings[i], actualOutputAst, equalityByExpansions);
            }
        }
    }

    protected void assertEqualAST(String message, String expectedOutputAstString, IStrategoTerm actualOutputAst,
        boolean equalityByExpansions) {
        if(equalityByExpansions) {
            IStrategoTerm expectedOutputAst = termReader.parseFromString(expectedOutputAstString);

            assertEqualTermExpansions(message, expectedOutputAst, actualOutputAst);
        } else {
            assertEquals(expectedOutputAstString, actualOutputAst.toString(), message);
        }
    }

    protected static void assertEqualTermExpansions(IStrategoTerm expected, IStrategoTerm actual) {
        assertEqualTermExpansions(null, expected, actual);
    }

    protected static void assertEqualTermExpansions(String message, IStrategoTerm expected, IStrategoTerm actual) {
        List<String> expectedExpansion = toSortedStringList(astUtilities.expand(expected));
        List<String> actualExpansion = toSortedStringList(astUtilities.expand(actual));

        assertEquals(expectedExpansion, actualExpansion, message);
    }

    private static List<String> toSortedStringList(List<IStrategoTerm> astExpansion) {
        List<String> result = new ArrayList<>(astExpansion.size());

        for(IStrategoTerm ast : astExpansion) {
            result.add(ast.toString());
        }

        sort(result);

        return result;
    }

    protected void testTokens(String inputString, List<TokenDescriptor> expectedTokens) {
        testTokens(inputString, expectedTokens, getTestVariants());
    }

    protected void testTokens(String inputString, List<TokenDescriptor> expectedTokens,
        Iterable<TestVariant> variants) {
        for(TestVariant variant : variants) {
            JSGLR2Result<?> jsglr2Result = variant.jsglr2().parseResult(inputString, "", null);

            String variantPrefix = "Variant '" + variant.name() + "' failed";
            assertTrue(jsglr2Result.isSuccess(), variantPrefix);

            JSGLR2Success<?> jsglr2Success = (JSGLR2Success<?>) jsglr2Result;

            List<TokenDescriptor> actualTokens = new ArrayList<>();

            for(IToken token : jsglr2Success.tokens) {
                actualTokens.add(TokenDescriptor.from(inputString, token));
            }

            TokenDescriptor expectedStartToken = new TokenDescriptor("", IToken.TK_RESERVED, 0, 1, 1, null, null);
            TokenDescriptor actualStartToken = actualTokens.get(0);

            assertEquals(expectedStartToken, actualStartToken, variantPrefix + "\nStart token incorrect:");

            Position endPosition = Position.atEnd(inputString);

            int endLine = endPosition.line;
            int endColumn = endPosition.column;

            TokenDescriptor expectedEndToken =
                new TokenDescriptor("", IToken.TK_EOF, inputString.length(), endLine, endColumn - 1, null, null);
            TokenDescriptor actualEndToken = actualTokens.get(actualTokens.size() - 1);

            List<TokenDescriptor> actualTokensWithoutStartAndEnd = actualTokens.subList(1, actualTokens.size() - 1);

            assertIterableEquals(actualTokensWithoutStartAndEnd, expectedTokens,
                variantPrefix + "\nToken lists don't match:");

            assertEquals(expectedEndToken, actualEndToken, variantPrefix + "\nEnd token incorrect:");
        }
    }

    protected String getFileAsString(String filename) throws IOException {
        byte[] encoded =
            Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("samples/" + filename).getPath()));

        return new String(encoded, StandardCharsets.UTF_8);
    }

    protected IStrategoTerm getFileAsAST(String filename) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("samples/" + filename);

        return termReader.parseFromStream(inputStream);
    }

}
