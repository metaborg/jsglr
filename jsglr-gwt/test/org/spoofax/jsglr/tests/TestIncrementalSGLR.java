package org.spoofax.jsglr.tests;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.jsglr.client.IncrementalSGLR;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.terms.ATerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestIncrementalSGLR extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Java-15", "java");
        sglr.setTreeBuilder(new TreeBuilder());
    }

    public void testJava5() throws Exception {
    	ATerm tree1 = doParseTest("java5");
    	TreeBuilder builder = (TreeBuilder) sglr.getTreeBuilder();
    	Set<String> sorts = new HashSet<String>();
    	sorts.add("MethodDec");
    	sorts.add("ClassBodyDec");
    	IncrementalSGLR<ATerm> parser = new IncrementalSGLR<ATerm>(sglr, builder, builder.getFactory(), sorts);
    	ATerm tree2 = (ATerm) parser.parseIncremental(loadAsString("java5-increment"), null, null, tree1);
    	ATerm tree3 = (ATerm) sglr.parse(loadAsString("java5-increment"));
    	System.out.println(tree2);
    	System.out.println(tree3);
    	assertEquals(tree3.toString(), tree2.toString());
    }

    public void testJava4() throws Exception {
    	ATerm tree1 = doParseTest("java4");
    	TreeBuilder builder = (TreeBuilder) sglr.getTreeBuilder();
    	Set<String> sorts = new HashSet<String>();
    	sorts.add("MethodDec");
    	sorts.add("ClassBodyDec");
    	IncrementalSGLR<ATerm> parser = new IncrementalSGLR<ATerm>(sglr, builder, builder.getFactory(), sorts);
    	ATerm tree2 = (ATerm) parser.parseIncremental(loadAsString("java4-increment"), null, null, tree1);
    	ATerm tree3 = (ATerm) sglr.parse(loadAsString("java4-increment"));
    	System.out.println(tree2);
    	System.out.println(tree3);
    	assertEquals(tree3.toString(), tree2.toString());
    }

}
