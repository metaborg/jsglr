package org.spoofax.jsglr2.tests.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.jsglr2.util.WithParseTableFromTerm;
import org.spoofax.terms.ParseError;

public class GreenMarlTest extends BaseTestWithParseTableFromTermWithJSGLR1 implements WithParseTableFromTerm {

    public GreenMarlTest()
        throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
        setupParseTable("GreenMarl");
    }

    @Test
    public void testSampleProgramByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("GreenMarl/infomap.gm");

        testSuccessByJSGLR1(sampleProgram);
    }

}