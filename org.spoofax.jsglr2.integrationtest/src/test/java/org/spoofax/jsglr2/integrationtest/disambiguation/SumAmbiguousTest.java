package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class SumAmbiguousTest extends BaseTestWithSdf3ParseTables {

    public SumAmbiguousTest() {
        super("sum-ambiguous.sdf3");
    }

    @TestFactory public Stream<DynamicTest> one() throws ParseError {
        return testSuccessByExpansions("x", "Term()");
    }

    @TestFactory public Stream<DynamicTest> two() throws ParseError {
        return testSuccessByExpansions("x+x", "Add(Term(),Term())");
    }

    @TestFactory public Stream<DynamicTest> three() throws ParseError {
        return testSuccessByExpansions("x+x+x", "amb([Add(Add(Term,Term),Term), Add(Term,Add(Term,Term))])");
    }

}