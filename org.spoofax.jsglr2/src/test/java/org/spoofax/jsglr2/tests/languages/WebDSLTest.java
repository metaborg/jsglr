package org.spoofax.jsglr2.tests.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithParseTableFromTerm;
import org.spoofax.jsglr2.util.WithParseTableFromTerm;
import org.spoofax.terms.ParseError;

public class WebDSLTest extends BaseTestWithParseTableFromTerm implements WithParseTableFromTerm {

    public WebDSLTest()
        throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
        setupParseTable("WebDSL");
    }

    @Test
    public void testSampleProgramByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("WebDSL/built-in.app");

        testParseSuccess(sampleProgram);
    }

}