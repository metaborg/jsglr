package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class GreenMarlTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public GreenMarlTest() throws Exception {
        setupParseTable("GreenMarl");
    }

    @TestFactory public Stream<DynamicTest> testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("GreenMarl/infomap.gm");

        return testSuccessByJSGLR1(sampleProgram);
    }

}