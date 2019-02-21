package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.incremental.IncrementalParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalCharacterNode extends IncrementalParseForest {

    public static final IncrementalCharacterNode EOF_NODE =
        new IncrementalCharacterNode(CharacterClassFactory.EOF_INT, IncrementalParse.NO_STATE);

    public final int character;

    public IncrementalCharacterNode(int character, IState state) {
        super(CharacterClassFactory.isNewLine(character) ? new Position(1, 2, 1) : new Position(1, 1, 1), state);
        this.character = character;
    }

    @Override public String descriptor() {
        return "'" + getSource() + "'";
    }

    @Override protected void prettyPrint(TreePrettyPrinter printer) {
        printer.print(descriptor());
    }

    @Override public String getSource() {
        return CharacterClassFactory.intToString(character);
    }
}
