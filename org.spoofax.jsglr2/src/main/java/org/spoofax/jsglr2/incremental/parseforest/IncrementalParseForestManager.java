package org.spoofax.jsglr2.incremental.parseforest;

import static org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode.NO_STATE;

import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.parseforest.Disambiguator;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class IncrementalParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<?, StackNode> & IIncrementalParseState>
//@formatter:on
    extends
    ParseForestManager<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> {

    public IncrementalParseForestManager(
        ParserObserving<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> observing,
        Disambiguator<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> disambiguator) {
        super(observing, disambiguator);
    }

    public static
//@formatter:off
   <StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<?, StackNode_> & IIncrementalParseState>
//@formatter:on
    ParseForestManagerFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode_, ParseState_>
        factory() {
        return IncrementalParseForestManager::new;
    }

    @Override public IncrementalParseNode createParseNode(ParseState parseState, IStackNode stack,
        IProduction production, IncrementalDerivation firstDerivation) {

        IState state = parseState.newParseNodesAreReusable() ? stack.state() : NO_STATE;
        IncrementalParseNode parseNode = new IncrementalParseNode(production, firstDerivation, state);

        observing.notify(observer -> observer.createParseNode(parseNode, production));
        observing.notify(observer -> observer.addDerivation(parseNode, firstDerivation));

        return parseNode;
    }

    public IncrementalParseNode createChangedParseNode(IncrementalParseForest... children) {
        IncrementalParseNode parseNode = new IncrementalParseNode(children);

        observing.notify(observer -> observer.createDerivation(parseNode.getFirstDerivation(), null, children));
        observing.notify(observer -> observer.createParseNode(parseNode, null));
        observing.notify(observer -> observer.addDerivation(parseNode, parseNode.getFirstDerivation()));

        return parseNode;
    }

    @Override public IncrementalDerivation createDerivation(ParseState parseState, IStackNode stack,
        IProduction production, ProductionType productionType, IncrementalParseForest[] parseForests) {

        IncrementalDerivation derivation = new IncrementalDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, derivation.parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState, IncrementalParseNode parseNode,
        IncrementalDerivation derivation) {

        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);

        if(disambiguator != null)
            disambiguator.disambiguate(parseState, parseNode);
    }

    @Override public IncrementalSkippedNode createSkippedNode(ParseState parseState, IStackNode stack,
        IProduction production, IncrementalParseForest[] parseForests) {
        IState state = parseState.newParseNodesAreReusable() ? stack.state() : NO_STATE;
        return new IncrementalSkippedNode(production, parseForests, state);
    }

    @Override public IncrementalParseForest createCharacterNode(ParseState parseState) {
        return createCharacterNode(parseState.inputStack.getChar());
    }

    public IncrementalParseForest createCharacterNode(int currentChar) {
        IncrementalCharacterNode characterNode = new IncrementalCharacterNode(currentChar);

        observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public IncrementalParseForest[] parseForestsArray(int length) {
        return new IncrementalParseForest[length];
    }

    @Override protected IncrementalParseNode filteredTopParseNode(IncrementalParseNode parseNode,
        List<IncrementalDerivation> derivations) {
        IncrementalParseNode topParseNode =
            new IncrementalParseNode(parseNode.production(), derivations.get(0), NO_STATE);

        for(int i = 1; i < derivations.size(); i++)
            topParseNode.addDerivation(derivations.get(i));

        return topParseNode;
    }

}
