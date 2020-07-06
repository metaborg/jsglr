package org.spoofax.jsglr2.parseforest.mock;

import org.spoofax.jsglr2.parseforest.ICharacterNode;

public class MockCharacterNode extends MockParseForest implements ICharacterNode {

    public final int character;

    public MockCharacterNode(int character) {
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

    @Override public String toString() {
        return descriptor();
    }

}
