package org.spoofax.jsglr2.integrationtest;

import java.io.InputStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integration.WithParseTableFromTerm;

public abstract class BaseTestWithParseTableFromTerm extends BaseTest implements WithParseTableFromTerm {

    private IStrategoTerm parseTableTerm;
    
    protected BaseTestWithParseTableFromTerm() {
        super();
    }

    protected void setupParseTable(String parseTable) throws Exception {
		setParseTableFromTermFile("parsetables/" + parseTable + ".tbl");
	}
	
    public void setParseTableTerm(IStrategoTerm parseTableTerm) {
        this.parseTableTerm = parseTableTerm;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }
    
    public InputStream resourceInputStream(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

}
