package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class StartSymbolTest extends BaseTestWithSdf3ParseTables {

    public StartSymbolTest() {
        super("start-symbol.sdf3");
    }

    @TestFactory public Stream<DynamicTest> withoutStartSymbol() throws ParseError {
        return testSuccessByExpansions(null, "foo", "amb([\"foo\", Id(\"foo\")])");
    }

    @TestFactory public Stream<DynamicTest> withStartSymbol() throws ParseError {
        return testSuccessByExpansions("Start", "foo", "Id(\"foo\")");
    }

}