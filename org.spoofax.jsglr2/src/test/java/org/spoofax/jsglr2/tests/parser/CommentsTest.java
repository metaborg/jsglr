package org.spoofax.jsglr2.tests.parser;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class CommentsTest extends BaseTest implements WithGrammar {
	
	public CommentsTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
	    setupParseTableFromDefFile("comments");
	}
    
    @Test
    public void oneX() throws ParseError, ParseTableReadException, IOException {
        testParseSuccessByExpansions("x", "Xs([X])");
    }
    
    @Test
    public void twoXs() throws ParseError, ParseTableReadException, IOException {
        testParseSuccessByExpansions("x x", "Xs([X, X])");
        testParseSuccessByExpansions("x x // x", "Xs([X, X])");
        testParseSuccessByExpansions("x /* x */ x", "Xs([X, X])");
        testParseSuccessByExpansions("x /* \n */ x", "Xs([X, X])");
    }
  
}