package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoWebDSLJavaTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoWebDSLJavaTest() throws Exception {
        setupParseTable("Stratego-WebDSL-Java");
    }

    @TestFactory public Stream<DynamicTest> test() throws ParseError, IOException {
        String sampleProgram = getFileAsString("StrategoWebDSLJava/webdsl-src-org-webdsl-dsl-to-java-servlet-uicomponents-attributes.str");

        return testSuccessByJSGLR1(sampleProgram);
    }


}