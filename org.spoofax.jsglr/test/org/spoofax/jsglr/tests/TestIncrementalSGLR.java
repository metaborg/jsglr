package org.spoofax.jsglr.tests;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.incremental.IncrementalSGLR;
import org.spoofax.jsglr.client.incremental.IncrementalSGLRException;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestIncrementalSGLR extends ParseTestCase {

	private static IStrategoTerm java4Result, java5Result, java7Result, java8Result;
	
    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
    	super.gwtSetUp("Java-15", "java", "MethodDec", "ClassBodyDec"
        		, "ClassMemberDec", "ConstrDec", "FieldDec"
        		);
        assertTrue("Java -ea assertions must be enabled for these tests",
        		IncrementalSGLR.class.desiredAssertionStatus());
    }
    
    private IStrategoTerm getJava4Result() {
    	if (java4Result == null) java4Result = doParseTest("java4");
    	return java4Result;
    }
    
    private IStrategoTerm getJava5Result() {
    	if (java5Result == null) java5Result = doParseTest("java5");
    	return java5Result;
    }
    
    private IStrategoTerm getJava7Result() {
    	if (java7Result == null) java7Result = doParseTest("java7");
    	return java7Result;
    }
    
    private IStrategoTerm getJava8Result() {
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
    	IStrategoTerm result = doParseIncrementalTest(getJava5Result(), "java5-increment5");
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
    	try {
    		doParseIncrementalTest(getJava5Result(), "java5-increment7");
    		fail("Was really expecting a failure here; comment damage expander expands to previous newline?");
    	} catch (IncrementalSGLRException e) {
    		assertTrue("Only allowed to fail because of comment damage expander:" + e.getMessage(),
    				e.getMessage().indexOf("Precondition") != -1);
    	}
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
    	IStrategoTerm java6 = doParseTest("java6");
    	IStrategoTerm java61 = doParseIncrementalTest(java6, "java6-increment");
    	assertFalse(java6.toString().contains("baz"));
    	assertTrue(java61.toString().contains("baz"));
    	assertTrue(getLeftToken(java61).getTokenizer().toString().toString().contains("sense"));
    }
    
    public void testJava7() throws Exception {
    	IStrategoTerm java7 = getJava7Result();
    	doParseIncrementalTest(java7, "java7-increment");
    }
    
    public void testJava72() throws Exception {
    	IStrategoTerm java7 = getJava7Result();
    	doParseIncrementalTest(java7, "java7-increment2");
    	assertFalse(isReparsed("foo"));
    }
    
    public void testJava73() throws Exception {
    	IStrategoTerm java7 = getJava7Result();
    	doParseIncrementalTest(java7, "java7-increment3");
    	int reparsed = incrementalSGLR.getLastReconstructedNodes().size();
    	assertTrue("Expected 1 reparsed node: " + reparsed, reparsed <= 4);
    }
    
    public void testJava8() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertTrue(isReparsed("foo"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava82() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment2");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	// Here, qux is reparsed because comment damage handler and then neighbour damage handler
    	// epand the damage zone
    	// assertFalse(isReparsed("qux"));
    }
    
    public void testJava83() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment3");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava84() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment4");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava85() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment5");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava912Recovery() throws Exception {
    	suffix = "java.recover";
    	sglr.setUseStructureRecovery(true);
    	doCompare = false;
    	IStrategoTerm java9 = doParseTest("java9");
    	IStrategoTerm java91 = doParseIncrementalTest(java9,  "java9-increment");
    	try {
    		doParseIncrementalTest(java91, "java9-increment2");
    	} catch (IncrementalSGLRException e) {
    		System.out.println(e.getMessage());
    		return; // expected
    	}
    	fail("Exception expected");
    }
    
    public void testJava93Recovery() throws Exception {
    	suffix = "java.recover";
    	sglr.setUseStructureRecovery(true);
    	doCompare = false;
    	IStrategoTerm java9 = doParseTest("java9");
    	try {
    		doParseIncrementalTest(java9, "java9-increment3");
    	} catch (IncrementalSGLRException e) {
    		System.out.println(e.getMessage());
    		return; // expected
    	}
    	fail("Exception expected");
    }

    public void testJava4() throws Exception {
    	doParseIncrementalTest(getJava4Result(), "java4-increment");
    	assertTrue(isReparsed("foo"));
    	int reparsed = incrementalSGLR.getLastReconstructedNodes().size();
    	assertTrue("Expected 4 or fewer reparsed nodes: " + reparsed, reparsed <= 4);
    }

    public void testJava4vs5() throws Exception {
    	try {
            IncrementalSGLR.DEBUG = false;
    		doParseIncrementalTest(getJava4Result(), "java5-increment");
    	} catch (IncrementalSGLRException e) {
    		System.out.println(e.getMessage());
    		assertTrue("Must fail on precondition", e.getMessage().indexOf("Precondition") != -1);
    		return;
    	} finally {
            IncrementalSGLR.DEBUG = true;
    	}
    	fail("Exception expected");
    }
    
    private boolean isReparsed(String substring) {
    	return incrementalSGLR.getLastReconstructedNodes().toString().indexOf(substring) != -1;
    }

}
