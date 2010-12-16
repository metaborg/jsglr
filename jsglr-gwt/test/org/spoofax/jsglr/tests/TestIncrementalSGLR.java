package org.spoofax.jsglr.tests;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.incremental.IncrementalSGLR;
import org.spoofax.jsglr.client.incremental.IncrementalSGLRException;
import org.spoofax.jsglr.shared.terms.ATerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestIncrementalSGLR extends ParseTestCase {

	private static ATerm java4Result, java5Result, java8Result;
	
    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Java-15", "java", "MethodDec", "ClassBodyDec"
        		, "ClassMemberDec", "ConstrDec", "FieldDec"
        		);
        assertTrue("Java -ea assertions must be enabled for these tests",
        		IncrementalSGLR.class.desiredAssertionStatus());
    }
    
    private ATerm getJava4Result() {
    	if (java4Result == null) java4Result = doParseTest("java4");
    	return java4Result;
    }
    
    private ATerm getJava5Result() {
    	if (java5Result == null) java5Result = doParseTest("java5");
    	return java5Result;
    }
    
    private ATerm getJava8Result() {
    	if (java8Result == null) java8Result = doParseTest("java8");
    	return java8Result;
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
    	ATerm result = doParseIncrementalTest(getJava5Result(), "java5-increment5");
    	assertFalse("There is no foo", result.toString().contains("\"foo\""));
    	assertFalse("There is no baz", result.toString().contains("\"bar\""));
    	assertTrue("There is only foobaz", result.toString().contains("\"foobaz\""));
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
    
    public void testJava58() throws Exception {
    	try {
    		doParseIncrementalTest(getJava5Result(), "java5-increment8");
    	} catch (IncrementalSGLRException e) {
    		return;
    	}
    	fail("Exception expected");
    }
    
    public void testJava6Recovery() throws Exception {
    	suffix = "java.recover";
    	sglr.setUseStructureRecovery(true);
    	doCompare = false;
    	ATerm java6 = doParseTest("java6");
    	ATerm java61 = doParseIncrementalTest(java6, "java6-increment");
    	assertFalse(java6.toString().contains("baz"));
    	assertTrue(java61.toString().contains("baz"));
    	assertTrue(java61.getLeftToken().getTokenizer().toString().toString().contains("sense"));
    }
    
    public void testJava7() throws Exception {
    	ATerm java7 = doParseTest("java7");
    	ATerm java7Increment = doParseIncrementalTest(java7, "java7-increment");
    	assertTrue("Method bar should be outside of a comment", java7.toString().contains("\"bar\""));
    	assertFalse("Method bar should be in a comment", java7Increment.toString().contains("\"bar\""));
    }
    
    public void testJava8() throws Exception {
    	ATerm java8 = getJava8Result();
    	ATerm java8Increment = doParseIncrementalTest(java8, "java8-increment");
    	assertTrue("Comment should be in input tokens", java8.getLeftToken().getTokenizer().toString().contains("comment"));
    	assertFalse("Comment should be in output tokens", java8Increment.getLeftToken().getTokenizer().toString().contains("comment"));
    }
    
    public void testJava82() throws Exception {
    	ATerm java8 = getJava8Result();
    	ATerm java8Increment = doParseIncrementalTest(java8, "java8-increment2");
    	assertTrue("Comment should be in input tokens", java8.getLeftToken().getTokenizer().toString().contains("comment"));
    	assertFalse("Comment should be in output tokens", java8Increment.getLeftToken().getTokenizer().toString().contains("comment"));
    }
    
    public void testJava83() throws Exception {
    	ATerm java8 = getJava8Result();
    	ATerm java8Increment = doParseIncrementalTest(java8, "java8-increment3");
    	assertTrue("Comment should be in input tokens", java8.getLeftToken().getTokenizer().toString().contains("comment"));
    	assertFalse("Comment should be in output tokens", java8Increment.getLeftToken().getTokenizer().toString().contains("comment"));
    }
    
    public void testJava84() throws Exception {
    	ATerm java8 = getJava8Result();
    	ATerm java8Increment = doParseIncrementalTest(java8, "java8-increment4");
    	assertTrue("Comment should be in input tokens", java8.getLeftToken().getTokenizer().toString().contains("comment"));
    	assertFalse("Comment should be in output tokens", java8Increment.getLeftToken().getTokenizer().toString().contains("comment"));
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
