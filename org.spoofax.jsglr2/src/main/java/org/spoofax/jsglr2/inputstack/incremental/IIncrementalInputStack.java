package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.inputstack.IInputStack;

public interface IIncrementalInputStack extends IInputStack {
    IncrementalParseForest getNode();

    void breakDown();

    IIncrementalInputStack clone();

    default boolean hasNext() {
        return getNode() != null; // null is the lookahead of the EOF node
    }

    default int getChar() {
        throw new IllegalStateException("IncrementalInputStack does not provide single characters");
    }
}
