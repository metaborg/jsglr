package org.spoofax.jsglr2.imploder;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;

import org.metaborg.parsetable.IProduction;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;

public class IterativeStrategoTermTokenizer extends IterativeTreeTokenizer<IStrategoTerm> {
    @Override protected void storeTokensInTree(IStrategoTerm tree, IProduction production, IToken leftToken,
        IToken rightToken) {
        if(tree != null && leftToken != null && rightToken != null) {
            String sort = production == null ? null : production.sort();
            putImploderAttachment(tree, false, sort, leftToken, rightToken, false, false, false, false);
        }
    }

    @Override protected void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }
}
