package org.spoofax.jsglr2.integrationtest.features;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.terms.ParseError;

public class AmbiguousMessagesTest extends BaseTestWithSdf3ParseTables {

    public AmbiguousMessagesTest() {
        super("ambiguous.sdf3");
    }

    @Override protected JSGLR2Request configureRequest(JSGLR2Request request) {
        return request.withAmbiguitiesReporting(true);
    }

    @TestFactory public Stream<DynamicTest> contextFree() throws ParseError {
        return testMessages("foo",
            Collections.singletonList(new MessageDescriptor("Ambiguity", Severity.WARNING, 0, 1, 1, 3)), "ContextFree");
    }

    Stream<TestVariant> nonOptimizedParseForestVariants =
        getTestVariants(variant -> variant.variant.parser.parseForestConstruction == ParseForestConstruction.Full
            && variant.variant.parser.parseForestRepresentation == ParseForestRepresentation.Basic);

    @TestFactory public Stream<DynamicTest> lexical() throws ParseError {
        return testMessages("foo",
            Collections.singletonList(new MessageDescriptor("Lexical ambiguity", Severity.WARNING, 0, 1, 1, 3)),
            nonOptimizedParseForestVariants, "Lexical");
    }

    @TestFactory public Stream<DynamicTest> layout() throws ParseError {
        return testMessages("///bar",
            Collections.singletonList(new MessageDescriptor("Layout ambiguity", Severity.WARNING, 0, 1, 1, 4)),
            nonOptimizedParseForestVariants, "Layout");
    }

    @TestFactory public Stream<DynamicTest> lexicalInLayout() throws ParseError {
        return testMessages("/*___*/bar",
            Collections.singletonList(new MessageDescriptor("Lexical ambiguity", Severity.WARNING, 2, 1, 3, 4)),
            nonOptimizedParseForestVariants, "Layout");
    }

}
