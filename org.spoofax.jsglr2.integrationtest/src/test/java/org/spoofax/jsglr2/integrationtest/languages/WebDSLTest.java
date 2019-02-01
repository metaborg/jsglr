package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTerm;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class WebDSLTest extends BaseTestWithParseTableFromTerm {

    public WebDSLTest() throws Exception {
        setupParseTable("WebDSL");
    }

    @Test
    public void testSampleProgramByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("WebDSL/built-in.app");

        testParseSuccess(sampleProgram);
    }

}