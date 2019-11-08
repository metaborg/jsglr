package org.spoofax.jsglr2.integrationtest;

import org.junit.jupiter.api.DynamicTest;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr2.integration.WithJSGLR1;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class BaseTestWithParseTableFromTermWithJSGLR1 extends BaseTestWithParseTableFromTerm
    implements WithJSGLR1 {

    public Stream<DynamicTest> testSuccessByJSGLR1(String inputString) {
        try {
            IStrategoTerm expectedOutputAst = (IStrategoTerm) getJSGLR1().parse(inputString, null, null).output;

            return testPerVariant(getTestVariants(), variant -> () -> {
                IStrategoTerm actualOutputAst = testSuccess(variant, null, inputString);

                assertEqualTermExpansions(expectedOutputAst, actualOutputAst);
            });
        } catch(SGLRException | InterruptedException | InvalidParseTableException e) {
            e.printStackTrace();

            fail();

            return Stream.empty();
        }
    }

}
