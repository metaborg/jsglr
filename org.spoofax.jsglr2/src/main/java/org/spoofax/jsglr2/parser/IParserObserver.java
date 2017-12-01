package org.spoofax.jsglr2.parser;

import java.util.List;
import java.util.Queue;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.states.IState;

public interface IParserObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> {

    public void parseStart(Parse<StackNode, ParseForest> parse);

    public void parseCharacter(Parse<StackNode, ParseForest> parse, Iterable<StackNode> activeStacks);

    public void addActiveStack(StackNode stack);

    public void addForActorStack(StackNode stack);

    public void findActiveStackWithState(IState state);

    public void createStackNode(StackNode stack);

    public void createStackLink(StackLink<StackNode, ParseForest> link);

    public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack);

    public void rejectStackLink(StackLink<StackNode, ParseForest> link);

    public void forActorStacks(IForActorStacks<StackNode> forActorStacks);

    public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks);

    public void actor(StackNode stack, Parse<StackNode, ParseForest> parse, Iterable<IAction> applicableActions);

    public void skipRejectedStack(StackNode stack);

    public void addForShifter(ForShifterElement<StackNode, ParseForest> forShifterElement);

    public void doReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce);

    public void doLimitedReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce,
        StackLink<StackNode, ParseForest> link);

    public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState);

    public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes);

    public void directLinkFound(Parse<StackNode, ParseForest> parse, StackLink<StackNode, ParseForest> directLink);

    public void accept(StackNode acceptingStack);

    public void createParseNode(ParseForest parseNode, IProduction production);

    public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes);

    public void createCharacterNode(ParseForest characterNode, int character);

    public void addDerivation(ParseForest parseNode);

    public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode, ParseForest>> forShifter);

    public void remark(String remark);

    public void success(ParseSuccess<StackNode, ParseForest, ?> success);

    public void failure(ParseFailure<StackNode, ParseForest, ?> failure);

    default String stackQueueToString(Iterable<StackNode> stacks) {
        String res = "";

        for(StackNode stack : stacks) {
            if(res.isEmpty())
                res += stack.stackNumber;
            else
                res += "," + stack.stackNumber;
        }

        return "[" + res + "]";
    }

    default String applicableActionsToString(Iterable<IAction> applicableActions) {
        String res = "";

        for(IAction action : applicableActions) {
            if(res.isEmpty())
                res += action.toString();
            else
                res += "," + action.toString();
        }

        return "[" + res + "]";
    }

    default String forShifterQueueToString(Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
        String res = "";

        for(ForShifterElement<StackNode, ParseForest> forShifterElement : forShifter) {
            if(res.isEmpty())
                res += forShifterElementToString(forShifterElement);
            else
                res += "," + forShifterElementToString(forShifterElement);
        }

        return "[" + res + "]";
    }

    default String forShifterElementToString(ForShifterElement<StackNode, ParseForest> forShifterElement) {
        return "{\"stack\":" + forShifterElement.stack.stackNumber + ",\"state\":" + forShifterElement.state.id() + "}";
    }

    default String parseForestListToString(ParseForest[] parseForests) {
        String res = "";

        for(ParseForest parseForest : parseForests) {
            if(res.isEmpty())
                res += parseForest.nodeNumber;
            else
                res += "," + parseForest.nodeNumber;
        }

        return "[" + res + "]";
    }

    default String parseForestListToString(List<ParseForest> parseForestsList) {
        @SuppressWarnings("unchecked") ParseForest[] parseForestsArray =
            (ParseForest[]) new Object[parseForestsList.size()];

        parseForestsList.toArray(parseForestsArray);

        return parseForestListToString(parseForestsArray);
    }

}
