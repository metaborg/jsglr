package org.spoofax.jsglr2.integrationtest.disambiguation;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class SumNonAmbiguousTest extends BaseTestWithSdf3ParseTables {

    public SumNonAmbiguousTest() {
        super("sum-nonambiguous.sdf3");
    }

    @TestFactory public Stream<DynamicTest> one() throws ParseError {
        return testSuccessByExpansions("x", "Term()");
    }

    @TestFactory public Stream<DynamicTest> two() throws ParseError {
        return testSuccessByExpansions("x+x", "Add(Term(),Term())");
    }

    @TestFactory public Stream<DynamicTest> three() throws ParseError {
        return testSuccessByExpansions("x+x+x", "Add(Add(Term(),Term()),Term())");
    }

}