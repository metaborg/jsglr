package org.spoofax.jsglr2.integrationtest.incremental;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTerm;
import org.spoofax.terms.ParseError;

public class IncrementalJava8Test extends BaseTestWithParseTableFromTerm {

    public IncrementalJava8Test() throws Exception {
        setupParseTable("Java8");
    }

    @TestFactory public Stream<DynamicTest> testOriginal() throws ParseError, IOException {
        return testIncrementalSuccessByBatch( //
            getFileAsString("Java/SubstituteLoggingEvent.java/6.in"),
            getFileAsString("Java/SubstituteLoggingEvent.java/7.in"),
            getFileAsString("Java/SubstituteLoggingEvent.java/8.in"));
    }

    @TestFactory public Stream<DynamicTest> testSmall() throws ParseError {
        return testIncrementalSuccessByBatch( //
            "class M { void addM() { getM; } }", //
            "class M { void addM() { return; m; } }");
    }

    public static void main(String[] args) throws Exception {
        IncrementalJava8Test test = new IncrementalJava8Test();
        IncrementalSGLRThesisExampleTest.logIncrementalParse(test, //
            // "class M { void addM() { getM; } }", //
            // "class M { void addM() { return; m; } }"
            test.getFileAsString("Java/SubstituteLoggingEvent.java/7.in"),
            test.getFileAsString("Java/SubstituteLoggingEvent.java/8.in"));
    }

}