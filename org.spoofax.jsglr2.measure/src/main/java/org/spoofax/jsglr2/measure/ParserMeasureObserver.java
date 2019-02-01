package org.spoofax.jsglr2.measure;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class ParserMeasureObserver<ParseForest extends AbstractParseForest>
    implements IParserObserver<ParseForest, AbstractElkhoundStackNode<ParseForest>> {

    public int length = 0;
    AbstractParse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse;

    Set<AbstractElkhoundStackNode<ParseForest>> stackNodes = new HashSet<AbstractElkhoundStackNode<ParseForest>>();
    Set<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> stackLinks =
        new HashSet<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>();
    Set<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> stackLinksRejected =
        new HashSet<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>>();

    Set<Actor> actors = new HashSet<Actor>();

    public int doReductions = 0;
    public int doLimitedReductions = 0;

    public int doReductionsLR = 0;
    public int doReductionsDeterministicGLR = 0;
    public int doReductionsNonDeterministicGLR = 0;

    Set<Reducer> reducers = new HashSet<Reducer>();
    Set<Reducer> reducersElkhound = new HashSet<Reducer>();

    public int deterministicDepthResets = 0;

    Set<ParseNode> parseNodes = new HashSet<ParseNode>();
    Set<ParseForest> characterNodes = new HashSet<ParseForest>();

    class Actor {
        public AbstractElkhoundStackNode<ParseForest> stack;
        public Iterable<IAction> applicableActions;

        public Actor(AbstractElkhoundStackNode<ParseForest> stack, Iterable<IAction> applicableActions) {
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

        for(AbstractElkhoundStackNode<?> stackNode : stackNodes) {
            int linksOutCount = 0;

            for(StackLink<?, ?> link : stackNode.getLinks())
                linksOutCount++;

            res += linksOutCount == 1 ? 1 : 0;
        }

        return res;
    }

    @Override
    public void parseStart(AbstractParse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse) {
        this.parse = parse;

        length += parse.inputLength;
    }

    @Override
    public void parseCharacter(AbstractParse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse,
        Iterable<AbstractElkhoundStackNode<ParseForest>> activeStacks) {
    }

    @Override
    public void addActiveStack(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override
    public void addForActorStack(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override
    public void findActiveStackWithState(IState state) {
    }

    @Override
    public void createStackNode(AbstractElkhoundStackNode<ParseForest> stack) {
        stackNodes.add(stack);
    }

    @Override
    public void createStackLink(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link) {
        stackLinks.add(link);
    }

    @Override
    public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        deterministicDepthResets++;
    }

    @Override
    public void rejectStackLink(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link) {
        stackLinksRejected.add(link);
    }

    @Override
    public void forActorStacks(IForActorStacks<AbstractElkhoundStackNode<ParseForest>> forActorStacks) {
    }

    @Override
    public void handleForActorStack(AbstractElkhoundStackNode<ParseForest> stack,
        IForActorStacks<AbstractElkhoundStackNode<ParseForest>> forActorStacks) {
    }

    @Override
    public void actor(AbstractElkhoundStackNode<ParseForest> stack,
        AbstractParse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse, Iterable<IAction> applicableActions) {
        actors.add(new Actor(stack, applicableActions));
    }

    @Override
    public void skipRejectedStack(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override
    public void
        addForShifter(ForShifterElement<ParseForest, AbstractElkhoundStackNode<ParseForest>> forShifterElement) {
    }

    @Override
    public void doReductions(AbstractParse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce) {
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

    @Override
    public void doLimitedReductions(AbstractParse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> throughLink) {
        doLimitedReductions++;
    }

    @Override
    public void reducer(AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce, ParseForest[] parseNodes,
        AbstractElkhoundStackNode<ParseForest> activeStackWithGotoState) {
        reducers.add(new Reducer(reduce, parseNodes));
    }

    @Override
    public void reducerElkhound(AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        ParseForest[] parseNodes) {
        reducersElkhound.add(new Reducer(reduce, parseNodes));
    }

    @Override
    public void directLinkFound(AbstractParse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse,
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> directLink) {
    }

    @Override
    public void accept(AbstractElkhoundStackNode<ParseForest> acceptingStack) {
    }

    @Override
    public void createParseNode(ParseForest parseNode, IProduction production) {
        parseNodes.add((ParseNode) parseNode);
    }

    @Override
    public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
    }

    @Override
    public void createCharacterNode(ParseForest characterNode, int character) {
        characterNodes.add(characterNode);
    }

    @Override
    public void addDerivation(ParseForest parseNode) {
    }

    @Override
    public void shifter(ParseForest termNode,
        Queue<ForShifterElement<ParseForest, AbstractElkhoundStackNode<ParseForest>>> forShifter) {
    }

    @Override
    public void remark(String remark) {
    }

    @Override
    public void success(ParseSuccess<ParseForest, ?> success) {
    }

    @Override
    public void failure(ParseFailure<ParseForest, ?> failure) {
        throw new IllegalStateException("Failing parses not allowed during measurements");
    }

}
