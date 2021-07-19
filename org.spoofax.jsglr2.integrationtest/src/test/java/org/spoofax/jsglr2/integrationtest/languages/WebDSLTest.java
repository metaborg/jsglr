package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTerm;
import org.spoofax.terms.ParseError;

public class WebDSLTest extends BaseTestWithParseTableFromTerm {

    public WebDSLTest() throws Exception {
        setupParseTable("WebDSL");
    }

    @TestFactory public Stream<DynamicTest> testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("WebDSL/built-in.app");

        return testParseSuccess(sampleProgram);
    }

}
