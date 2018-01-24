package org.spoofax.jsglr2.tests.util;

import static java.util.Collections.sort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.parser.ParseResult;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.states.StateFactory;
import org.spoofax.jsglr2.util.AstUtilities;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class BaseTest {

    private TermReader termReader;
    private IStrategoTerm parseTableTerm;

    protected AstUtilities astUtilities;

    protected BaseTest() {
        TermFactory termFactory = new TermFactory();
        this.termReader = new TermReader(termFactory);

        this.astUtilities = new AstUtilities();
    }

    public TermReader getTermReader() {
        return termReader;
    }

    public void setParseTableTerm(IStrategoTerm parseTableTerm) {
        this.parseTableTerm = parseTableTerm;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }

    protected IParseTable getParseTable(JSGLR2Variants.ParseTableVariant variant) {
        try {
            return new ParseTableReader(new CharacterClassFactory(true, true), new ActionsFactory(true),
                new StateFactory(variant.actionsForCharacterRepresentation, variant.productionToGotoRepresentation))
                    .read(getParseTableTerm());
        } catch(ParseTableReadException e) {
            e.printStackTrace();

            fail("ParseTableReadException: " + e.getMessage());

            return null;
        }
    }

    public void testParseSuccess(String inputString) {
        for(JSGLR2Variants.Variant variant : JSGLR2Variants.testVariants()) {
            IParseTable parseTable = getParseTable(variant.parseTable);
            Parser<?, ?, ?, ?> parser = JSGLR2Variants.getParser(parseTable, variant.parser);

            ParseResult<?, ?> parseResult = parser.parse(inputString);

            assertEquals("Variant '" + variant.name() + "' failed: ", true, parseResult.isSuccess);
        }
    }

    public void testParseFailure(String inputString) {
        for(JSGLR2Variants.Variant variant : JSGLR2Variants.testVariants()) {
            IParseTable parseTable = getParseTable(variant.parseTable);
            Parser<?, ?, ?, ?> parser = JSGLR2Variants.getParser(parseTable, variant.parser);

            ParseResult<?, ?> parseResult = parser.parse(inputString);

            assertEquals("Variant '" + variant.name() + "' failed: ", false, parseResult.isSuccess);
        }
    }

    protected IStrategoTerm testSuccess(IParseTable parseTable, JSGLR2Variants.ParserVariant variant,
        String startSymbol, String inputString) {
        JSGLR2<?, IStrategoTerm> jsglr2 = JSGLR2Variants.getJSGLR2(parseTable, variant);

        ParseResult<?, ?> parseResult = jsglr2.parser.parse(inputString, "", startSymbol);

        assertEquals("Variant '" + variant.name() + "' failed parsing: ", true, parseResult.isSuccess); // Fail here if
                                                                                                        // parsing
                                                                                                        // failed

        IStrategoTerm result = jsglr2.parse(inputString, "", startSymbol);

        assertNotNull("Variant '" + variant.name() + "' failed imploding: ", result); // Fail here if imploding or
                                                                                      // tokenization failed

        return result;
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
            IParseTable parseTable = getParseTable(variant.parseTable);
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
