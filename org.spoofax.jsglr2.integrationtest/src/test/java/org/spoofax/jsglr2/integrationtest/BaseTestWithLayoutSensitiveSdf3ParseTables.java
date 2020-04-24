package org.spoofax.jsglr2.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
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

    private Predicate<TestVariant> isLayoutSensitiveVariant = testVariant -> {
        ParseForestRepresentation parseForestRepresentation = testVariant.variant.parser.parseForestRepresentation;

        return parseForestRepresentation.equals(ParseForestRepresentation.LayoutSensitive)
            || parseForestRepresentation.equals(ParseForestRepresentation.Composite);
    };

    private Predicate<TestVariant> isNotLayoutSensitiveVariant = isLayoutSensitiveVariant.negate();

    protected Stream<DynamicTest> testLayoutSensitiveParseFiltered(String inputString) {
        Stream<DynamicTest> notLayoutSensitiveTests =
            testPerVariant(getTestVariants(isNotLayoutSensitiveVariant), variant -> () -> {
                ParseResult<?> parseResult = variant.parser().parse(inputString);

                assertEquals(true, parseResult.isSuccess(),
                    "Variant '" + variant.name() + "' should succeed for non-layout-sensitive parsing: ");
            });

        Stream<DynamicTest> layoutSensitiveTests =
            testPerVariant(getTestVariants(isLayoutSensitiveVariant), variant -> () -> {
                ParseResult<?> parseResult = variant.parser().parse(inputString);

                assertEquals(false, parseResult.isSuccess(),
                    "Variant '" + variant.name() + "' should fail for layout-sensitive parsing: ");
            });

        return Stream.concat(notLayoutSensitiveTests, layoutSensitiveTests);
    }

    protected Stream<DynamicTest> testLayoutSensitiveSuccessByExpansions(String inputString,
        String expectedOutputAstString) {
        return testLayoutSensitiveSuccess(inputString, expectedOutputAstString, null, true);
    }

    private Stream<DynamicTest> testLayoutSensitiveSuccess(String inputString, String expectedOutputAstString,
        String startSymbol, boolean equalityByExpansions) {
        return testPerVariant(getTestVariants(isLayoutSensitiveVariant), variant -> () -> {
            IStrategoTerm actualOutputAst = testSuccess(variant, startSymbol, inputString);

            assertEqualAST("Variant '" + variant.name() + "' has incorrect AST", expectedOutputAstString,
                actualOutputAst, equalityByExpansions);
        });
    }

}
