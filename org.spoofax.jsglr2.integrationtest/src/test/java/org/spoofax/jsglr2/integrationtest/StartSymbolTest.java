package org.spoofax.jsglr2.integrationtest;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class StartSymbolTest extends BaseTestWithSdf3ParseTables {

    public StartSymbolTest() {
        super("start-symbol.sdf3");
    }

    @Test
    public void withoutStartSymbol() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions(null, "foo", "amb([\"foo\", Id(\"foo\")])");
    }

    @Test
    public void withStartSymbol() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("Start", "foo", "Id(\"foo\")");
    }

}