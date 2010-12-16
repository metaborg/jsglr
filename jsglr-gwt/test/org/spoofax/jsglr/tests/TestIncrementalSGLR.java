package org.spoofax.jsglr.tests;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.shared.terms.ATerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestIncrementalSGLR extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Java-15", "java", "MethodDec", "ClassBodyDec");
    }

    public void testJava5() throws Exception {
    	ATerm tree1 = doParseTest("java5");
    	doParseIncrementalTest(tree1, "java5-increment");
    	// doParseIncrementalTest(tree1, "java5-increment2");
    	// doParseIncrementalTest(tree1, "java5-increment3");
    }

    public void testJava4() throws Exception {
    	ATerm tree1 = doParseTest("java4");
    	doParseIncrementalTest(tree1, "java4-increment");
    	// TODO: test doParseIncrementalTest(tree1, "java5-increment");
    }

}
