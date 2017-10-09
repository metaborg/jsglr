package org.spoofax.jsglr2.parser;

import java.util.Queue;
import java.util.logging.Logger;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class ParserLogObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> implements IParserObserver<StackNode, ParseForest> {
	
	public void parseStart(Parse<StackNode, ParseForest> parse) {
		log("\n  ---  Starting parse for input '" + parse.inputString + "'  ---\n");
	}
	
	public void parseCharacter(int character, Iterable<StackNode> activeStacks) {
		log("Parse character '" + ICharacters.charToString(character) + "' (active stacks: " + stackQueueToString(activeStacks) + ")");
	}
	
	public void createStackNode(StackNode stack) {
		log("Create new stack with number " + stack.stackNumber + " for state " + stack.state.stateNumber());
	}
	
	public void createStackLink(StackLink<StackNode, ParseForest> link) {
		log("Create link " + link.linkNumber + " from stack " + link.from.stackNumber + " to stack " + link.to.stackNumber + " with parse node " + link.parseForest.nodeNumber);
	}
	
	public void rejectStackLink(StackLink<StackNode, ParseForest> link) {
		log("Reject link " + link.linkNumber);
	}
	
    public void forActorStacks(Queue<StackNode> forActor, Queue<StackNode> forActorDelayed) {
        log("For actor: " + stackQueueToString(forActor) + ", for actor delayed: " + stackQueueToString(forActorDelayed));
    }
    
    public void actor(StackNode stack, int currentChar, Iterable<IAction> applicableActions) {
        log("Actor for stack " + stack.stackNumber + " (applicable actions: " + applicableActionsToString(applicableActions) + ")");
    }
    
    public void skipRejectedStack(StackNode stack) {
        log("Skipping stack " + stack.stackNumber + " since all links to it are rejected");
    }
	
	public void addForShifter(ForShifterElement<StackNode, ParseForest> forShifterElement) {
		log("Add for shifter " + forShifterElementToString(forShifterElement));
	}
	
	public void doReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce) {}
	
	public void doLimitedReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce, StackLink<StackNode, ParseForest> link) {}
	
	public void reducer(IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState) {
		log("Reduce by prodution " + reduce.production().productionNumber() + " (" + reduce.productionType().toString() + ") with parse nodes " + parseForestListToString(parseNodes) + ", using existing stack: " + (activeStackWithGotoState != null ? activeStackWithGotoState.stackNumber : "no"));
	}
	
	public void reducerElkhound(IReduce reduce, ParseForest[] parseNodes) {
		log("Reduce (Elkhound) by prodution " + reduce.production().productionNumber() + " (" + reduce.productionType().toString() + ") with parse nodes " + parseForestListToString(parseNodes));
	}
	
	public void directLinkFound(StackLink<StackNode, ParseForest> directLink) {
	    log("Direct link " + (directLink != null ? directLink.linkNumber : "not") + " found");
	}
	
	public void accept(StackNode acceptingStack) {
		log("Accept stack " + acceptingStack.stackNumber);
	}
	
	public void createParseNode(ParseForest parseNode, IProduction production) {
		log("Create parse node " + parseNode.nodeNumber + " for production " + production.productionNumber());
	}
	
	public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
		log("Create derivation with parse nodes " + parseForestListToString(parseNodes));
	}
	
	public void createCharacterNode(ParseForest characterNode, int character) {
		log("Create character node " + characterNode.nodeNumber + " for character '" + ICharacters.charToString(character) + "'");
	}
	
	public void addDerivation(ParseForest parseNode) {
		log("Add derivation to parse node '" + parseNode.nodeNumber);
	}
	
	public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
		log("Shifter for elements " + forShifterQueueToString(forShifter) + " with character node " + termNode.nodeNumber);
	}
	
	public void remark(String remark) {
		log(remark);
	}
	
	public void success(ParseSuccess<ParseForest> success) {
		log("Parsing succeeded. Result: " + success.parseResult.toString());
	}
	
	public void failure(ParseFailure<ParseForest> failure) {
		log("Parsing failed");
	}
	
	private void log(String message) {
		Logger.getGlobal().info(message);
	}
	
}
