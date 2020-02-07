package org.spoofax.jsglr2.inputstack;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveInputStack extends InputStack {

    private static final int TAB_SIZE = 8;

    private int currentLine = 1;
    private int currentColumn = 1;

    public LayoutSensitiveInputStack(String inputString) {
        super(inputString);
    }

    public Position currentPosition() {
        return new Position(currentOffset, currentLine, currentColumn);
    }

    @Override public void next() {
        super.next();

        if(currentOffset < inputLength) {
            if(CharacterClassFactory.isNewLine(currentChar)) {
                currentLine++;
                currentColumn = 0;
            } else if(CharacterClassFactory.isTab(currentChar)) {
                currentColumn = (currentColumn / TAB_SIZE + 1) * TAB_SIZE;
            } else {
                currentColumn++;
            }
        }
        if(currentOffset == inputLength)
            currentColumn++;
    }

}
