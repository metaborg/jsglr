package org.spoofax.jsglr2.parseforest.hybrid;

import org.spoofax.jsglr2.parseforest.ICharacterNode;

public class HybridCharacterNode extends HybridParseForest implements ICharacterNode {

    public final int character;

    public HybridCharacterNode(int character) {
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

}
