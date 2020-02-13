package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class NaBL2Test extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public NaBL2Test() throws Exception {
        setupParseTable("NaBL2v7"); // Parse Table version 7
    }

    @TestFactory public Stream<DynamicTest> testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("NaBL2/map.nabl2");

        return testSuccessByJSGLR1(sampleProgram);
    }

}
