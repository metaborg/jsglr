package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parseforest.basic.BasicCharacterNode;
import org.spoofax.jsglr2.parser.Position;

class LayoutSensitiveCharacterNode extends BasicCharacterNode implements ILayoutSensitiveCharacterNode {

    private final Position startPosition, endPosition;

    LayoutSensitiveCharacterNode(Position position, int character) {
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
