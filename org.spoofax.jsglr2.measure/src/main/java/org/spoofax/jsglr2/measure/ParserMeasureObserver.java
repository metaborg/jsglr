package org.spoofax.jsglr2.measure;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.ForShifterElement;
import org.spoofax.jsglr2.parser.IForActorStacks;
import org.spoofax.jsglr2.parser.IParserObserver;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseFailure;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;

public class ParserMeasureObserver<ParseForest extends AbstractParseForest>
    implements IParserObserver<AbstractElkhoundStackNode<ParseForest>, ParseForest> {

    public int length = 0;
    Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse;

    Set<AbstractElkhoundStackNode<ParseForest>> stackNodes = new HashSet<AbstractElkhoundStackNode<ParseForest>>();
    Set<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> stackLinks =
        new HashSet<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();
    Set<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>> stackLinksRejected =
        new HashSet<StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest>>();

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

    @Override public void parseStart(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse) {
        this.parse = parse;

        length += parse.inputLength;
    }

    @Override public void parseCharacter(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse,
        Iterable<AbstractElkhoundStackNode<ParseForest>> activeStacks) {
    }

    @Override public void addActiveStack(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void addForActorStack(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void findActiveStackWithState(IState state) {
    }

    @Override public void createStackNode(AbstractElkhoundStackNode<ParseForest> stack) {
        stackNodes.add(stack);
    }

    @Override public void createStackLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link) {
        stackLinks.add(link);
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        deterministicDepthResets++;
    }

    @Override public void rejectStackLink(StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> link) {
        stackLinksRejected.add(link);
    }

    @Override public void forActorStacks(IForActorStacks<AbstractElkhoundStackNode<ParseForest>> forActorStacks) {
    }

    @Override public void handleForActorStack(AbstractElkhoundStackNode<ParseForest> stack,
        IForActorStacks<AbstractElkhoundStackNode<ParseForest>> forActorStacks) {
    }

    @Override public void actor(AbstractElkhoundStackNode<ParseForest> stack,
        Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse, Iterable<IAction> applicableActions) {
        actors.add(new Actor(stack, applicableActions));
    }

    @Override public void skipRejectedStack(AbstractElkhoundStackNode<ParseForest> stack) {
    }

    @Override public void
        addForShifter(ForShifterElement<AbstractElkhoundStackNode<ParseForest>, ParseForest> forShifterElement) {
    }

    @Override public void doReductions(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse,
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

    @Override public void doLimitedReductions(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> throughLink) {
        doLimitedReductions++;
    }

    @Override public void reducer(AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        ParseForest[] parseNodes, AbstractElkhoundStackNode<ParseForest> activeStackWithGotoState) {
        reducers.add(new Reducer(reduce, parseNodes));
    }

    @Override public void reducerElkhound(AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        ParseForest[] parseNodes) {
        reducersElkhound.add(new Reducer(reduce, parseNodes));
    }

    @Override public void directLinkFound(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse,
        StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> directLink) {
    }

    @Override public void accept(AbstractElkhoundStackNode<ParseForest> acceptingStack) {
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        parseNodes.add((ParseNode) parseNode);
    }

    @Override public void createDerivation(int nodeNumber, IProduction production, ParseForest[] parseNodes) {
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        characterNodes.add(characterNode);
    }

    @Override public void addDerivation(ParseForest parseNode) {
    }

    @Override public void shifter(ParseForest termNode,
        Queue<ForShifterElement<AbstractElkhoundStackNode<ParseForest>, ParseForest>> forShifter) {
    }

    @Override public void remark(String remark) {
    }

    @Override public void success(ParseSuccess<AbstractElkhoundStackNode<ParseForest>, ParseForest, ?> success) {
    }

    @Override public void failure(ParseFailure<AbstractElkhoundStackNode<ParseForest>, ParseForest, ?> failure) {
        throw new IllegalStateException("Failing parses not allowed during measurements");
    }

}
