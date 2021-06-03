package org.spoofax.jsglr2.inputstack.incremental;

import static org.metaborg.parsetable.characterclasses.ICharacterClass.EOF_INT;
import static org.metaborg.parsetable.characterclasses.ICharacterClass.MAX_CHAR;

import org.spoofax.jsglr2.inputstack.IInputStack;

public abstract class AbstractInputStack implements IInputStack {
    protected final String inputString;
    protected final int inputLength;
    protected int currentOffset = 0;

    public AbstractInputStack(String inputString) {
        this.inputString = inputString;
        this.inputLength = inputString.length();
    }

    @Override public abstract AbstractInputStack clone();

    @Override public String inputString() {
        return inputString;
    }

    @Override public int offset() {
        return currentOffset;
    }

    @Override public int length() {
        return inputLength;
    }

    @Override public int actionQueryCharacter() {
        if(currentOffset < inputLength)
            return inputString.codePointAt(currentOffset);
        if(currentOffset == inputLength)
            return EOF_INT;
        else
            return -1;
    }

    @Override public int[] actionQueryLookahead(int length) {
        int[] res = new int[length];
        int nextOffset = currentOffset + Character.charCount(getChar(currentOffset));
        for(int i = 0; i < length; i++) {
            if(nextOffset >= inputLength) {
                int[] resShort = new int[i];
                System.arraycopy(res, 0, resShort, 0, i);
                return resShort;
            }
            res[i] = inputString.codePointAt(nextOffset);
            nextOffset += Character.charCount(res[i]);
        }
        return res;
    }

    @Override public int getChar(int offset) {
        if(offset < inputLength) {
            int c = inputString.codePointAt(offset);

            if(c > MAX_CHAR)
                throw new IllegalStateException("Character " + c + " not supported");

            return c;
        } else
            return EOF_INT;
    }
}
