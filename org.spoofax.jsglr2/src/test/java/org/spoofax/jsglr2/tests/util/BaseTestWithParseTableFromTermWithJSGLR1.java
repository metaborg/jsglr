package org.spoofax.jsglr2.tests.util;

import static org.junit.Assert.fail;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.util.WithJSGLR1;

public abstract class BaseTestWithParseTableFromTermWithJSGLR1 extends BaseTestWithParseTableFromTerm implements WithJSGLR1 {

    protected void testSuccessByJSGLR1(String inputString) {
        try {
            IStrategoTerm expectedOutputAst = (IStrategoTerm) getJSGLR1().parse(inputString, null, null).output;
            
            for(JSGLR2Variants.Variant variant : JSGLR2Variants.testVariants()) {
                IParseTable parseTable = getParseTableFailOnException(variant.parseTable);
                IStrategoTerm actualOutputAst = testSuccess(parseTable, variant.parser, null, inputString);

                assertEqualTermExpansions(expectedOutputAst, actualOutputAst);
            }
        } catch(SGLRException | InterruptedException | InvalidParseTableException e) {
            e.printStackTrace();

            fail();
        }
    }

}
