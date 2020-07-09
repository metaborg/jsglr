package org.spoofax.jsglr2.testset;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableVariant;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integration.Sdf3ToParseTable;
import org.spoofax.jsglr2.integration.WithParseTableFromTerm;
import org.spoofax.jsglr2.testset.testinput.TestInput;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.io.binary.TermReader;

public abstract class TestSetWithParseTableReader<ContentType, Input extends TestInput<ContentType>>
    extends TestSetReader<ContentType, Input> implements WithParseTableFromTerm {

    protected final TestSetWithParseTable<ContentType, Input> testSet;

    private TermReader termReader;

    private IStrategoTerm parseTableTerm;

    protected TestSetWithParseTableReader(TestSetWithParseTable<ContentType, Input> testSet) {
        super(testSet);

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

                if(testSetParseTableFromATerm.internal)
                    setParseTableFromTermResource("/parsetables/" + testSetParseTableFromATerm.file + ".tbl");
                else
                    setParseTableFromTermFile(testSetParseTableFromATerm.file);

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

    public TermReader getTermReader() {
        return termReader;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }

    public void setParseTableTerm(IStrategoTerm parseTableTerm) {
        this.parseTableTerm = parseTableTerm;
    }

}
