package org.spoofax.jsglr2.benchmark;

import java.util.Queue;

import org.spoofax.jsglr2.elkhound.ElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.IProduction;

public class BenchmarkParserObserver<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IParserObserver<ParseForest, StackNode> {

    @Override
    public void parseStart(AbstractParse<ParseForest, StackNode> parse) {
    }

    @Override
    public void parseCharacter(AbstractParse<ParseForest, StackNode> parse, Iterable<StackNode> activeStacks) {
    }

    @Override
    public void addActiveStack(StackNode stack) {
    }

    @Override
    public void addForActorStack(StackNode stack) {
    }

    @Override
    public void findActiveStackWithState(IState state) {
    }

    @Override
    public void createStackNode(StackNode stack) {
    }

    @Override
    public void createStackLink(StackLink<ParseForest, StackNode> link) {
    }

    @Override
    public void resetDeterministicDepth(ElkhoundStackNode<ParseForest> stack) {
    }

    @Override
    public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
    }

    @Override
    public void forActorStacks(IForActorStacks<StackNode> forActorStacks) {
    }

    @Override
    public void handleForActorStack(StackNode stack, IForActorStacks<StackNode> forActorStacks) {
    }

    @Override
    public void actor(StackNode stack, AbstractParse<ParseForest, StackNode> parse, Iterable<IAction> applicableActions) {
    }

    @Override
    public void skipRejectedStack(StackNode stack) {
    }

    @Override
    public void addForShifter(ForShifterElement<ParseForest, StackNode> forShifterElement) {
    }

    @Override
    public void doReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce) {
    }

    @Override
    public void doLimitedReductions(AbstractParse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> link) {
    }

    @Override
    public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes, StackNode activeStackWithGotoState) {
    }

    @Override
    public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
    }

    @Override
    public void directLinkFound(AbstractParse<ParseForest, StackNode> parse, StackLink<ParseForest, StackNode> directLink) {
    }

    @Override
    public void accept(StackNode acceptingStack) {
    }

    @Override
    public void createParseNode(ParseForest parseNode, IProduction production) {
    }

    @Override
    public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
    }

    @Override
    public void createCharacterNode(ParseForest characterNode, int character) {
    }

    @Override
    public void addDerivation(ParseForest parseNode) {
    }

    @Override
    public void shifter(ParseForest termNode, Queue<ForShifterElement<ParseForest, StackNode>> forShifter) {
    }

    @Override
    public void remark(String remark) {
    }

    @Override
    public void success(ParseSuccess<ParseForest, ?> success) {
    }

    @Override
    public void failure(ParseFailure<ParseForest, ?> failure) {
        throw new IllegalStateException("Failing parses not allowed during benchmarks");
    }

}
