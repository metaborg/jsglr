package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;

/**
 * An incremental input stack that does not have a previous parse tree, but just an input string.<br>
 * Used in the incremental parser when parsing from scratch.
 */
public class StringIncrementalInputStack extends AbstractInputStack implements IIncrementalInputStack {
    IncrementalCharacterNode currentNode;

    public StringIncrementalInputStack(String inputString) {
        super(inputString);
        createCharacterNode();
    }

    @Override public StringIncrementalInputStack clone() {
        StringIncrementalInputStack clone = new StringIncrementalInputStack(inputString);
        clone.currentNode = currentNode;
        clone.currentOffset = currentOffset;
        return clone;
    }

    private void createCharacterNode() {
        currentNode = new IncrementalCharacterNode(getChar(currentOffset));
    }

    @Override public void next() {
        currentOffset += currentNode.width();
        createCharacterNode();
    }

    @Override public IncrementalParseForest getNode() {
        return currentNode;
    }

    @Override public void breakDown() {
        // Nothing to do, character nodes cannot be broken down
    }

    @Override public boolean lookaheadIsUnchanged() {
        return false;
    }
}
