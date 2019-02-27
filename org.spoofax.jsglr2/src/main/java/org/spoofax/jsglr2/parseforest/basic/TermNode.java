package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class TermNode extends BasicParseForest {

    public final int character;

    public TermNode(AbstractParse<?, ?> parse, Position position, int character) {
        super(parse, position, CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override
    public String descriptor() {
        return "'" + CharacterClassFactory.intToString(this.character) + "'";
    }
    
    @Override public String toString() {
        return "'" + CharacterClassFactory.intToString(this.character) + "'";
    }

}
