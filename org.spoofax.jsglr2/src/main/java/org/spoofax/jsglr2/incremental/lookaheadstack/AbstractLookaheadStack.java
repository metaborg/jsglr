package org.spoofax.jsglr2.incremental.lookaheadstack;

import static org.metaborg.parsetable.characterclasses.CharacterClassFactory.EOF_INT;

public abstract class AbstractLookaheadStack implements ILookaheadStack {
    protected final String inputString;
    protected final int inputLength;
    protected int position = 0;

    public AbstractLookaheadStack(String inputString) {
        this.inputString = inputString;
        this.inputLength = inputString.length();
    }

    @Override public int actionQueryCharacter() {
        if(position < inputLength)
            return inputString.charAt(position);
        if(position == inputLength)
            return EOF_INT;
        else
            return -1;
    }

    @Override public String actionQueryLookahead(int length) {
        return inputString.substring(position + 1, Math.min(position + 1 + length, inputLength))
            + (position + 1 + length > inputLength ? (char) EOF_INT : "");
    }
}
