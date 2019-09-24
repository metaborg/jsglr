package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.basic.BasicCharacterNode;
import org.spoofax.jsglr2.parser.Position;

class LayoutSensitiveCharacterNode extends BasicCharacterNode implements ILayoutSensitiveParseForest {

    private final Position startPosition, endPosition;

    LayoutSensitiveCharacterNode(Position position, int character) {
        super(character);

        this.startPosition = position;
        this.endPosition = CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn();
    }

    @Override public Position getStartPosition() {
        return startPosition;
    }

    @Override public Position getEndPosition() {
        return endPosition;
    }

}
