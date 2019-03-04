package org.spoofax.jsglr2.measure;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.ElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class ParserMeasureObserver<ParseForest extends IParseForest>
    implements IParserObserver<ParseForest, ElkhoundStackNode<ParseForest>> {

    public int length = 0;
    AbstractParse<ParseForest, ElkhoundStackNode<ParseForest>> parse;

    Set<ElkhoundStackNode<ParseForest>> stackNodes = new HashSet<ElkhoundStackNode<ParseForest>>();
    Set<StackLink<ParseForest, ElkhoundStackNode<ParseForest>>> stackLinks =
        new HashSet<StackLink<ParseForest, ElkhoundStackNode<ParseForest>>>();
    Set<StackLink<ParseForest, ElkhoundStackNode<ParseForest>>> stackLinksRejected =
        new HashSet<StackLink<ParseForest, ElkhoundStackNode<ParseForest>>>();

    Set<Actor> actors = new HashSet<Actor>();

    public int doReductions = 0;
    public int doLimitedReductions = 0;

    public int doReductionsLR = 0;
    public int doReductionsDeterministicGLR = 0;
    public int doReductionsNonDeterministicGLR = 0;

    Set<Reducer> reducers = new HashSet<Reducer>();
    Set<Reducer> reducersElkhound = new HashSet<Reducer>();

    public int deterministicDepthResets = 0;

    Set<HybridParseNode> parseNodes = new HashSet<HybridParseNode>();
    Set<ParseForest> characterNodes = new HashSet<ParseForest>();

    class Actor {
        public ElkhoundStackNode<ParseForest> stack;
        public Iterable<IAction> applicableActions;

        public Actor(ElkhoundStackNode<ParseForest> stack, Iterable<IAction> applicableActions) {
            this.stack = stack;
            this.applicableActions = applicableActions;
        }
    }

    class Reducer {
        public IReduce reduce;
        public ParseForest[] parseNodes;

        public Reducer(IReduce reduce, ParseForest[] parseNodes) {
            this.reduce = reduce;
            this.parseNodes = parseNodes;
        }
    }

    int stackNodesSingleLink() {
        int res = 0;

        for(ElkhoundStackNode<?> stackNode : stackNodes) {
            int linksOutCount = 0;

            for(StackLink<?, ?> link : stackNode.getLinks())
                linksOutCount++;

            res += linksOutCount == 1 ? 1 : 0;
        }

        return res;
    }

    @Override public void parseStart(AbstractParse<ParseForest, ElkhoundStackNode<ParseForest>> parse) {
        this.parse = parse;

        length += parse.inputLength;
    }

    @Override public void parseCharacter(AbstractParse<ParseForest, ElkhoundStackNode<ParseForest>> parse,
        Iterable<ElkhoundStackNode<ParseForest>> activeStacks) {
    }

    @Override public void addActiveStack(ElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void addForActorStack(ElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void findActiveStackWithState(IState state) {
    }

    @Override public void createStackNode(ElkhoundStackNode<ParseForest> stack) {
        stackNodes.add(stack);
    }

    @Override public void createStackLink(StackLink<ParseForest, ElkhoundStackNode<ParseForest>> link) {
        stackLinks.add(link);
    }

    @Override public void resetDeterministicDepth(ElkhoundStackNode<ParseForest> stack) {
        deterministicDepthResets++;
    }

    @Override public void rejectStackLink(StackLink<ParseForest, ElkhoundStackNode<ParseForest>> link) {
        stackLinksRejected.add(link);
    }

    @Override public void forActorStacks(IForActorStacks<ElkhoundStackNode<ParseForest>> forActorStacks) {
    }

    @Override public void handleForActorStack(ElkhoundStackNode<ParseForest> stack,
        IForActorStacks<ElkhoundStackNode<ParseForest>> forActorStacks) {
    }

    @Override public void actor(ElkhoundStackNode<ParseForest> stack,
        AbstractParse<ParseForest, ElkhoundStackNode<ParseForest>> parse, Iterable<IAction> applicableActions) {
        actors.add(new Actor(stack, applicableActions));
    }

    @Override public void skipRejectedStack(ElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void
        addForShifter(ForShifterElement<ParseForest, ElkhoundStackNode<ParseForest>> forShifterElement) {
    }

    @Override public void doReductions(AbstractParse<ParseForest, ElkhoundStackNode<ParseForest>> parse,
        ElkhoundStackNode<ParseForest> stack, IReduce reduce) {
        doReductions++;

        if(stack.deterministicDepth >= reduce.arity()) {
            if(parse.activeStacks.isSingle())
                doReductionsLR++;
            else
                doReductionsDeterministicGLR++;
        } else {
            doReductionsNonDeterministicGLR++;
        }
    }

    @Override public void doLimitedReductions(AbstractParse<ParseForest, ElkhoundStackNode<ParseForest>> parse,
        ElkhoundStackNode<ParseForest> stack, IReduce reduce,
        StackLink<ParseForest, ElkhoundStackNode<ParseForest>> throughLink) {
        doLimitedReductions++;
    }

    @Override public void reducer(ElkhoundStackNode<ParseForest> stack, IReduce reduce, ParseForest[] parseNodes,
        ElkhoundStackNode<ParseForest> activeStackWithGotoState) {
        reducers.add(new Reducer(reduce, parseNodes));
    }

    @Override public void reducerElkhound(ElkhoundStackNode<ParseForest> stack, IReduce reduce,
        ParseForest[] parseNodes) {
        reducersElkhound.add(new Reducer(reduce, parseNodes));
    }

    @Override public void directLinkFound(AbstractParse<ParseForest, ElkhoundStackNode<ParseForest>> parse,
        StackLink<ParseForest, ElkhoundStackNode<ParseForest>> directLink) {
    }

    @Override public void accept(ElkhoundStackNode<ParseForest> acceptingStack) {
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        parseNodes.add((HybridParseNode) parseNode);
    }

    @Override public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        characterNodes.add(characterNode);
    }

    @Override public void addDerivation(ParseForest parseNode) {
    }

    @Override public void shifter(ParseForest termNode,
        Queue<ForShifterElement<ParseForest, ElkhoundStackNode<ParseForest>>> forShifter) {
    }

    @Override public void remark(String remark) {
    }

    @Override public void success(ParseSuccess<ParseForest, ?> success) {
    }

    @Override public void failure(ParseFailure<ParseForest, ?> failure) {
        throw new IllegalStateException("Failing parses not allowed during measurements");
    }

}
