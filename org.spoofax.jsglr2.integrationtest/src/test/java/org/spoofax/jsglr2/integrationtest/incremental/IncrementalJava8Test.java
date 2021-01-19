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
            "class Markers { void addMarker(int marker) { getMarkers; } }",
            "class Markers { void addMarker(int marker) { return; markers; } }");
    }

}
