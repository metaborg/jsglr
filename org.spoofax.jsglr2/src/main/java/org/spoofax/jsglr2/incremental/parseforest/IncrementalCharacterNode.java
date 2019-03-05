package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalCharacterNode extends IncrementalParseForest implements ICharacterNode {

    public static final IncrementalCharacterNode EOF_NODE = new IncrementalCharacterNode(CharacterClassFactory.EOF_INT);

    public final int character;

    public IncrementalCharacterNode(int character) {
        super(1);
        this.character = character;
    }

    @Override public int character() {
        return character;
    }

    @Override public String descriptor() {
        return "'" + getSource() + "'";
    }

    @Override protected void prettyPrint(TreePrettyPrinter printer) {
        printer.println(descriptor());
    }

    @Override public String getSource() {
        return CharacterClassFactory.intToString(character);
    }
}
