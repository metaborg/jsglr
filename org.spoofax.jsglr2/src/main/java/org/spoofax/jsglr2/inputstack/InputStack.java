package org.spoofax.jsglr2.inputstack;

import org.spoofax.jsglr2.inputstack.incremental.AbstractInputStack;

public class InputStack extends AbstractInputStack {
    int currentChar; // Current ASCII char in range [0, MAX_CHAR] or EOF_INT

    public InputStack(String inputString) {
        super(inputString);
        currentChar = getChar(currentOffset);
    }

    @Override public InputStack clone() {
        InputStack clone = new InputStack(inputString);
        clone.currentChar = currentChar;
        clone.currentOffset = currentOffset;
        return clone;
    }

    @Override public boolean hasNext() {
        return currentOffset <= inputLength;
    }

    @Override public void next() {
        currentOffset += Character.charCount(currentChar);
        currentChar = getChar(currentOffset);
    }

    @Override public int getChar() {
        return currentChar;
    }

}
