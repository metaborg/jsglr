package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parser.Position;

public interface ILayoutSensitiveCharacterNode extends ILayoutSensitiveParseForest {

    default Position getEndPosition(Position startPosition, int character) {
        return startPosition.next(character);
    }

    default Position getLeftPosition() {
        return null;
    }

    default Position getRightPosition() {
        return null;
    }

}
