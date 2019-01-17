package org.spoofax.jsglr2.integrationtest;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class CommentsTest extends BaseTestWithSdf3ParseTables {

    public CommentsTest() {
        super("comments.sdf3");
    }

    @Test
    public void oneX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x", "Xs([X])");
    }

    @Test
    public void twoXs() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x x", "Xs([X, X])");
        testSuccessByExpansions("x x // x", "Xs([X, X])");
        testSuccessByExpansions("x /* x */ x", "Xs([X, X])");
        testSuccessByExpansions("x /* \n */ x", "Xs([X, X])");
    }

}