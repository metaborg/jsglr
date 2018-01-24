package org.spoofax.jsglr2.parser.observing;

import java.util.List;
import java.util.Queue;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseFailure;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public interface IParserObserver<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    public void parseStart(Parse<ParseForest, StackNode> parse);

    public void parseCharacter(Parse<ParseForest, StackNode> parse, Iterable<StackNode> activeStacks);

    public void addActiveStack(StackNode stack);

    public void addForActorStack(StackNode stack);

    public void findActiveStackWithState(IState state);

    public void createStackNode(StackNode stack);

    public void createStackLink(StackLink<ParseForest, StackNode> link);

    public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack);

    public void rejectStackLink(StackLink<ParseForest, StackNode> link);

    public void forActorStacks(IForActorStacks<StackNode> forActorStacks);

    public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks);

    public void actor(StackNode stack, Parse<ParseForest, StackNode> parse, Iterable<IAction> applicableActions);

    public void skipRejectedStack(StackNode stack);

    public void addForShifter(ForShifterElement<ParseForest, StackNode> forShifterElement);

    public void doReductions(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce);

    public void doLimitedReductions(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link);

    public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState);

    public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes);

    public void directLinkFound(Parse<ParseForest, StackNode> parse, StackLink<ParseForest, StackNode> directLink);

    public void accept(StackNode acceptingStack);

    public void createParseNode(ParseForest parseNode, IProduction production);

    public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes);

    public void createCharacterNode(ParseForest characterNode, int character);

    public void addDerivation(ParseForest parseNode);

    public void shifter(ParseForest termNode, Queue<ForShifterElement<ParseForest, StackNode>> forShifter);

    public void remark(String remark);

    public void success(ParseSuccess<ParseForest, ?> success);

    public void failure(ParseFailure<ParseForest, ?> failure);

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

    default String forShifterQueueToString(Queue<ForShifterElement<ParseForest, StackNode>> forShifter) {
        String res = "";

        for(ForShifterElement<ParseForest, StackNode> forShifterElement : forShifter) {
            if(res.isEmpty())
                res += forShifterElementToString(forShifterElement);
            else
                res += "," + forShifterElementToString(forShifterElement);
        }

        return "[" + res + "]";
    }

    default String forShifterElementToString(ForShifterElement<ParseForest, StackNode> forShifterElement) {
        return "{\"stack\":" + forShifterElement.stack.stackNumber + ",\"state\":" + forShifterElement.state.id() + "}";
    }

    default String parseForestListToString(ParseForest[] parseForests) {
        String res = "";

        for(ParseForest parseForest : parseForests) {
            if(res.isEmpty())
                res += parseForest != null ? parseForest.nodeNumber : "null";
            else
                res += "," + (parseForest != null ? parseForest.nodeNumber : "null");
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
