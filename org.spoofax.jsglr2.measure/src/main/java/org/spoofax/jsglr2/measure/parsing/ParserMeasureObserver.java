package org.spoofax.jsglr2.measure.parsing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import org.spoofax.jsglr2.parser.result.ParseSuccess;
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

    long length = 0;

    private Set<StackNode> stackNodes_ = new HashSet<>();
    private Set<StackLink<ParseForest, StackNode>> stackLinks_ = new HashSet<>();

    long stackNodes = 0;
    long stackNodesSingleLink = 0;
    long stackLinks = 0;
    long stackLinksRejected = 0;

    long actors = 0;

    long doReductions = 0;
    long doLimitedReductions = 0;

    long doReductionsLR = 0;
    long doReductionsDeterministicGLR = 0;
    long doReductionsNonDeterministicGLR = 0;

    long reducers = 0;
    long reducersElkhound = 0;

    long deterministicDepthResets = 0;

    private List<IParseNode> parseNodes_ = new ArrayList<>();

    long parseNodes = 0;
    long parseNodesAmbiguous = 0;
    long parseNodesContextFree = 0;
    long parseNodesContextFreeAmbiguous = 0;
    long parseNodesLexical = 0;
    long parseNodesLexicalAmbiguous = 0;
    long parseNodesLiteral = 0;
    long parseNodesLiteralAmbiguous = 0;
    long parseNodesLayout = 0;
    long parseNodesLayoutAmbiguous = 0;
    long parseNodesSingleDerivation = 0;
    long characterNodes = 0;

    abstract int stackNodeLinkCount(StackNode stackNode);

    @Override public void parseStart(ParseState parseState) {
        length += parseState.inputStack.inputString().length();
    }

    @Override public void createStackNode(StackNode stack) {
        stackNodes_.add(stack);
    }

    @Override public void createStackLink(StackLink<ParseForest, StackNode> link) {
        stackLinks_.add(link);
    }

    @Override public void resetDeterministicDepth(AbstractElkhoundStackNode<ParseForest> stack) {
        deterministicDepthResets++;
    }

    @Override public void rejectStackLink(StackLink<ParseForest, StackNode> link) {
        stackLinksRejected++;
    }

    @Override public void actor(StackNode stack, ParseState parseState, Iterable<IAction> applicableActions) {
        actors++;
    }

    @Override public void doReductions(ParseState parseState, StackNode stack, IReduce reduce) {
        doReductions++;
    }

    @Override public void doLimitedReductions(ParseState parseState, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        doLimitedReductions++;
    }

    @Override public void reducer(ParseState parseState, StackNode activeStack, StackNode originStack, IReduce reduce,
        ParseForest[] parseNodes, StackNode gotoStack) {
        reducers++;
    }

    @Override public void reducerElkhound(StackNode stack, IReduce reduce, ParseForest[] parseNodes) {
        reducersElkhound++;
    }

    @Override public void createParseNode(ParseNode parseNode, IProduction production) {
        parseNodes_.add(parseNode);
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        characterNodes++;
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
        for(StackNode stackNode : stackNodes_) {
            stackNodes++;

            if(stackNodeLinkCount(stackNode) == 1)
                stackNodesSingleLink++;
        }

        for(StackLink stackLink : stackLinks_) {
            stackLinks++;

            if(stackLink.isRejected())
                stackLinksRejected++;
        }

        for(IParseNode parseNode : parseNodes_) {
            parseNodes++;

            boolean ambiguous = parseNode.isAmbiguous();

            if(ambiguous)
                parseNodesAmbiguous++;

            switch(parseNode.production().concreteSyntaxContext()) {
                case ContextFree:
                    parseNodesContextFree++;

                    if(ambiguous)
                        parseNodesContextFreeAmbiguous++;
                    break;
                case Lexical:
                    parseNodesLexical++;

                    if(ambiguous)
                        parseNodesLexicalAmbiguous++;
                    break;
                case Layout:
                    parseNodesLayout++;

                    if(ambiguous)
                        parseNodesLayoutAmbiguous++;
                    break;
                case Literal:
                    parseNodesLiteral++;

                    if(ambiguous)
                        parseNodesLiteralAmbiguous++;
                    break;
            }

            int derivationCount = 0;

            for(Object derivation : parseNode.getDerivations())
                derivationCount++;

            if(derivationCount == 1)
                parseNodesSingleDerivation++;
        }

        stackNodes_.clear();
        stackLinks_.clear();
        parseNodes_.clear();
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        throw new IllegalStateException("Failing parses not allowed during measurements");
    }

}
