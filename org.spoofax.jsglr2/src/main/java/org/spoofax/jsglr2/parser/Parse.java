package org.spoofax.jsglr2.parser;

import java.util.ArrayDeque;
import java.util.Queue;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseInput;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class Parse<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IParseInput {

    final public String filename;
    final public String inputString;
    final public int inputLength;

    public int currentChar; // Current ASCII char in range [0, 256]
    public int currentOffset, currentLine, currentColumn;
    
    private static final int TAB_SIZE = 8;

    public StackNode acceptingStack;
    public IActiveStacks<StackNode> activeStacks;
    public IForActorStacks<StackNode> forActorStacks;
    public Queue<ForShifterElement<ParseForest, StackNode>> forShifter;

    public int stackNodeCount, stackLinkCount, parseNodeCount;

    public int ambiguousParseNodes, ambiguousTreeNodes;

    public final ParserObserving<ParseForest, StackNode> observing;

    public Parse(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks, ParserObserving<ParseForest, StackNode> observing) {
        this.filename = filename;
        this.inputString = inputString;
        this.inputLength = inputString.length();

        this.stackNodeCount = 0;
        this.stackLinkCount = 0;
        this.parseNodeCount = 0;

        // Number of ambiguities in the parse forest
        this.ambiguousParseNodes = 0;

        // Number of ambiguities in the imploded AST (after applying post-parse filters), only available after imploding
        this.ambiguousTreeNodes = 0;

        this.acceptingStack = null;
        this.activeStacks = activeStacks;
        this.forActorStacks = forActorStacks;
        this.forShifter = new ArrayDeque<>();

        this.currentOffset = 0;
        this.currentLine = 1;
        this.currentColumn = 1;

        this.currentChar = getChar(currentOffset);

        this.observing = observing;
    }

    public Position currentPosition() {
        return new Position(currentOffset, currentLine, currentColumn);
    }

    public boolean hasNext() {
        return currentOffset <= inputLength;
    }

    public void next() throws ParseException {
        currentOffset++;
        currentChar = getChar(currentOffset);

        if(currentOffset < inputLength) {
            if(CharacterClassFactory.isNewLine(currentChar)) {
                currentLine++;
                currentColumn = 0;
            } else if (CharacterClassFactory.isTab(currentChar)) {
                currentColumn = (currentColumn / TAB_SIZE + 1) * TAB_SIZE;
            } else {
                currentColumn++;
            }
        }
    }

    private int getChar(int position) {
        if(position < inputLength) {
            char c = inputString.charAt(position);

            if(c > 255)
                throw new IllegalStateException("Unicode not supported");

            return c;
        } else
            return CharacterClassFactory.EOF_INT;
    }

    public String getPart(int begin, int end) {
        return inputString.substring(begin, end);
    }

    @Override
    public int getCurrentChar() {
        return currentChar;
    }

    @Override
    public String getLookahead(int length) {
        return getPart(currentOffset + 1, Math.min(currentOffset + 1 + length, inputLength));
    }

}
