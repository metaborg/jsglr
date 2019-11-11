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
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.ICharacterNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.recovery.IBacktrackChoicePoint;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public abstract class ParserObserver
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

    @Override public void parseRound(ParseState parseState, Iterable<StackNode> activeStacks,
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
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

    @Override public void actor(StackNode stack, ParseState parseState, Iterable<IAction> applicableActions) {
    }

    @Override public void skipRejectedStack(StackNode stack) {
    }

    @Override public void addForShifter(ForShifterElement<StackNode> forShifterElement) {
    }

    @Override public void doReductions(ParseState parseState, StackNode stack, IReduce reduce) {
    }

    @Override public void doLimitedReductions(ParseState parseState, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link) {
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
    }

    @Override public void directLinkFound(ParseState parseState, StackLink<ParseForest, StackNode> directLink) {
    }

    @Override public void accept(StackNode acceptingStack) {
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

    @Override public void addDerivation(ParseNode parseNode, Derivation derivation) {
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode>> forShifter) {
    }

    @Override public void recoveryBacktrackChoicePoint(int index, IBacktrackChoicePoint<?, StackNode> choicePoint) {
    }

    @Override public void startRecovery(ParseState parseState) {
    }

    @Override public void recoveryIteration(ParseState parseState) {
    }

    @Override public void endRecovery(ParseState parseState) {
    }

    @Override public void remark(String remark) {
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
    }

    public String stackQueueToString(Iterable<StackNode> stacks) {
        return StreamSupport.stream(stacks.spliterator(), false).map(this::id).map(Object::toString)
            .collect(Collectors.joining(","));
    }

    public String applicableActionsToString(Iterable<IAction> applicableActions) {
        return StreamSupport.stream(applicableActions.spliterator(), false).map(action -> {
            if(action instanceof IReduce)
                return action.toString() + "[" + ((IReduce) action).production().toString() + "]";
            else
                return action.toString();
        }).collect(Collectors.joining(","));
    }

    public String forShifterQueueToString(Queue<ForShifterElement<StackNode>> forShifter) {
        return StreamSupport.stream(forShifter.spliterator(), false).map(this::forShifterElementToString)
            .collect(Collectors.joining(","));
    }

    public String forShifterElementToString(ForShifterElement<StackNode> forShifterElement) {
        return "{\"stack\":" + id(forShifterElement.stack) + ",\"state\":" + forShifterElement.state.id() + "}";
    }

    public String parseForestsToString(ParseForest[] parseForests) {
        return Arrays.stream(parseForests).map(parseForest -> parseForest != null ? "" + id(parseForest) : "null")
            .collect(Collectors.joining(","));
    }

}
