package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parser.Position;

public interface ILayoutSensitiveCharacterNode extends ILayoutSensitiveParseForest {

    default Position getEndPosition(Position startPosition, int character) {
        return startPosition;
    }

    default Position getLeftPosition() {
        return getStartPosition();
    }

    default Position getRightPosition() {
        return getEndPosition();
    }

}
