package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithLayoutSensitiveSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LayoutSensitivePositionsTest extends BaseTestWithLayoutSensitiveSdf3ParseTables {

    public LayoutSensitivePositionsTest() {
        super("layout-sensitive-positions.sdf3");
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine1Col1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("l", "LiteralFirstLine1Col1()", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" l", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\nl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n\rl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" \nl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" \n\rl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n l", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n\r l", "LiteralFirstLine1Col1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine1Col2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("l", "LiteralFirstLine1Col2"),
            testLayoutSensitiveSuccessByExpansions(" l", "LiteralFirstLine1Col2()", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\nl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n\rl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered(" \nl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered(" \n\rl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n l", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n\r l", "LiteralFirstLine1Col2")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine2Col1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("l", "LiteralFirstLine2Col1"),
            testLayoutSensitiveParseFiltered(" l", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions("\nl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions("\n\rl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions(" \nl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions(" \n\rl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveParseFiltered("\n l", "LiteralFirstLine2Col1"),
            testLayoutSensitiveParseFiltered("\n\r l", "LiteralFirstLine2Col1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine2Col2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("l", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" l", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered("\nl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered("\n\rl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" \nl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" \n\rl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveSuccessByExpansions("\n l", "LiteralFirstLine2Col2()", "LiteralFirstLine2Col2"),
            testLayoutSensitiveSuccessByExpansions("\n\r l", "LiteralFirstLine2Col2()", "LiteralFirstLine2Col2")
        );
        //@formatter:on
    }

}
