package org.spoofax.jsglr2.incremental.parseforest;

import static org.metaborg.parsetable.characterclasses.CharacterClassFactory.EOF_INT;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalCharacterNode extends IncrementalParseForest implements ICharacterNode {

    public static final IncrementalCharacterNode EOF_NODE = new IncrementalCharacterNode(EOF_INT);

    public final int character;

    public IncrementalCharacterNode(int character) {
        super(1);
        this.character = character;
    }

    @Override public boolean isTerminal() {
        return true;
    }

    @Override public int character() {
        return character;
    }

    @Override public String descriptor() {
        return "'" + (character == EOF_INT ? "EOF" : character < 32 ? "\\" + character : getYield()) + "'";
    }

    @Override protected void prettyPrint(TreePrettyPrinter printer) {
        printer.println(descriptor());
    }

    @Override public String getYield() {
        return character == EOF_INT ? "" + (char) character : CharacterClassFactory.intToString(character);
    }

    @Override public String getYield(int length) {
        return length > 0 ? getYield() : "";
    }
}
