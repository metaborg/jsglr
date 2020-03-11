package org.spoofax.jsglr2.incremental.parseforest;

import static org.metaborg.parsetable.characterclasses.ICharacterClass.EOF_INT;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalCharacterNode extends IncrementalParseForest implements ICharacterNode {

    public static final IncrementalCharacterNode EOF_NODE = new IncrementalCharacterNode(EOF_INT);

    public final int character;

    public IncrementalCharacterNode(int character) {
        super(Character.charCount(character));
        this.character = character;
    }

    @Override public boolean isReusable() {
        return true;
    }

    @Override public boolean isReusable(IState stackState) {
        return true;
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
        return character == EOF_INT ? "" : CharacterClassFactory.intToString(character);
    }

    @Override public String getYield(int length) {
        return length > 0 ? getYield() : "";
    }
}
