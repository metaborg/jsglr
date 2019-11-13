package org.spoofax.jsglr2.measure.parsing;

import java.util.HashSet;
import java.util.Set;

import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

abstract class ParserMeasureObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    implements IParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    int length = 0;

    Set<StackNode> stackNodes = new HashSet<>();
    Set<StackLink<ParseForest, StackNode>> stackLinks = new HashSet<>();
    Set<StackLink<ParseForest, StackNode>> stackLinksRejected = new HashSet<>();

    Set<Actor> actors = new HashSet<>();

    int doReductions = 0;
    int doLimitedReductions = 0;

    int doReductionsLR = 0;
    int doReductionsDeterministicGLR = 0;
    int doReductionsNonDeterministicGLR = 0;

    Set<Reducer> reducers = new HashSet<>();
    Set<Reducer> reducersElkhound = new HashSet<>();

    int deterministicDepthResets = 0;

    Set<IParseNode> parseNodes = new HashSet<>();
    Set<ParseForest> characterNodes = new HashSet<>();

    class Actor {
        StackNode stack;
        Iterable<IAction> applicableActions;

        Actor(StackNode stack, Iterable<IAction> applicableActions) {
            this.stack = stack;
            this.applicableActions = applicableActions;
        }
    }

    class Reducer {
        IReduce reduce;
        ParseForest[] parseNodes;

        Reducer(IReduce reduce, ParseForest[] parseNodes) {
            this.reduce = reduce;
            this.parseNodes = parseNodes;
        }
    }

    int stackNodesSingleLink() {
        int res = 0;

        for(StackNode stackNode : stackNodes) {
            if(stackNodeLinkCount(stackNode) == 1)
                res++;
        }

        return res;
    }

    abstract int stackNodeLinkCount(StackNode stackNode);

    @Override public void parseStart(ParseState parseState) {
        length += parseState.inputStack.inputString().length();
    }

    @Override public void createStackNode(StackNode stack) {
        stackNodes.add(stack);
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        stackLinks.add(link);
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        deterministicDepthResets++;
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
        stackLinksRejected.add(link);
    }

    @Override public void actor(StackNode stack, ParseState parseState, Iterable<IAction> applicableActions) {
        actors.add(new Actor(stack, applicableActions));
    }

    @Override public void doReductions(ParseState parseState, StackNode stack, IReduce reduce) {
        doReductions++;
    }

    @Override public void doLimitedReductions(ParseState parseState, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        doLimitedReductions++;
    }

    @Override public void reducer(StackNode stack, IReduce reduce, ParseForest[] parseNodes,
        StackNode activeStackWithGotoState) {
        reducers.add(new Reducer(reduce, parseNodes));
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        reducersElkhound.add(new Reducer(reduce, parseNodes));
    }

    @Override public void createParseNode(ParseNode parseNode, IProduction production) {
        parseNodes.add(parseNode);
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        characterNodes.add(characterNode);
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        throw new IllegalStateException("Failing parses not allowed during measurements");
    }

}
