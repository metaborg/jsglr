package org.spoofax.jsglr2.integrationtest;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.metaborg.parsetable.ParseTableVariant;
import org.metaborg.sdf2table.io.ParseTableIO;
import org.metaborg.sdf2table.parsetable.ParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr2.integration.WithJSGLR1;

public abstract class BaseTestWithSdf3ParseTablesWithJSGLR1 extends BaseTestWithSdf3ParseTables implements WithJSGLR1 {

    protected BaseTestWithSdf3ParseTablesWithJSGLR1(String sdf3Resource) {
        super(sdf3Resource);
    }

    /**
     * Note: this method is only used in {@link WithJSGLR1#getJSGLR1()}, not for the JSGLR2 parser(s).
     *
     * @return The {@link org.metaborg.sdf2table.parsetable.ParseTable} converted to an {@link IStrategoTerm}.
     */
    @Override public IStrategoTerm getParseTableTerm() {
        try {
            return ParseTableIO.generateATerm(((ParseTable) getParseTable(new ParseTableVariant()).parseTable));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

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
