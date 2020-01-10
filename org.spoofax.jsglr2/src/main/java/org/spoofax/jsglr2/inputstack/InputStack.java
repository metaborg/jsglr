package org.spoofax.jsglr2.inputstack;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.inputstack.incremental.AbstractInputStack;

public class InputStack extends AbstractInputStack {
    int currentChar; // Current ASCII char in range [0, 256]

    public InputStack(String inputString, @Nullable FileObject resource) {
        super(inputString, resource);
    }

    @Override public InputStack clone() {
        InputStack clone = new InputStack(inputString, resource);
        clone.currentChar = currentChar;
        clone.currentOffset = currentOffset;
        return clone;
    }

    @Override public boolean hasNext() {
        return currentOffset <= inputLength;
    }

    @Override public void next() {
        currentOffset++;
        currentChar = getChar(currentOffset);
    }

    @Override public int getChar() {
        return currentChar;
    }

}
