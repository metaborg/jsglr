package org.spoofax.jsglr2.composite;

import org.spoofax.jsglr2.parseforest.basic.BasicCharacterNode;
import org.spoofax.jsglr2.parser.Position;

class CompositeCharacterNode extends BasicCharacterNode implements ICompositeCharacterNode {

    private final Position startPosition, endPosition;

    CompositeCharacterNode(Position position, int character) {
        super(character);

        this.startPosition = position;
        this.endPosition = getEndPosition(position, character);
    }

    @Override public Position getStartPosition() {
        return startPosition;
    }

    @Override public Position getEndPosition() {
        return endPosition;
    }

}
