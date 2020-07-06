package org.spoofax.jsglr.client.imploder;

import java.io.Serializable;
import java.util.Iterator;

public interface ITokens extends Iterable<IToken>, Serializable {

    String getInput();

    int getTokenCount();

    IToken getTokenAtOffset(int offset);

    String getFilename();

    String toString(IToken left, IToken right);

    String toString(int startOffset, int endOffset);

    /**
     * @return An iterator that ensures that the returned tokens are in-order. For every ambiguous subtree, only one
     *         derivation is chosen to get the tokens from. Empty tokens are skipped so that all tokens in this iterator
     *         have a width of at least one character.
     */
    @Override Iterator<IToken> iterator();

    /**
     * @return An iterator that includes all tokens, including all tokens from all ambiguous subtrees and empty tokens.
     *         No order is guaranteed on the offsets of the returned tokens.
     */
    Iterable<IToken> allTokens();

}
