package org.spoofax.jsglr2.parseforest.hybrid;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class HybridCharacterNode extends HybridParseForest implements ICharacterNode {

    public final int character;

    public HybridCharacterNode(Position position, int character) {
        super(position, CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

}
