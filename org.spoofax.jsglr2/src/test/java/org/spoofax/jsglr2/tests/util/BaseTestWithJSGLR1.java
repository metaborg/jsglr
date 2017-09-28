package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.fail;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr2.util.WithJSGLR1;

public abstract class BaseTestWithJSGLR1 extends BaseTest implements WithJSGLR1 {

    protected void testParseSuccessByJSGLR(String inputString) {
        IStrategoTerm srActualOutputAst = testSRParseSuccess(inputString);
        IStrategoTerm hActualOutputAst = testHParseSuccess(inputString);
        IStrategoTerm srElkhoundActualOutputAst = testSRElkhoundParseSuccess(inputString);
        
        try {
            IStrategoTerm expectedOutputAst = (IStrategoTerm) getJSGLR1().parse(inputString, null, null).output;

            assertEqualTermExpansions(expectedOutputAst, srActualOutputAst);
            assertEqualTermExpansions(expectedOutputAst, hActualOutputAst);
            assertEqualTermExpansions(expectedOutputAst, srElkhoundActualOutputAst);
        } catch(SGLRException | InterruptedException | InvalidParseTableException e) {
            e.printStackTrace();

            fail();
        }
    }
	
}
