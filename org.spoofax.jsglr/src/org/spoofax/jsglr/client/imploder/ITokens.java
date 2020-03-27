package org.spoofax.jsglr.client.imploder;

import java.io.Serializable;

public interface ITokens extends Iterable<IToken>, Serializable {

    String getInput();

    int getTokenCount();

    IToken getTokenAtOffset(int offset);

    String getFilename();

    String toString(IToken left, IToken right);

    String toString(int startOffset, int endOffset);

    /**
     * Determines if the tokenizer is ambiguous. If it is, tokens with subsequent indices may not always have matching
     * start/end offsets.
     * 
     * @see Tokenizer#getTokenAfter(IToken) Gets the next token with a matching offset.
     * @see Tokenizer#getTokenBefore(IToken) Gets the previous token with a matching offset.
     */
    boolean isAmbiguous();

}
