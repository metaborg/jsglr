package org.spoofax.jsglr2.parser.observing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public abstract class ParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    implements IParserObserver<ParseForest, StackNode> {

    private int parseNodeCount = 0;
    private int stackNodeCount = 0;
    private int stackLinkCount = 0;

    protected final Map<IDerivation<ParseForest>, Integer> derivationId = new HashMap<>();
    protected final Map<ParseForest, Integer> parseNodeId = new HashMap<>();
    protected final Map<StackNode, Integer> stackNodeId = new HashMap<>();
    protected final Map<StackLink<ParseForest, StackNode>, Integer> stackLinkId = new HashMap<>();

    protected void registerDerivationNode(IDerivation<ParseForest> derivation) {
        // use same count as parse nodes as they're in the same tree in the visualisation
        derivationId.put(derivation, parseNodeCount++);
    }

    protected void registerParseNode(ParseForest parseNode) {
        parseNodeId.put(parseNode, parseNodeCount++);
    }

    protected void registerStackNode(StackNode stackNode) {
        stackNodeId.put(stackNode, stackNodeCount++);
    }

    protected void registerStackLink(StackLink<ParseForest, StackNode> stackLink) {
        stackLinkId.put(stackLink, stackLinkCount++);
    }

    protected int id(IDerivation<ParseForest> derivation) {
        return derivationId.get(derivation);
    }

    protected int id(ParseForest parseNode) {
        // For incremental parsing, not all nodes are registered yet because they come from a previous parse
        if(!parseNodeId.containsKey(parseNode)) {
            if(parseNode instanceof IParseNode)
                createParseNode(parseNode, ((IParseNode) parseNode).production());
            else
                createCharacterNode(parseNode, ((ICharacterNode) parseNode).character());
        }
        return parseNodeId.get(parseNode);
    }

    protected int id(StackNode stackNode) {
        return stackNodeId.get(stackNode);
    }

    protected int id(StackLink<ParseForest, StackNode> stackLink) {
        return stackLinkId.get(stackLink);
    }

    @Override public void parseStart(AbstractParse<ParseForest, StackNode> parse) {
        parseNodeCount = 0;
        stackNodeCount = 0;
        stackLinkCount = 0;
        derivationId.clear();
        parseNodeId.clear();
        stackNodeId.clear();
        stackLinkId.clear();
    }

    @Override public void parseCharacter(AbstractParse<ParseForest, StackNode> parse,
        Iterable<StackNode> activeStacks) {
    }

    @Override public void addActiveStack(StackNode stack) {
    }

    @Override public void addForActorStack(StackNode stack) {
    }

    @Override public void findActiveStackWithState(IState state) {
    }

    @Override public void createStackNode(StackNode stack) {
        registerStackNode(stack);
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        registerStackLink(link);
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
    }

    @Override public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
    }

    @Override public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks) {
    }

    @Override public void actor(StackNode stack, AbstractParse<ParseForest, StackNode> parse,
        Iterable<IAction> applicableActions) {
    }

    @Override public void skipRejectedStack(StackNode stack) {
    }

    @Override public void addForShifter(ForShifterElement<StackNode> forShifterElement) {
    }

    @Override public void doReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce) {
    }

    @Override public void doLimitedReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack,
        IReduce reduce, StackLink<ParseForest, StackNode> link) {
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
    }

    @Override public void directLinkFound(AbstractParse<ParseForest, StackNode> parse,
        StackLink<ParseForest, StackNode> directLink) {
    }

    @Override public void accept(StackNode acceptingStack) {
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        registerParseNode(parseNode);
    }

    @Override public void createDerivation(IDerivation<ParseForest> derivation, IProduction production,
        ParseForest[] parseNodes) {
        registerDerivationNode(derivation);
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        registerParseNode(characterNode);
    }

    @Override public void addDerivation(ParseForest parseNode) {
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {
    }

    @Override public void remark(String remark) {
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
    }

    String stackQueueToString(Iterable<StackNode> stacks) {
        String res = "";

        for(StackNode stack : stacks) {
            if(res.isEmpty())
                res += id(stack);
            else
                res += "," + id(stack);
        }

        return "[" + res + "]";
    }

    String applicableActionsToString(Iterable<IAction> applicableActions) {
        String res = "";

        for(IAction action : applicableActions) {
            if(res.isEmpty())
                res += action.toString();
            else
                res += "," + action.toString();
            if(action instanceof IReduce)
                res += "[" + ((IReduce) action).production().toString() + "]";
        }

        return "[" + res + "]";
    }

    String forShifterQueueToString(Queue<ForShifterElement<StackNode>> forShifter) {
        String res = "";

        for(ForShifterElement<StackNode> forShifterElement : forShifter) {
            if(res.isEmpty())
                res += forShifterElementToString(forShifterElement);
            else
                res += "," + forShifterElementToString(forShifterElement);
        }

        return "[" + res + "]";
    }

    String forShifterElementToString(ForShifterElement<StackNode> forShifterElement) {
        return "{\"stack\":" + id(forShifterElement.stack) + ",\"state\":" + forShifterElement.state.id() + "}";
    }

    String parseForestListToString(ParseForest[] parseForests) {
        String res = "";

        for(ParseForest parseForest : parseForests) {
            if(res.isEmpty())
                res += parseForest != null ? id(parseForest) : "null";
            else
                res += "," + (parseForest != null ? id(parseForest) : "null");
        }

        return "[" + res + "]";
    }

    String parseForestListToString(List<ParseForest> parseForestsList) {
        @SuppressWarnings("unchecked") ParseForest[] parseForestsArray =
            (ParseForest[]) new Object[parseForestsList.size()];

        parseForestsList.toArray(parseForestsArray);

        return parseForestListToString(parseForestsArray);
    }

}
