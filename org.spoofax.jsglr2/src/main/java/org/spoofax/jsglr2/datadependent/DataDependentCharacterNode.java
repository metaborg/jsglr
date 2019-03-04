package org.spoofax.jsglr2.datadependent;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parser.Position;

public class DataDependentCharacterNode extends DataDependentParseForest implements ICharacterNode {

    public final int character;

    public DataDependentCharacterNode(Position position, int character) {
        super(position, CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

}
