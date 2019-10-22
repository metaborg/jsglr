package org.spoofax.jsglr2.integrationtest.features;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class StartSymbolTest extends BaseTestWithSdf3ParseTables {

    public StartSymbolTest() {
        super("start-symbol.sdf3");
    }

    @Test public void withoutStartSymbol() throws ParseError {
        testSuccessByExpansions(null, "foo", "amb([\"foo\", Id(\"foo\")])");
    }

    @Test public void withStartSymbol() throws ParseError {
        testSuccessByExpansions("Start", "foo", "Id(\"foo\")");
    }

}