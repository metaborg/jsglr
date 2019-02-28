package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class CommentsTest extends BaseTestWithSdf3ParseTables {

    public CommentsTest() {
        super("comments.sdf3");
    }

    @Test public void oneX() throws ParseError {
        testSuccessByExpansions("x", "Xs([X])");
    }

    @Test public void twoXs() throws ParseError {
        testSuccessByExpansions("x x", "Xs([X, X])");
        testSuccessByExpansions("x x // x", "Xs([X, X])");
        testSuccessByExpansions("x /* x */ x", "Xs([X, X])");
        testSuccessByExpansions("x /* \n */ x", "Xs([X, X])");
    }

}