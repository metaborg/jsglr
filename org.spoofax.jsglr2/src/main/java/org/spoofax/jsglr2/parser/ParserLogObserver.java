package org.spoofax.jsglr2.parser;

import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.characters.Characters;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class ParserLogObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> implements IParserObserver<StackNode, ParseForest> {
	
	public void parseStart(String inputString) {
		log("\n  ---  Starting parse for input '" + inputString + "'  ---\n");
	}
	
	public void parseCharacter(int character, Queue<StackNode> activeStacks) {
		log("Parse character '" + Characters.charToString(character) + "' (active stacks: " + stackQueueToString(activeStacks) + ")");
	}
	
	public void createStackNode(StackNode stack) {
		log("Create new stack with number " + stack.stackNumber + " for state " + stack.state.stateNumber());
	}
	
	public void createStackLink(int linkNumber, StackNode from, StackNode to, ParseForest parseNode) {
		log("Create link " + linkNumber + " from stack " + from.stackNumber + " to stack " + to.stackNumber + " with parse node " + parseNode.nodeNumber);
	}
	
	public void rejectStackLink(StackLink<StackNode, ParseForest> link) {
		log("Reject link " + link.linkNumber);
	}
	
    public void forActorStacks(Queue<StackNode> forActor, Queue<StackNode> forActorDelayed) {
        log("For actor: " + stackQueueToString(forActor) + ", for actor delayed: " + stackQueueToString(forActorDelayed));
    }
    
    public void actor(StackNode stack, Iterable<IAction> applicableActions) {
        log("Actor for stack " + stack.stackNumber + " (applicable actions: " + applicableActionsToString(applicableActions) + ")");
    }
    
    public void skipRejectedStack(StackNode stack) {
        log("Skipping stack " + stack.stackNumber + " since all links to it are rejected");
    }
	
	public void addForShifter(ForShifterElement<StackNode, ParseForest> forShifterElement) {
		log("Add for shifter " + forShifterElementToString(forShifterElement));
	}
	
	public void reduce(IReduce reduce, List<? extends AbstractParseForest> parseNodes, StackNode activeStackWithGotoState) {
		log("Reduce by prodution " + reduce.production().productionNumber() + " (" + reduce.productionType().toString() + ") with parse nodes " + parseForestListToString(parseNodes) + ", using existing stack: " + (activeStackWithGotoState != null ? activeStackWithGotoState.stackNumber : "no"));
	}
	
	public void directLinkFound(StackLink<StackNode, ParseForest> directLink) {
	    log("Direct link " + (directLink != null ? directLink.linkNumber : "not") + " found");
	}
	
	public void accept(StackNode acceptingStack) {
		log("Accept stack " + acceptingStack.stackNumber);
	}
	
	public void createParseNode(AbstractParseForest parseNode, IProduction production) {
		log("Create parse node " + parseNode.nodeNumber + " for production " + production.productionNumber());
	}
	
	public void createDerivation(AbstractParseForest[] parseNodes) {
		log("Create derivation with parse nodes " + parseForestListToString(parseNodes));
	}
	
	public void createCharacterNode(AbstractParseForest characterNode, int character) {
		log("Create character node " + characterNode.nodeNumber + " for character '" + Characters.charToString(character) + "'");
	}
	
	public void addDerivation(AbstractParseForest parseNode) {
		log("Add derivation to parse node '" + parseNode.nodeNumber);
	}
	
	public void shifter(AbstractParseForest termNode, Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
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
	
	protected String stackQueueToString(Queue<StackNode> stacks) {
		String res = "";
		
		for (StackNode stack : stacks) {
			if (res.isEmpty())
				res += stack.stackNumber;
			else
				res += "," + stack.stackNumber;
		}
		
		return "[" + res + "]";
	}
	
	private String applicableActionsToString(Iterable<IAction> applicableActions) {
		String res = "";
		
		for (IAction action : applicableActions) {
			if (res.isEmpty())
				res += actionToString(action);
			else
				res += "," + actionToString(action);
		}
		
		return "[" + res + "]";
	}
	
	private String actionToString(IAction action) {
		switch(action.actionType())  {
		case ACCEPT:
			return "accept";
		case REDUCE:
		    IReduce reduce = ((IReduce) action);
		    
			return "reduce(" + reduce.production().productionNumber() + "/" + reduce.productionType() + ")";
		case REDUCE_LOOKAHEAD:
			return "reduce_la";
		case SHIFT:
			return "shift";
		}
		return null;
	}
	
	protected String forShifterQueueToString(Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
		String res = "";
		
		for (ForShifterElement<StackNode, ParseForest> forShifterElement : forShifter) {
			if (res.isEmpty())
				res += forShifterElementToString(forShifterElement);
			else
				res += "," + forShifterElementToString(forShifterElement);
		}
		
		return "[" + res + "]";
	}
	
	private String forShifterElementToString(ForShifterElement<StackNode, ParseForest> forShifterElement) {
		return "{\"stack\":" + forShifterElement.stack.stackNumber + ",\"state\":" + forShifterElement.state.stateNumber() + "}";
	}
    
    protected String parseForestListToString(AbstractParseForest[] parseForests) {
        String res = "";
        
        for (AbstractParseForest parseForest : parseForests) {
            if (res.isEmpty())
                res += parseForest.nodeNumber;
            else
                res += "," + parseForest.nodeNumber;
        }
        
        return "[" + res + "]";
    }
    
    protected String parseForestListToString(List<? extends AbstractParseForest> parseForestsList) {
        AbstractParseForest[] parseForestsArray = new AbstractParseForest[parseForestsList.size()];
        
        parseForestsList.toArray(parseForestsArray);
        
        return parseForestListToString(parseForestsArray);
    }
	
}
