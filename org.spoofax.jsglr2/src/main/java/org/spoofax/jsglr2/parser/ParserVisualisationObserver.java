package org.spoofax.jsglr2.parser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.characters.Characters;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class ParserVisualisationObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> extends ParserLogObserver<StackNode, ParseForest> {
	
	List<String> jsonTrace = new ArrayList<String>();
	
	@Override
	public void parseStart(String inputString) {
		trace("{\"action\":\"start\",\"inputString\":\"" + inputString +  "\"}");
	}
	
	@Override
	public void parseCharacter(int character, Queue<StackNode> activeStacks) {
		trace("{\"action\":\"parseCharacter\",\"character\":\"" + Characters.charToString(character) + "\",\"activeStacks\":" + stackQueueToString(activeStacks) + "}");
	}
	
	@Override
	public void createStackNode(StackNode stack) {
		trace("{\"action\":\"createStackNode\",\"stackNumber\":" + stack.stackNumber + ",\"stateNumber\":" + stack.state.stateNumber() + "}");
	}
	
	@Override
	public void createStackLink(int linkNumber, StackNode from, StackNode to, ParseForest parseNode) {
		trace("{\"action\":\"createStackLink\",\"linkNumber\":" + linkNumber + ",\"fromStack\":" + from.stackNumber + ",\"toStack\":" + to.stackNumber + ",\"parseNode\":" + parseNode.nodeNumber + ",\"descriptor\":\"" + escape(parseNode.descriptor()) + "\"}");
	}
	
	@Override
	public void rejectStackLink(StackLink<StackNode, ParseForest> link) {
		trace("{\"action\":\"rejectStackLink\",\"linkNumber\":" + link.linkNumber + "}");
	}
    
	@Override
    public void forActorStacks(Queue<StackNode> forActor, Queue<StackNode> forActorDelayed) {
        trace("{\"action\":\"forActorStacks\",\"forActor\":" + stackQueueToString(forActor) + ",\"forActorDelayed\":" + stackQueueToString(forActorDelayed) + "}");
    }
	
	@Override
	public void actor(StackNode stack, Iterable<IAction> applicableActions) {
	    trace("{\"action\":\"actor\",\"stackNumber\":" + stack.stackNumber + "}");
	}
    
	@Override
    public void skipRejectedStack(StackNode stack) {
        trace("{\"action\":\"skipRejectedStack\",\"stackNumber\":" + stack.stackNumber + "}");
    }
	
	@Override
	public void addForShifter(ForShifterElement<StackNode, ParseForest> forShifterElement) {
	    trace("{\"action\":\"addForShifter\",\"stack\":" + forShifterElement.stack.stackNumber + ", \"state\":" + forShifterElement.state.stateNumber() + "}");
	}
	
	@Override
	public void reduce(IReduce reduce, List<? extends AbstractParseForest> parseNodes, StackNode activeStackWithGotoState) {
	    trace("{\"action\":\"reduce\",\"parseNodes\":" + parseForestListToString(parseNodes) + ",\"activeStackWithGotoState\":" + (activeStackWithGotoState != null ? activeStackWithGotoState.stackNumber : -1) + "}");
	}
    
	@Override
    public void directLinkFound(StackLink<StackNode, ParseForest> directLink) {
        trace("{\"action\":\"directLinkFound\",\"linkNumber\":" + (directLink != null ? directLink.linkNumber : -1) + "}");
    }
	
	@Override
	public void accept(StackNode acceptingStack) {
		trace("{\"action\":\"acceptStackNode\",\"stackNumber\":" + acceptingStack.stackNumber + "}");
	}
	
	@Override
	public void createParseNode(AbstractParseForest parseNode, IProduction production) {
		trace("{\"action\":\"createParseNode\",\"nodeNumber\":" + parseNode.nodeNumber + ",\"production\":" + production.productionNumber() + ",\"term\":\"" + escape(production.descriptor()) + "\"}");
	}
	
	@Override
	public void createDerivation(AbstractParseForest[] parseNodes) {
		trace("{\"action\":\"createDerivation\",\"subTrees\":" + parseForestListToString(parseNodes) + "}");
	}
	
	@Override
	public void createCharacterNode(AbstractParseForest parseNode, int character) {
		trace("{\"action\":\"createCharacterNode\",\"nodeNumber\":" + parseNode.nodeNumber + ",\"character\":\"" + Characters.charToString(character) + "\"}");
	}
	
	@Override
	public void addDerivation(AbstractParseForest parseNode) {
		trace("{\"action\":\"addDerivation\",\"parseNode\":" + parseNode.nodeNumber + "}");
	}
	
	@Override
	public void shifter(AbstractParseForest termNode, Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
        trace("{\"action\":\"shifter\",\"termNode\":" + termNode.nodeNumber + ",\"elements\":" + forShifterQueueToString(forShifter) + "}");
	}
	
	@Override
	public void remark(String remark) {
		trace("{\"action\":\"remark\",\"remark\":\"" + remark + "\"}");
	}
	
	@Override
	public void success(ParseSuccess<ParseForest> success) {
		trace("{\"action\":\"success\"}");
	}

	@Override
	public void failure(ParseFailure<ParseForest> failure) {
		trace("{\"action\":\"failure\"}");
	}
	
	private void trace(String json) {
		jsonTrace.add(json);
	}
    
    public String toJson() {
        String res = "";
        
        for (String action : jsonTrace) {
            res += "\n\t";
            
            if (res.isEmpty())
                res += action;
            else
                res += "," + action;
        }
        
        return "[" + res + "]";
    }
    
    public void toJsonFile(String filename) throws FileNotFoundException {
        try(PrintWriter out = new PrintWriter(filename)) {
            out.println(toJson());
        }
    }
	
	private String escape(String string) {
		return string.replaceAll("\"", Matcher.quoteReplacement("\\\""));
	}
	
}
