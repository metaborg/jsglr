package org.spoofax.jsglr2.integrationtest;

import static org.junit.Assert.assertEquals;

import java.util.function.Predicate;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parser.result.ParseResult;

public abstract class BaseTestWithRecoverySdf3ParseTables extends BaseTestWithSdf3ParseTables {

    protected BaseTestWithRecoverySdf3ParseTables(String sdf3Resource) {
        super(sdf3Resource);
    }

    @Override public IParseTable getParseTable(ParseTableVariant variant, String sdf3Resource) throws Exception {
        return sdf3ToParseTable.getParseTable(variant, sdf3Resource);
    }

    private Predicate<TestVariant> isRecoveryVariant = testVariant -> testVariant.variant.parser.recovery;

    private Predicate<TestVariant> isNotRecoveryVariant = testVariant -> !testVariant.variant.parser.recovery;

    protected void testRecovery(String inputString) {
        for(TestVariant variant : getTestVariants(isNotRecoveryVariant)) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should fail for non-recovering parsing: ", false,
                parseResult.isSuccess());
        }

        for(TestVariant variant : getTestVariants(isRecoveryVariant)) {
            ParseResult<?> parseResult = variant.parser().parse(inputString);

            assertEquals("Variant '" + variant.name() + "' should succeed for recovering parsing: ", true,
                parseResult.isSuccess());
        }
    }

}
