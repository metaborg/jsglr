package org.spoofax.jsglr2.tests.grammars;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.jsglr2.util.WithLegacySdfGrammar;
import org.spoofax.terms.ParseError;

public class SumNonAmbiguousTest extends BaseTestWithParseTableFromTermWithJSGLR1 implements WithLegacySdfGrammar {

    public SumNonAmbiguousTest() throws ParseError, InterruptedException, IOException, ParseTableReadException,
        InvalidParseTableException, URISyntaxException {
        setupParseTableFromDefFile("sum-nonambiguous");
    }

    @Test
    public void one() throws ParseError, ParseTableReadException, IOException {
        testSuccessByJSGLR1("x");
    }

    @Test
    public void two() throws ParseError, ParseTableReadException, IOException {
        testSuccessByJSGLR1("x+x");
    }

    @Test
    public void three() throws ParseError, ParseTableReadException, IOException {
        testSuccessByJSGLR1("x+x+x");
    }

}