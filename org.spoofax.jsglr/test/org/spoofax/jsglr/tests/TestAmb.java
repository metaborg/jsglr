/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.terms.io.binary.TermReader;

public class TestAmb extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("SmallAmbLang", "txt");
    }


    public void testAmb_1() throws InterruptedException {
    	sglr.setTreeBuilder(new TreeBuilder());
    	doCompare=false; //c-sglr does not show ambiguity, sglri does???
    	IStrategoTerm parsed=doParseTest("amb1");
    	IStrategoTerm wanted=new TermReader(pf).parseFromString("Module(amb([BType,AType(\"xyz\")]))");
    	if(!parsed.match(wanted)) {
			fail();
		}
    }
}
