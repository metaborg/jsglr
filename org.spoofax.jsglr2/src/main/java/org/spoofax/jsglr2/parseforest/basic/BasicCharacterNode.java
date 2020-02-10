package org.spoofax.jsglr2.parseforest.basic;

import org.spoofax.jsglr2.parseforest.ICharacterNode;

public class BasicCharacterNode implements IBasicParseForest, ICharacterNode {

    public final int character;

    public BasicCharacterNode(int character) {
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

}
