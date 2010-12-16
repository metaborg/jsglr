package org.spoofax.jsglr.tests;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.incremental.IncrementalSGLRException;
import org.spoofax.jsglr.shared.terms.ATerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestIncrementalSGLR extends ParseTestCase {

	private static ATerm java4Result, java5Result;
	
    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Java-15", "java", "MethodDec", "ClassBodyDec"
        		// TODO:, "ClassMemberDec"
        		);
    }
    
    private ATerm getJava4Result() {
    	if (java4Result == null) java4Result = doParseTest("java4");
    	return java4Result;
    }
    
    private ATerm getJava5Result() {
    	if (java5Result == null) java5Result = doParseTest("java5");
    	return java5Result;
    }

    public void testJava51() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment");
    }
    
    public void testJava52() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment2");
    }
    
    public void testJava53() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment3");
    }
    
    public void testJava54() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment4");
    }
    
    public void testJava55() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment5");
    }
    
    public void testJava56() throws Exception {
    	try {
    		doParseIncrementalTest(getJava5Result(), "java5-increment6");
    	} catch (IncrementalSGLRException e) {
    		return;
    	}
    	fail("Exception expected");
    }
    
    public void testJava57() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment7");
    }

    public void testJava4() throws Exception {
    	doParseIncrementalTest(getJava4Result(), "java4-increment");
    	// TODO: test doParseIncrementalTest(java4, "java5-increment");
    }

    public void testJava4vs5() throws Exception {
    	try {
    		doParseIncrementalTest(getJava4Result(), "java5-increment");
    	} catch (IncrementalSGLRException e) {
    		return;
    	}
    	fail("Exception expected");
    }

}
