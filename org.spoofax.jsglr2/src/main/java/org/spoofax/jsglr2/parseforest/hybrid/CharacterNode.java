package org.spoofax.jsglr2.parseforest.hybrid;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class CharacterNode extends HybridParseForest {

    public final int character;

    public CharacterNode(int nodeNumber, Parse<?, ?> parse, Position position, int character) {              
        super(nodeNumber, parse, position, 
            CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override
    public String descriptor() {
        return "'" + CharacterClassFactory.intToString(this.character) + "'";
    }

}
