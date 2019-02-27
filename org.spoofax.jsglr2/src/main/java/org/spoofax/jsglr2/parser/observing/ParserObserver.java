package org.spoofax.jsglr2.parser.observing;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class ParserObserver<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IParserObserver<ParseForest, StackNode> {

    private int parseNodeCount = 0;
    private int stackNodeCount = 0;
    private int stackLinkCount = 0;

    protected final Map<ParseForest, Integer> parseNodeId = new HashMap<>();
    protected final Map<StackNode, Integer> stackNodeId = new HashMap<>();
    protected final Map<StackLink<ParseForest, StackNode>, Integer> stackLinkId = new HashMap<>();

    protected void registerParseNode(ParseForest parseNode) {
        parseNodeId.put(parseNode, parseNodeCount++);
    }

    protected void registerStackNode(StackNode stackNode) {
        stackNodeId.put(stackNode, stackNodeCount++);
    }

    protected void registerStackLink(StackLink<ParseForest, StackNode> stackLink) {
        stackLinkId.put(stackLink, stackLinkCount++);
    }

    protected int id(ParseForest parseNode) {
        return parseNodeId.get(parseNode);
    }

    protected int id(StackNode stackNode) {
        return stackNodeId.get(stackNode);
    }

    protected int id(StackLink<ParseForest, StackNode> stackLink) {
        return stackLinkId.get(stackLink);
    }

    @Override
    public void parseStart(AbstractParse<ParseForest, StackNode> parse) {}

    @Override
    public void parseCharacter(AbstractParse<ParseForest, StackNode> parse, Iterable<StackNode> activeStacks) {}

    @Override
    public void addActiveStack(StackNode stack) {}

    @Override
    public void addForActorStack(StackNode stack) {}

    @Override
    public void findActiveStackWithState(IState state) {}

    @Override
    public void createStackNode(StackNode stack) {
        registerStackNode(stack);
    }

    @Override
    public void createStackLink(StackLink<ParseForest, StackNode> link) {
        registerStackLink(link);
    }

    @Override
    public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {}

    @Override
    public void rejectStackLink(StackLink<ParseForest, StackNode> link) {}

    @Override
    public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {}

    @Override
    public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks) {}

    @Override
    public void actor(StackNode stack, AbstractParse<ParseForest, StackNode> parse, Iterable<IAction> applicableActions) {}

    @Override
    public void skipRejectedStack(StackNode stack) {}

    @Override
    public void addForShifter(ForShifterElement<ParseForest, StackNode> forShifterElement) {}

    @Override
    public void doReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce) {}

    @Override
    public void doLimitedReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link) {}

    @Override
    public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState) {}

    @Override
    public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {}

    @Override
    public void directLinkFound(AbstractParse<ParseForest, StackNode> parse, StackLink<ParseForest, StackNode> directLink) {}

    @Override
    public void accept(StackNode acceptingStack) {}

    @Override
    public void createParseNode(ParseForest parseNode, IProduction production) {
        registerParseNode(parseNode);
    }

    @Override
    public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {}

    @Override
    public void createCharacterNode(ParseForest characterNode, int character) {
        registerParseNode(characterNode);
    }

    @Override
    public void addDerivation(ParseForest parseNode) {}

    @Override
    public void shifter(ParseForest termNode, Queue<ForShifterElement<ParseForest, StackNode>> forShifter) {}

    @Override
    public void remark(String remark) {}

    @Override
    public void success(ParseSuccess<ParseForest, ?> success) {}

    @Override
    public void failure(ParseFailure<ParseForest, ?> failure) {}

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
        }

        return "[" + res + "]";
    }

    String forShifterQueueToString(Queue<ForShifterElement<ParseForest, StackNode>> forShifter) {
        String res = "";

        for(ForShifterElement<ParseForest, StackNode> forShifterElement : forShifter) {
            if(res.isEmpty())
                res += forShifterElementToString(forShifterElement);
            else
                res += "," + forShifterElementToString(forShifterElement);
        }

        return "[" + res + "]";
    }

    String forShifterElementToString(ForShifterElement<ParseForest, StackNode> forShifterElement) {
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
