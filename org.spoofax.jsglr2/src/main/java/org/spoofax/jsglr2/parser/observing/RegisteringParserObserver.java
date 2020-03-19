package org.spoofax.jsglr2.parser.observing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public abstract class RegisteringParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    implements IParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    private int parseNodeCount = 0;
    private int stackNodeCount = 0;
    private int stackLinkCount = 0;

    private final Map<IDerivation<ParseForest>, Integer> derivationId = new HashMap<>();
    private final Map<ParseForest, Integer> parseNodeId = new HashMap<>();
    private final Map<StackNode, Integer> stackNodeId = new HashMap<>();
    private final Map<StackLink<ParseForest, StackNode>, Integer> stackLinkId = new HashMap<>();

    private void registerDerivationNode(IDerivation<ParseForest> derivation) {
        // use same count as parse nodes as they're in the same tree in the visualisation
        derivationId.put(derivation, parseNodeCount++);
    }

    private void registerParseNode(ParseForest parseNode) {
        parseNodeId.put(parseNode, parseNodeCount++);
    }

    private void registerStackNode(StackNode stackNode) {
        stackNodeId.put(stackNode, stackNodeCount++);
    }

    private void registerStackLink(StackLink<ParseForest, StackNode> stackLink) {
        stackLinkId.put(stackLink, stackLinkCount++);
    }

    protected int id(IDerivation<ParseForest> derivation) {
        return derivationId.get(derivation);
    }

    protected int id(ParseForest parseNode) {
        // For incremental parsing, not all nodes are registered yet because they come from a previous parse
        if(!parseNodeId.containsKey(parseNode)) {
            if(parseNode instanceof IParseNode)
                createParseNode((ParseNode) parseNode, ((ParseNode) parseNode).production());
            else
                createCharacterNode(parseNode, ((ICharacterNode) parseNode).character());
        }
        return parseNodeId.get(parseNode);
    }

    protected int id(StackNode stackNode) {
        return stackNodeId.get(stackNode);
    }

    protected String stackNodeString(StackNode stackNode) {
        return id(stackNode) + "[state=" + stackNode.state().id() + "]";
    }

    protected String id(StackLink<ParseForest, StackNode> stackLink) {
        return stackLinkId.get(stackLink) + "[parseNode="
            + (stackLink.parseForest != null ? id(stackLink.parseForest) : "null") + "]";
    }

    @Override public void parseStart(ParseState parseState) {
        parseNodeCount = 0;
        stackNodeCount = 0;
        stackLinkCount = 0;
        derivationId.clear();
        parseNodeId.clear();
        stackNodeId.clear();
        stackLinkId.clear();
    }

    @Override public void createStackNode(StackNode stack) {
        registerStackNode(stack);
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        registerStackLink(link);
    }

    @Override public void createParseNode(ParseNode parseNode, IProduction production) {
        registerParseNode((ParseForest) parseNode);
    }

    @Override public void createDerivation(Derivation derivation, IProduction production, ParseForest[] parseNodes) {
        registerDerivationNode(derivation);
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        registerParseNode(characterNode);
    }

    protected String stackQueueToString(Iterable<StackNode> stacks) {
        return "[" + StreamSupport.stream(stacks.spliterator(), false).map(this::id).map(Object::toString)
            .collect(Collectors.joining(",")) + "]";
    }

    protected String applicableActionsToString(Iterable<IAction> applicableActions) {
        return "[" + StreamSupport.stream(applicableActions.spliterator(), false).map(action -> {
            if(action instanceof IReduce)
                return action.toString() + "[" + ((IReduce) action).production().toString() + "]";
            else
                return action.toString();
        }).collect(Collectors.joining(",")) + "]";
    }

    protected String forShifterQueueToString(Queue<ForShifterElement<StackNode>> forShifter) {
        return "[" + forShifter.stream().map(this::forShifterElementToString).collect(Collectors.joining(",")) + "]";
    }

    protected String forShifterElementToString(ForShifterElement<StackNode> forShifterElement) {
        return "(" + id(forShifterElement.stack) + "," + forShifterElement.state.id() + ")";
    }

    protected String parseForestsToString(ParseForest[] parseForests) {
        return "[" + Arrays.stream(parseForests).map(parseForest -> parseForest != null ? "" + id(parseForest) : "null")
            .collect(Collectors.joining(",")) + "]";
    }

}
