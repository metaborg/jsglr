package org.spoofax.jsglr2.imploder;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;

public class StrategoTermTokenizer extends TreeTokenizer<IStrategoTerm> {
    @Override protected void configure(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken) {
        // rightToken can be null, e.g. for an empty string lexical
        putImploderAttachment(term, false, sort, leftToken, rightToken != null ? rightToken : leftToken, false, false,
            false, false);
    }

    @Override protected void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }
}
