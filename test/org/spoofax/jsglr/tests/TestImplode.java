package org.spoofax.jsglr.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.imploder.TreeBuilder;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestImplode extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Java-15", "java");
        sglr.setTreeBuilder(new TreeBuilder());
    }
    

    public void testJava0() throws FileNotFoundException, IOException {
    	doParseTest("java0");
    }
    
    public void testJava2() throws FileNotFoundException, IOException {
    	doParseTest("java2");
    }

    public void testJava4() throws FileNotFoundException, IOException {
    	doParseTest("java4");
    }

    public void testJava10() throws FileNotFoundException, IOException {
    	doParseTest("java10");
    }
    
}
