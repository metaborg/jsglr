package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IntersectionTest extends BaseTestWithSdf3ParseTables {

    public IntersectionTest() {
        super("intersection.sdf3");
    }

    // TODO: implement stack priorities during reducing to fix intersection problem (see P9707 Section 8.4)

    @Disabled @Test public void testOneNotInIntersection() throws ParseError {
        testParseFailure("1");
    }

    @Disabled @Test public void testTwoInIntersection() throws ParseError {
        testSuccessByAstString("2", "Two");
    }

    @Disabled @Test public void testThreeNotInIntersecton() throws ParseError {
        testParseFailure("3");
    }

}