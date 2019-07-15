package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveCharacterNode extends LayoutSensitiveParseForest implements ICharacterNode {

    public final int character;

    public LayoutSensitiveCharacterNode(Position position, int character) {
        super(position, CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

}
