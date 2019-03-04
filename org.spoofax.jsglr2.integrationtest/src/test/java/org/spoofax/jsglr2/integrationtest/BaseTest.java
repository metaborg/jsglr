package org.spoofax.jsglr2.integrationtest;

import static java.util.Collections.sort;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.integration.WithParseTable;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.util.AstUtilities;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class BaseTest implements WithParseTable {

    private TermReader termReader;
    protected AstUtilities astUtilities;

    protected BaseTest() {
        TermFactory termFactory = new TermFactory();
        this.termReader = new TermReader(termFactory);

        this.astUtilities = new AstUtilities();
    }

    public TermReader getTermReader() {
        return termReader;
    }

    protected IParseTable getParseTableFailOnException(JSGLR2Variants.ParseTableVariant variant) {
        try {
            return getParseTable(variant);
        } catch(Exception e) {
            e.printStackTrace();

            fail("Exception during reading parse table: " + e.getMessage());

            return null;
        }
    }

    public void testParseSuccess(String inputString) {
        for(JSGLR2Variants.Variant variant : JSGLR2Variants.testVariants()) {
            IParseTable parseTable = getParseTableFailOnException(variant.parseTable);
            Parser<?, ?, ?, ?, ?> parser = JSGLR2Variants.getParser(parseTable, variant.parser);

            ParseResult<?> parseResult = parser.parse(inputString);

            assertEquals("Variant '" + variant.name() + "' failed parsing: ", true, parseResult.isSuccess);
        }
    }

    public void testParseFailure(String inputString) {
        for(JSGLR2Variants.Variant variant : JSGLR2Variants.testVariants()) {
            IParseTable parseTable = getParseTableFailOnException(variant.parseTable);
            Parser<?, ?, ?, ?, ?> parser = JSGLR2Variants.getParser(parseTable, variant.parser);

            ParseResult<?> parseResult = parser.parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should fail: ", false, parseResult.isSuccess);
        }
    }

    protected IStrategoTerm testSuccess(IParseTable parseTable, JSGLR2Variants.ParserVariant variant,
        String startSymbol, String inputString) {
        JSGLR2<?, IStrategoTerm> jsglr2 = JSGLR2Variants.getJSGLR2(parseTable, variant);

        try {

            IStrategoTerm result = jsglr2.parseUnsafe(inputString, "", startSymbol);

            // Fail here if imploding or tokenization failed
            assertNotNull("Variant '" + variant.name() + "' failed imploding: ", result);

            return result;

        } catch(ParseException e) {

            // Fail here if parsing failed
            fail("Variant '" + variant.name() + "' failed parsing: " + e.failureType);

        }
        return null;
    }

    protected void testSuccessByAstString(String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, null, false);
    }

    protected void testSuccessByExpansions(String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, null, true);
    }

    protected void testSuccessByAstString(String startSymbol, String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, startSymbol, false);
    }

    protected void testSuccessByExpansions(String startSymbol, String inputString, String expectedOutputAstString) {
        testSuccess(inputString, expectedOutputAstString, startSymbol, true);
    }

    private void testSuccess(String inputString, String expectedOutputAstString, String startSymbol,
        boolean equalityByExpansions) {
        for(JSGLR2Variants.Variant variant : JSGLR2Variants.testVariants()) {
            IParseTable parseTable = getParseTableFailOnException(variant.parseTable);
            IStrategoTerm actualOutputAst = testSuccess(parseTable, variant.parser, startSymbol, inputString);

            if(equalityByExpansions) {
                IStrategoTerm expectedOutputAst = termReader.parseFromString(expectedOutputAstString);

                assertEqualTermExpansions(expectedOutputAst, actualOutputAst);
            } else {
                assertEquals(expectedOutputAstString, actualOutputAst.toString());
            }
        }
    }

    protected void assertEqualTermExpansions(IStrategoTerm expected, IStrategoTerm actual) {
        List<String> expectedExpansion = toSortedStringList(this.astUtilities.expand(expected));
        List<String> actualExpansion = toSortedStringList(this.astUtilities.expand(actual));

        assertEquals(expectedExpansion, actualExpansion);

    }

    protected void testTokens(String inputString, List<TokenDescriptor> expectedTokens) {
        for(JSGLR2Variants.Variant variant : JSGLR2Variants.testVariants()) {
            IParseTable parseTable = getParseTableFailOnException(variant.parseTable);
            JSGLR2<?, IStrategoTerm> jsglr2 = JSGLR2Variants.getJSGLR2(parseTable, variant.parser);

            JSGLR2Result<?, ?> jsglr2Result = jsglr2.parseResult(inputString, "", null);

            assertTrue("Variant '" + variant.name() + "' failed: ", jsglr2Result.isSuccess);

            List<TokenDescriptor> actualTokens = new ArrayList<>();

            for(IToken token : jsglr2Result.tokens) {
                actualTokens.add(TokenDescriptor.from(inputString, token));
            }

            TokenDescriptor expectedBeginToken = new TokenDescriptor("", IToken.TK_RESERVED, 0, 1, 1);
            TokenDescriptor actualBeginToken = actualTokens.get(0);

            assertEquals("Start token incorrect:", expectedBeginToken, actualBeginToken);

            Position endPosition = Position.atEnd(inputString);

            int endLine = endPosition.line;
            int endColumn = endPosition.column;

            TokenDescriptor expectedEndToken =
                new TokenDescriptor("", IToken.TK_EOF, inputString.length(), endLine, endColumn - 1);
            TokenDescriptor actualEndToken = actualTokens.get(actualTokens.size() - 1);

            List<TokenDescriptor> actualTokenWithoutStartAndEnd = actualTokens.subList(1, actualTokens.size() - 1);

            assertThat(actualTokenWithoutStartAndEnd, is(expectedTokens));

            assertEquals("End token incorrect:", expectedEndToken, actualEndToken);
        }
    }

    private List<String> toSortedStringList(List<IStrategoTerm> astExpansion) {
        List<String> result = new ArrayList<String>(astExpansion.size());

        for(IStrategoTerm ast : astExpansion) {
            result.add(ast.toString());
        }

        sort(result);

        return result;
    }

    protected String getFileAsString(String filename) throws IOException {
        byte[] encoded =
            Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("samples/" + filename).getPath()));

        return new String(encoded, StandardCharsets.UTF_8);
    }

    protected IStrategoTerm getFileAsAST(String filename) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("samples/" + filename);

        return this.termReader.parseFromStream(inputStream);
    }

}
