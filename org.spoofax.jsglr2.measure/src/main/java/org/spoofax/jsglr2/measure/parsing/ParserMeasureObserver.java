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
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.stack.StackLink;

class ParserMeasureObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    implements
    IParserObserver<ParseForest, Derivation, ParseNode, AbstractElkhoundStackNode<ParseForest>, AbstractParseState<?, AbstractElkhoundStackNode<ParseForest>>> {

    int length = 0;

    Set<AbstractElkhoundStackNode<ParseForest>> stackNodes = new HashSet<>();
    Set<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> stackLinks = new HashSet<>();
    Set<StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>>> stackLinksRejected = new HashSet<>();

    Set<Actor> actors = new HashSet<>();

    int doReductions = 0;
    int doLimitedReductions = 0;

    int doReductionsLR = 0;
    int doReductionsDeterministicGLR = 0;
    int doReductionsNonDeterministicGLR = 0;

    Set<Reducer> reducers = new HashSet<>();
    Set<Reducer> reducersElkhound = new HashSet<>();

    int deterministicDepthResets = 0;

    Set<HybridParseNode> parseNodes = new HashSet<>();
    Set<ParseForest> characterNodes = new HashSet<>();

    class Actor {
        AbstractElkhoundStackNode<ParseForest> stack;
        Iterable<IAction> applicableActions;

        Actor(AbstractElkhoundStackNode<ParseForest> stack, Iterable<IAction> applicableActions) {
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

        for(AbstractElkhoundStackNode<?> stackNode : stackNodes) {
            int linksOutCount = 0;

            for(StackLink<?, ?> link : stackNode.getLinks())
                linksOutCount++;

            res += linksOutCount == 1 ? 1 : 0;
        }

        return res;
    }

    @Override public void parseStart(AbstractParseState<?, AbstractElkhoundStackNode<ParseForest>> parseState) {
        length += parseState.inputStack.inputString().length();
    }

    @Override public void createStackNode(AbstractElkhoundStackNode<ParseForest> stack) {
        stackNodes.add(stack);
    }

    @Override public void createStackLink(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link) {
        stackLinks.add(link);
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        deterministicDepthResets++;
    }

    @Override public void rejectStackLink(StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> link) {
        stackLinksRejected.add(link);
    }

    @Override public void actor(AbstractElkhoundStackNode<ParseForest> stack,
        AbstractParseState<?, AbstractElkhoundStackNode<ParseForest>> parseState, Iterable<IAction> applicableActions) {
        actors.add(new Actor(stack, applicableActions));
    }

    @Override public void doReductions(AbstractParseState<?, AbstractElkhoundStackNode<ParseForest>> parseState,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce) {
        doReductions++;

        if(stack.deterministicDepth >= reduce.arity()) {
            if(parseState.activeStacks.isSingle())
                doReductionsLR++;
            else
                doReductionsDeterministicGLR++;
        } else {
            doReductionsNonDeterministicGLR++;
        }
    }

    @Override public void doLimitedReductions(AbstractParseState<?, AbstractElkhoundStackNode<ParseForest>> parseState,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> throughLink) {
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

    @Override public void createParseNode(ParseNode parseNode, IProduction production) {
        parseNodes.add((HybridParseNode) parseNode);
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        characterNodes.add(characterNode);
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        throw new IllegalStateException("Failing parses not allowed during measurements");
    }

}
