package org.spoofax.jsglr2.benchmark;

import java.util.Queue;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.IForActorStacks;
import org.spoofax.jsglr2.parser.IParserObserver;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseFailure;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.states.IState;

public class BenchmarkParserObserver<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
    implements IParserObserver<StackNode, ParseForest> {

    @Override public void parseStart(Parse<StackNode, ParseForest> parse) {
    }

    @Override public void parseCharacter(Parse<StackNode, ParseForest> parse, Iterable<StackNode> activeStacks) {
    }

    @Override public void addActiveStack(StackNode stack) {
    }

    @Override public void addForActorStack(StackNode stack) {
    }

    @Override public void findActiveStackWithState(IState state) {
    }

    @Override public void createStackNode(StackNode stack) {
    }

    @Override public void createStackLink(StackLink<StackNode, ParseForest> link) {
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void rejectStackLink(StackLink<StackNode, ParseForest> link) {
    }

    @Override public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
    }

    @Override public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks) {
    }

    @Override public void actor(StackNode stack, Parse<StackNode, ParseForest> parse,
        Iterable<IAction> applicableActions) {
    }

    @Override public void skipRejectedStack(StackNode stack) {
    }

    @Override public void addForShifter(ForShifterElement<StackNode, ParseForest> forShifterElement) {
    }

    @Override public void doReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce) {
    }

    @Override public void doLimitedReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce,
        StackLink<StackNode, ParseForest> link) {
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
    }

    @Override public void directLinkFound(Parse<StackNode, ParseForest> parse,
        StackLink<StackNode, ParseForest> directLink) {
    }

    @Override public void accept(StackNode acceptingStack) {
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
    }

    @Override public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
    }

    @Override public void addDerivation(ParseForest parseNode) {
    }

    @Override public void shifter(ParseForest termNode, Queue<ForShifterElement<StackNode, ParseForest>> forShifter) {
    }

    @Override public void remark(String remark) {
    }

    @Override public void success(ParseSuccess<StackNode, ParseForest, ?> success) {
    }

    @Override public void failure(ParseFailure<StackNode, ParseForest, ?> failure) {
        throw new IllegalStateException("Failing parses not allowed during benchmarks");
    }

}
