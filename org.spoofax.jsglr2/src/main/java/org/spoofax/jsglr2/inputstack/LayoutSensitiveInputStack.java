package org.spoofax.jsglr2.inputstack;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveInputStack extends InputStack {

    private static final int TAB_SIZE = 8;

    private int currentLine;
    private int currentColumn;
    public Position previousPosition;

    public LayoutSensitiveInputStack(String inputString) {
        super(inputString);
        
        this.currentLine = 1;
        this.currentColumn = CharacterClassFactory.isNewLine(currentChar) ? 0 : 1;
        this.previousPosition = new Position(-1, currentLine, currentColumn - 1);
    }

    public Position currentPosition() {
        return new Position(currentOffset, currentLine, currentColumn);
    }

    @Override public void consumed() {
        previousPosition = currentPosition();

        if(currentOffset < inputLength) {
            if(CharacterClassFactory.isNewLine(currentChar)) {
                currentLine++;
                currentColumn = 1;
            } else if(CharacterClassFactory.isCarriageReturn(currentChar)) {
            
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
