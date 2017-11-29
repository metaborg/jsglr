package org.spoofax.jsglr2.parser;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.ActiveStacks;
import org.spoofax.jsglr2.stack.IActiveStacks;

public class Parse<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> {

    final public String filename;
    final public String inputString;
    final public int inputLength;

    public int currentChar; // Current ASCII char in range [0, 256]
    public int currentOffset, currentLine, currentColumn;

    public StackNode acceptingStack;
    public IActiveStacks<StackNode> activeStacks;
    public IForActorStacks<StackNode> forActorStacks;
    public Queue<ForShifterElement<StackNode, ParseForest>> forShifter;

    public int stackNodeCount, stackLinkCount, parseNodeCount;

    public int ambiguousParseNodes, ambiguousTreeNodes;

    private final List<IParserObserver<StackNode, ParseForest>> observers;

    public Parse(String inputString, String filename, List<IParserObserver<StackNode, ParseForest>> observers) {
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
        this.activeStacks = new ActiveStacks<>(this);
        this.forActorStacks = new ForActorStacks<>(this);
        this.forShifter = new ArrayDeque<>();

        this.currentOffset = 0;
        this.currentLine = 1;
        this.currentColumn = 1;

        this.currentChar = getChar(currentOffset);

        if(!observers.isEmpty())
            this.observers = observers;
        else
            this.observers = null;
    }

    public static Parse<?, ?> EMTPY = new Parse<>("", "", Collections.EMPTY_LIST);

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

    public void notify(IParserNotification<StackNode, ParseForest> notification) {
        if(observers == null)
            return;

        for(IParserObserver<StackNode, ParseForest> observer : observers)
            notification.notify(observer);
    }

}
