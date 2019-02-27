package org.spoofax.jsglr2.parseforest.hybrid;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class CharacterNode extends HybridParseForest {

    public final int character;

    public CharacterNode(AbstractParse<?, ?> parse, Position position, int character) {
        super(parse, position,
            CharacterClassFactory.isNewLine(character) ? position.nextLine() : position.nextColumn());
        this.character = character;
    }

    @Override
    public String descriptor() {
        return "'" + CharacterClassFactory.intToString(this.character) + "'";
    }
    
    protected void prettyPrint(TreePrettyPrinter printer) {
    	printer.println("'" + CharacterClassFactory.intToString(character) + "'");
    }

}
