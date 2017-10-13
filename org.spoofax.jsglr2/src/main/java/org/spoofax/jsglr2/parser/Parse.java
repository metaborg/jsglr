package org.spoofax.jsglr2.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.ActiveStacks;
import org.spoofax.jsglr2.stack.IActiveStacks;

public class Parse<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> {

    final public String filename;
    final public String inputString;
	final public int inputLength;

    public int currentOffset;
    public int currentLine;
    public int currentColumn;
	public int currentChar;
	
	public StackNode acceptingStack;
	public IActiveStacks<StackNode> activeStacks;
	public Queue<StackNode> forActor;
	public Queue<StackNode> forActorDelayed;
	public Queue<ForShifterElement<StackNode, ParseForest>> forShifter;

    public int stackNodeCount;
    public int stackLinkCount;
    public int parseNodeCount;
    
    public int ambiguities;
    
    private final List<IParserObserver<StackNode, ParseForest>> observers;
	
	public Parse(String inputString, String filename, List<IParserObserver<StackNode, ParseForest>> observers) {
        this.filename = filename;
        this.inputString = inputString;
		this.inputLength = inputString.length();

        this.stackNodeCount = 0;
        this.stackLinkCount = 0;
        this.parseNodeCount = 0;

        this.ambiguities = 0;

        Comparator<StackNode> stackNodePriorityComparator = new Comparator<StackNode>() {
            public int compare(StackNode stackNode1, StackNode stackNode2) {
                return 0; // TODO: implement priority (see P9707 Section 8.4)
            }
        };
        
        this.acceptingStack = null;
        this.activeStacks = new ActiveStacks<ParseForest, StackNode>();
        this.forActor = new ArrayDeque<StackNode>();
        this.forActorDelayed = new PriorityQueue<StackNode>(stackNodePriorityComparator);
        this.forShifter = new ArrayDeque<ForShifterElement<StackNode, ParseForest>>();

        this.currentOffset = 0;
        this.currentLine = 1;
        this.currentColumn = 1;
		this.currentChar = getChar(currentOffset);
		
		this.observers = new ArrayList<IParserObserver<StackNode, ParseForest>>(observers);
	}
	
	public Position currentPosition() {
	    return new Position(currentOffset, currentLine, currentColumn);
	}
	
	public boolean hasNext() {
		return currentOffset < inputLength;
	}
	
	public int next() throws ParseException {
		currentOffset++;
		
		currentChar = getChar(currentOffset);
		
		if (currentChar > 256)
		    throw new ParseException("Unicode not supported");
		
		if (ICharacters.isNewLine(currentChar)) {
		    currentLine++;
		    currentColumn = 1;
		} else {
		    currentColumn++;
		}
		
		return currentChar;
	}
	
	private int getChar(int position) {
		return position < inputLength ? inputString.charAt(position) : ICharacters.EOF;
	}
	
	public String getPart(int begin, int end) {
		return inputString.substring(begin, end);
	}
	
	public String getLookahead(int length) {
	    return getPart(currentOffset + 1, Math.min(currentOffset + 1 + length, inputLength));
	}
	
	public boolean hasNextActorStack() {
		return !forActor.isEmpty() || !forActorDelayed.isEmpty();
	}
	
	public StackNode getNextActorStack() {
		// First return all actors in forActor
		if (!forActor.isEmpty())
			return forActor.remove();
		
		// Then return actors from forActorDelayed
		return forActorDelayed.remove();
	}
    
    public void notify(IParserNotification<StackNode, ParseForest> notification) {
        for (IParserObserver<StackNode, ParseForest> observer : observers)
            notification.notify(observer);
    }

}
