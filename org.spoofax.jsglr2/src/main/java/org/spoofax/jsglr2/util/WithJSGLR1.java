package org.spoofax.jsglr2.util;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.terms.TermFactory;

public interface WithJSGLR1 {

    IStrategoTerm getParseTableTerm();

    default SGLR getJSGLR1() throws InvalidParseTableException {
        TermFactory termFactory = new TermFactory();

        TreeBuilder jsglr1TreeBuilder = new TreeBuilder(new TermTreeFactory(termFactory));
        ParseTable jsglr1ParseTable = new ParseTable(getParseTableTerm(), termFactory);

        return new SGLR(jsglr1TreeBuilder, jsglr1ParseTable);
    }

}
