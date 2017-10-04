package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.fail;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.util.WithJSGLR1;

public abstract class BaseTestWithJSGLR1 extends BaseTest implements WithJSGLR1 {

    protected void testSuccessByJSGLR1(String inputString) {
    		IParseTable parseTable = getParseTable();
		
		for (JSGLR2Variants.Variant variant : JSGLR2Variants.allVariants()) {
			IStrategoTerm actualOutputAst = testSuccess(parseTable, variant, inputString);
			
			try {
	            IStrategoTerm expectedOutputAst = (IStrategoTerm) getJSGLR1().parse(inputString, null, null).output;

	            assertEqualTermExpansions(expectedOutputAst, actualOutputAst);
	        } catch(SGLRException | InterruptedException | InvalidParseTableException e) {
	            e.printStackTrace();

	            fail();
	        }
		}
    }
	
}
