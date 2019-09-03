package org.spoofax.jsglr2.integrationtest;

import static org.junit.Assert.assertEquals;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.result.ParseResult;

public abstract class BaseTestWithLayoutSensitiveSdf3ParseTables extends BaseTestWithSdf3ParseTables {

    protected BaseTestWithLayoutSensitiveSdf3ParseTables(String sdf3Resource) {
        super(sdf3Resource);
    }

    @Override public IParseTable getParseTable(ParseTableVariant variant, String sdf3Resource) throws Exception {
        return sdf3ToParseTable.getLayoutSensitiveParseTable(variant, sdf3Resource);
    }

    protected void testLayoutSensitiveParseFailure(String inputString) {
        for(TestVariant variant : getTestVariants()) {
            if(!variant.variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.LayoutSensitive))
                continue;

            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should fail: ", false, parseResult.isSuccess());
        }
    }

    protected void testLayoutSensitiveSuccessByExpansions(String inputString, String expectedOutputAstString) {
        testLayoutSensitiveSuccess(inputString, expectedOutputAstString, null, true);
    }

    private void testLayoutSensitiveSuccess(String inputString, String expectedOutputAstString, String startSymbol,
        boolean equalityByExpansions) {
        for(TestVariant variant : getTestVariants()) {
            if(!variant.variant.parser.parseForestRepresentation.equals(ParseForestRepresentation.LayoutSensitive))
                continue;

            IStrategoTerm actualOutputAst = testSuccess(variant, startSymbol, inputString);

            assertEqualAST("Variant '" + variant.name() + "' has incorrect AST", expectedOutputAstString,
                actualOutputAst, equalityByExpansions);
        }
    }

}
