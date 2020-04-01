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
     * @return An iterator that skips ambiguous tokens.
     */
    @Override Iterator<IToken> iterator();

    /**
     * @return An iterator that includes ambiguous tokens. No order is guaranteed on the offsets of the returned tokens.
     */
    Iterable<IToken> ambiguousTokens();

}
