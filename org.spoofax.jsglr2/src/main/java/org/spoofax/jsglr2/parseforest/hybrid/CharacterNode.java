package org.spoofax.jsglr2.parseforest.hybrid;

import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class CharacterNode extends HybridParseForest {

    public final int character;

    public CharacterNode(int nodeNumber, Parse parse, Position position, int character) {
        super(nodeNumber, parse, position,
            ICharacters.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override
    public String descriptor() {
        return "'" + ICharacters.charToString(this.character) + "'";
    }

}
