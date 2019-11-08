package org.spoofax.jsglr2.integrationtest.languages;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

import java.io.IOException;

public class GreenMarlTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public GreenMarlTest() throws Exception {
        setupParseTable("GreenMarl");
    }

    @Test public void testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("GreenMarl/infomap.gm");

        testSuccessByJSGLR1(sampleProgram);
    }

}