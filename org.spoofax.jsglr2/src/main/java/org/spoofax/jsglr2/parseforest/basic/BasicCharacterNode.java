package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parser.Position;

public class BasicCharacterNode extends BasicParseForest implements ICharacterNode {

    public final int character;

    public BasicCharacterNode(Position position, int character) {
        super(position, CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override public String descriptor() {
        return "'" + CharacterClassFactory.intToString(this.character) + "'";
    }

    @Override public String toString() {
        return "'" + CharacterClassFactory.intToString(this.character) + "'";
    }

}
