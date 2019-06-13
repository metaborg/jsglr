package org.spoofax.jsglr2.testset;

import java.io.IOException;
import java.io.InputStream;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.integration.Sdf3ToParseTable;
import org.spoofax.jsglr2.integration.WithParseTableFromTerm;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class TestSetReader<Input> implements WithParseTableFromTerm {

    protected final TestSet<Input> testSet;

    protected TermReader termReader;

    protected IStrategoTerm parseTableTerm;

    protected TestSetReader(TestSet<Input> testSet) {
        this.testSet = testSet;

        this.termReader = new TermReader(new TermFactory());

        try {
            setupTestSetParseTable();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public IParseTable getParseTableFromTerm(ParseTableVariant variant) throws Exception {
        return variant.parseTableReader().read(getParseTableTerm());
    }

    private void setupTestSetParseTable() throws Exception {
        switch(testSet.parseTable.source) {
            case ATERM:
                TestSetParseTableFromATerm testSetParseTableFromATerm = (TestSetParseTableFromATerm) testSet.parseTable;

                setParseTableFromTermFile("/parsetables/" + testSetParseTableFromATerm.name + ".tbl");

                break;
            case SDF3:
                TestSetParseTableFromSDF3 testSetParseTableFromSDF3 = (TestSetParseTableFromSDF3) testSet.parseTable;

                Sdf3ToParseTable sdf3ToParseTable = new Sdf3ToParseTable(resource -> basePath() + resource);

                this.parseTableTerm = sdf3ToParseTable.getParseTableTerm(testSetParseTableFromSDF3.name + ".sdf3");

                break;
            default:
                throw new IllegalStateException("invalid parse table source");
        }
    }

    protected abstract String basePath();

    public abstract InputStream resourceInputStream(String filename) throws Exception;

    public TermReader getTermReader() {
        return termReader;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }

    public void setParseTableTerm(IStrategoTerm parseTableTerm) {
        this.parseTableTerm = parseTableTerm;
    }

    public Iterable<Input> getInputs() throws IOException {
        return testSet.input.getInputs();
    }

    public Iterable<Input> getInputsForSize(int n) {
        if(testSet.input.type == TestSetInput.Type.SIZED) {
            return ((TestSetSizedInput<Input>) testSet.input).getInputs(n);
        }
        throw new IllegalStateException("invalid input type (test set input should have a size)");
    }

}
