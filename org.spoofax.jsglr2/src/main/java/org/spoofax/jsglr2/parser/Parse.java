package org.spoofax.jsglr2.parser;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacks;
import org.spoofax.jsglr2.stack.collections.ForActorStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class Parse<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    final public String filename;
    final public String inputString;
    final public int inputLength;

    public int currentChar; // Current ASCII char in range [0, 256]
    public int currentOffset, currentLine, currentColumn;

    public StackNode acceptingStack;
    public IActiveStacks<StackNode> activeStacks;
    public IForActorStacks<StackNode> forActorStacks;
    public Queue<ForShifterElement<ParseForest, StackNode>> forShifter;

    public int stackNodeCount, stackLinkCount, parseNodeCount;

    public int ambiguousParseNodes, ambiguousTreeNodes;

    public final ParserObserving<ParseForest, StackNode> observing;

    public Parse(String inputString, String filename, ParserObserving<ParseForest, StackNode> observing) {
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
        this.activeStacks = new ActiveStacks<>(observing);
        this.forActorStacks = new ForActorStacks<>(observing);
        this.forShifter = new ArrayDeque<>();

        this.currentOffset = 0;
        this.currentLine = 1;
        this.currentColumn = 1;

        this.currentChar = getChar(currentOffset);

        this.observing = observing;
    }

    public static <ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
        Parse<ParseForest, StackNode> empty() {
        return new Parse<>("", "", new ParserObserving<>());
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
            if(ICharacterClass.isNewLine(currentChar)) {
                currentLine++;
                currentColumn = 1;
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
            return ICharacterClass.EOF_INT;
    }

    public String getPart(int begin, int end) {
        return inputString.substring(begin, end);
    }

    public String getLookahead(int length) {
        return getPart(currentOffset + 1, Math.min(currentOffset + 1 + length, inputLength));
    }

}
