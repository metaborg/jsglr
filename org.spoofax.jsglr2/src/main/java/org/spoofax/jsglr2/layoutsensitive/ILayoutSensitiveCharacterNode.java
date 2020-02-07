package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.Position;

public interface ILayoutSensitiveCharacterNode extends ILayoutSensitiveParseForest {

    default Position getEndPosition(Position startPosition, int character) {
        return CharacterClassFactory.isNewLine(character) ? startPosition.nextLine() : startPosition.nextColumn();
    }

    default Position getLeftPosition() {
        return null;
    }

    default Position getRightPosition() {
        return null;
    }

}
