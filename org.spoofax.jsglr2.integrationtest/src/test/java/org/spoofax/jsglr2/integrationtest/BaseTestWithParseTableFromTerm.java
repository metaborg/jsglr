package org.spoofax.jsglr2.integrationtest;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integration.WithParseTableFromTerm;

public abstract class BaseTestWithParseTableFromTerm extends BaseTest implements WithParseTableFromTerm {

    private IStrategoTerm parseTableTerm;
    
    protected BaseTestWithParseTableFromTerm() {
        super();
    }

    public void setParseTableTerm(IStrategoTerm parseTableTerm) {
        this.parseTableTerm = parseTableTerm;
    }

    public IStrategoTerm getParseTableTerm() {
        return parseTableTerm;
    }

}
