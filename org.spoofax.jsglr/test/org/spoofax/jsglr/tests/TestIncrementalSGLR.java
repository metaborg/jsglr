package org.spoofax.jsglr.tests;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.incremental.IncrementalSGLR;
import org.spoofax.jsglr.client.incremental.IncrementalSGLRException;
import org.spoofax.jsglr.client.incremental.IncrementalSortSet;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestIncrementalSGLR extends ParseTestCase {
	
	private static String[] CLASSBODY_SORTS = { "ClassBodyDec" };
	
	private static String[] MOST_SORTS = {"MethodDec", "ClassBodyDec",
		"ClassMemberDec", "ConstrDec", "FieldDec", "ImportDec", "PackageDec",
		/*unsafe:*/ "ImportDec*" };

	private static IStrategoTerm java4Result, java5Result, java7Result, java8Result;
	
    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
    	super.gwtSetUp("Java-15", "java", CLASSBODY_SORTS);
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
    	doParseIncrementalTest(getJava5Result(), "java5-increment", "MethodDec", "ClassMemberDec");
    }
    
    public void testJava52() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment2", "MethodDec", "ClassMemberDec");
    }
    
    public void testJava53() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment3", "MethodDec", "ClassMemberDec");
    }
    
    public void testJava54() throws Exception {
    	doParseIncrementalTest(getJava5Result(), "java5-increment4", "MethodDec", "ClassMemberDec");
    }
    
    public void testJava55() throws Exception {
    	IStrategoTerm result = doParseIncrementalTest(getJava5Result(), "java5-increment5", "MethodDec", "ClassMemberDec");
    	assertFalse("There is no foo", result.toString().contains("\"foo\""));
    	assertFalse("There is no baz", result.toString().contains("\"bar\""));
    	assertTrue("There is only foobaz", result.toString().contains("\"foobaz\""));
    }
    
    public void testJava56() throws Exception {
		doParseIncrementalTest(getJava5Result(), "java5-increment6", "ClassDec");
    }
    
    public void testJava57() throws Exception {
		doParseIncrementalTest(getJava5Result(), "java5-increment7", "ClassDec");
    }
    
    public void testJava58() throws Exception {
		doParseIncrementalTest(getJava5Result(), "java5-increment8", "ClassDec");
    }
    
    public void testJava59() throws Exception {
		doParseIncrementalTest(getJava5Result(), "java5-increment9", "ClassDec");
    }
    
    public void testJava510() throws Exception {
		doParseIncrementalTest(getJava5Result(), "java5-increment10", "ClassDec");
    }
    
    public void testJava511() throws Exception {
		doParseIncrementalTest(getJava5Result(), "java5-increment11", "MethodDec", "ClassMemberDec");
    }
    
    public void testJava6Recovery() throws Exception {
    	suffix = "java.recover";
    	sglr.setUseStructureRecovery(true);
    	doCompare = false; // can't compare unparseable data
    	IStrategoTerm java6 = doParseTest("java6");
    	IStrategoTerm java61 = doParseIncrementalTest(java6, "java6-increment");
    	assertFalse(java6.toString().contains("baz"));
    	assertTrue(java61.toString().contains("baz"));
    	assertTrue(getLeftToken(java61).getTokenizer().toString().toString().contains("sense"));
    }
    
    public void testJava7() throws Exception {
    	IStrategoTerm java7 = getJava7Result();
    	doParseIncrementalTest(java7, "java7-increment", "MethodDec", "ClassMemberDec");
    }
    
    public void testJava72() throws Exception {
    	IStrategoTerm java7 = getJava7Result();
    	doParseIncrementalTest(java7, "java7-increment2", "MethodDec", "ClassMemberDec");
    	assertFalse(isReparsed("foo"));
    }
    
    public void testJava73() throws Exception {
    	IStrategoTerm java7 = getJava7Result();
    	doParseIncrementalTest(java7, "java7-increment3", "MethodDec", "ClassMemberDec");
    	int reparsed = incrementalSGLR.getLastReconstructedNodes().size();
    	assertTrue("Expected 1 reparsed node: " + reparsed, reparsed <= 4);
    }
    
    public void testJava8() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment", "MethodDec", "ClassMemberDec");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertTrue(isReparsed("foo"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava82() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment2", "MethodDec", "ClassMemberDec");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	// Here, qux is reparsed because comment damage handler and then neighbour damage handler
    	// epand the damage zone
    	// assertFalse(isReparsed("qux"));
    }
    
    public void testJava83() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment3", "MethodDec", "ClassMemberDec");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava84() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment4", "MethodDec", "ClassMemberDec");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava85() throws Exception {
    	IStrategoTerm java8 = getJava8Result();
    	IStrategoTerm java8Increment = doParseIncrementalTest(java8, "java8-increment5", "MethodDec", "ClassMemberDec");
    	assertTrue("Comment should be in input tokens", getLeftToken(java8).getTokenizer().toString().contains("comment"));
    	assertTrue("Comment should be in output tokens", getLeftToken(java8Increment).getTokenizer().toString().contains("comment"));
    	assertFalse(isReparsed("qux"));
    }
    
    public void testJava912Recovery() throws Exception {
    	suffix = "java.recover";
    	sglr.setUseStructureRecovery(true);
    	doCompare = false; // can't compare unparseable data
    	IStrategoTerm java9 = doParseTest("java9");
    	IStrategoTerm java91 = doParseIncrementalTest(java9,  "java9-increment");
		doParseIncrementalTest(java91, "java9-increment2");
    }
    
    public void testJava912RecoveryFail() throws Exception {
    	incrementalSGLR.setIncrementalSorts(IncrementalSortSet.create(table, false, CLASSBODY_SORTS));
    	try {
    		testJava912Recovery();
        	fail("Exception expected");
    	} catch (IncrementalSGLRException e) {
    		System.out.println(e.getMessage()); // expected
    	}
    }
    
    public void testJava93Recovery() throws Exception {
    	suffix = "java.recover";
    	sglr.setUseStructureRecovery(true);
    	doCompare = false; // can't compare unparseable data
    	IStrategoTerm java9 = doParseTest("java9");
    	doParseIncrementalTest(java9, "java9-increment3");
    }
    
    public void testJava93RecoveryFail() throws Exception {
    	incrementalSGLR.setIncrementalSorts(IncrementalSortSet.create(table, false, CLASSBODY_SORTS));
    	try {
    		testJava93Recovery();
        	fail("Exception expected");
    	} catch (IncrementalSGLRException e) {
    		System.out.println(e.getMessage()); // expected
    	}
    }

    public void testJava4() throws Exception {
        IncrementalSGLR.DEBUG = false; // too much output
    	doParseIncrementalTest(getJava4Result(), "java4-increment", "MethodDec", "ClassMemberDec");
    	assertTrue(isReparsed("foo"));
    	int reparsed = incrementalSGLR.getLastReconstructedNodes().size();
    	assertTrue("Expected 4 or fewer reparsed nodes: " + reparsed, reparsed <= 4);
    }

    public void testJava4vs5Fail1() throws Exception {
    	try {
	        IncrementalSGLR.DEBUG = false; // too much output
	    	incrementalSGLR.setIncrementalSorts(IncrementalSortSet.create(table, true, MOST_SORTS));
			doParseIncrementalTest(getJava4Result(), "java5-increment");
	    	fail("Exception expected");
    	} catch (IncrementalSGLRException e) {
    		System.out.println(e.getMessage());
    		assertTrue("Must fail on precondition", e.getMessage().indexOf("Precondition") != -1);
    	}
    }

    public void testJava4vs5Fail2() throws Exception {
		try {
	        IncrementalSGLR.DEBUG = false; // too much output
	    	incrementalSGLR.setIncrementalSorts(IncrementalSortSet.create(table, false, CLASSBODY_SORTS));
			doParseIncrementalTest(getJava4Result(), "java5-increment");
        	fail("Exception expected");
    	} catch (IncrementalSGLRException e) {
    		System.out.println(e.getMessage());
    		assertTrue("Must fail on precondition", e.getMessage().indexOf("Precondition") != -1);
    	}
    }
    
    private boolean isReparsed(String substring) {
    	return incrementalSGLR.getLastReconstructedNodes().toString().indexOf(substring) != -1;
    }

}
