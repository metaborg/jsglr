package org.spoofax.jsglr2.parser;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IActionQuery;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

import com.google.common.collect.Maps;

public abstract class AbstractParse
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    implements IActionQuery {

    final public String filename;
    final public String inputString;
    final public int inputLength;

    final public Map<Integer, Object> longestMatchPos = Maps.newHashMap();

    public int currentChar; // Current ASCII char in range [0, 256]
    public int currentOffset, currentLine, currentColumn;

    private static final int TAB_SIZE = 8;

    public StackNode acceptingStack;
    public IActiveStacks<StackNode> activeStacks;
    public IForActorStacks<StackNode> forActorStacks;
    public Queue<ForShifterElement<StackNode>> forShifter;

    public final ParserObserving<ParseForest, StackNode> observing;

    public AbstractParse(String inputString, String filename, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory, ParserObserving<ParseForest, StackNode> observing) {
        this.filename = filename;
        this.inputString = inputString;
        this.inputLength = inputString.length();

        this.acceptingStack = null;
        this.activeStacks = activeStacksFactory.get(observing);
        this.forActorStacks = forActorStacksFactory.get(observing);
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

    public void next() {
        currentOffset++;
        currentChar = getChar(currentOffset);

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

    @Override public int actionQueryCharacter() {
        return currentChar;
    }

    @Override public String actionQueryLookahead(int length) {
        return inputString.substring(currentOffset + 1, Math.min(currentOffset + 1 + length, inputLength));
    }

}
