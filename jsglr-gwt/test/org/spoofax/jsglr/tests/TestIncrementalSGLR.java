package org.spoofax.jsglr.tests;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.shared.terms.ATerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestIncrementalSGLR extends ParseTestCase {

	ATerm java5;
	
    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Java-15", "java", "MethodDec", "ClassBodyDec");
    	java5 = doParseTest("java5");
    }

    public void testJava51() throws Exception {
    	doParseIncrementalTest(java5, "java5-increment");
    }
    
    public void testJava52() throws Exception {
    	doParseIncrementalTest(java5, "java5-increment2");
    }
    
    public void testJava53() throws Exception {
    	doParseIncrementalTest(java5, "java5-increment3");
    }
    
    public void testJava54() throws Exception {
    	doParseIncrementalTest(java5, "java5-increment4");
    }

    public void testJava4() throws Exception {
    	ATerm tree1 = doParseTest("java4");
    	doParseIncrementalTest(tree1, "java4-increment");
    	// TODO: test doParseIncrementalTest(tree1, "java5-increment");
    }

}
