package org.spoofax.jsglr2.measure;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.IParserObserver;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseFailure;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;

public class ParserMeasureObserver<ParseForest extends AbstractParseForest> implements IParserObserver<AbstractElkhoundStackNode<ParseForest>, ParseForest> {
	
	public int length = 0;
	Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse;

	Set<AbstractElkhoundStackNode<ParseForest>> stackNodes = new HashSet<AbstractElkhoundStackNode<ParseForest>>();
	Set<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> stackLinks = new HashSet<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();
	Set<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> stackLinksRejected = new HashSet<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();

	Set<Actor> actors = new HashSet<Actor>();

	public int doReductions = 0;
	public int doLimitedReductions = 0;
	
	public int doReductionsLR = 0;
	public int doReductionsDeterministicGLR = 0;
	public int doReductionsNonDeterministicGLR = 0;
	
	Set<Reducer> reducers = new HashSet<Reducer>();
	Set<Reducer> reducersElkhound = new HashSet<Reducer>();

	public int deterministicDepthResets = 0;

	Set<ParseNode> parseNodes = new HashSet<ParseNode>();
	Set<ParseForest> characterNodes = new HashSet<ParseForest>();

	class Actor {
		public AbstractElkhoundStackNode<ParseForest> stack;
		public Iterable<IAction> applicableActions;
		
		public Actor(AbstractElkhoundStackNode<ParseForest> stack, Iterable<IAction> applicableActions) {
			this.stack = stack;
			this.applicableActions = applicableActions;
		}
	}

	class Reducer {
		public IReduce reduce;
		public ParseForest[] parseNodes;
		
		public Reducer(IReduce reduce, ParseForest[] parseNodes) {
			this.reduce = reduce;
			this.parseNodes = parseNodes;
		}
	}
	
	public void parseStart(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse) {
		this.parse = parse;
		
		length += parse.inputLength;
	}
	
	public void parseCharacter(int character, Iterable<AbstractElkhoundStackNode<ParseForest>> activeStacks) {}
	
	public void createStackNode(AbstractElkhoundStackNode<ParseForest> stack) {
		stackNodes.add(stack);
	}
	
	public void createStackLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link) {
		stackLinks.add(link);
	}
	
	public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
		deterministicDepthResets++;
	}
	
	public void rejectStackLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link) {
		stackLinksRejected.add(link);
	}
    
	public void forActorStacks(Queue<AbstractElkhoundStackNode<ParseForest>> forActor, Queue<AbstractElkhoundStackNode<ParseForest>> forActorDelayed) {}
	
	public void actor(AbstractElkhoundStackNode<ParseForest> stack, int currentChar, Iterable<IAction> applicableActions) {
	    actors.add(new Actor(stack, applicableActions));
	}
    
	public void skipRejectedStack(AbstractElkhoundStackNode<ParseForest> stack) {}
	
	public void addForShifter(ForShifterElement<AbstractElkhoundStackNode<ParseForest>, ParseForest> forShifterElement) {}
	
	public void doReductions(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce) {
		doReductions++;
		
		if (stack.deterministicDepth >= reduce.arity()) {
		    if (parse.activeStacks.size() == 1)
		    		doReductionsLR++;
	        else
	        		doReductionsDeterministicGLR++;
	    } else {
	    		doReductionsNonDeterministicGLR++;
	    }
	}
	
	public void doLimitedReductions(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce, StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> throughLink) {
		doLimitedReductions++;
	}
	
	public void reducer(IReduce reduce, ParseForest[] parseNodes, AbstractElkhoundStackNode<ParseForest> activeStackWithGotoState) {
		reducers.add(new Reducer(reduce, parseNodes));
	}
	
	public void reducerElkhound(IReduce reduce, ParseForest[] parseNodes) {
		reducersElkhound.add(new Reducer(reduce, parseNodes));
	}
    
	public void directLinkFound(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> directLink) {}
	
	public void accept(AbstractElkhoundStackNode<ParseForest> acceptingStack) {}
	
	public void createParseNode(ParseForest parseNode, IProduction production) {
		parseNodes.add((ParseNode) parseNode);
	}
	
	public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {}
	
	public void createCharacterNode(ParseForest characterNode, int character) {
		characterNodes.add(characterNode);
	}
	
	public void addDerivation(ParseForest parseNode) {}
	
	public void shifter(ParseForest termNode, Queue<ForShifterElement<AbstractElkhoundStackNode<ParseForest>, ParseForest>> forShifter) {}
	
	public void remark(String remark) {}
	
	public void success(ParseSuccess<ParseForest> success) {}

	public void failure(ParseFailure<ParseForest> failure) {
		throw new IllegalStateException("Failing parses not allowed during measurements");
	}
	
}
