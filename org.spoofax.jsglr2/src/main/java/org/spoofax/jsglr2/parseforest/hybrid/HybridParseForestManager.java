package org.spoofax.jsglr2.parseforest.hybrid;

import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class HybridParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends ParseForestManager<HybridParseForest, HybridDerivation, HybridParseNode, StackNode, ParseState> {

    public HybridParseForestManager(
        ParserObserving<HybridParseForest, HybridDerivation, HybridParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<?, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<HybridParseForest, HybridDerivation, HybridParseNode, StackNode_, ParseState_> factory() {
        return HybridParseForestManager::new;
    }

    @Override public HybridParseNode createParseNode(ParseState parseState, IStackNode stack, IProduction production,
        HybridDerivation firstDerivation) {
        HybridParseNode parseNode = new HybridParseNode(production, firstDerivation);

        observing.notify(observer -> observer.createParseNode(parseNode, production));
        observing.notify(observer -> observer.addDerivation(parseNode, firstDerivation));

        return parseNode;
    }

    @Override public HybridDerivation createDerivation(ParseState parseState, IStackNode stack, IProduction production,
        ProductionType productionType, HybridParseForest[] parseForests) {
        HybridDerivation derivation = new HybridDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState, HybridParseNode parseNode, HybridDerivation derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public HybridParseNode createSkippedNode(ParseState parseState, IProduction production,
        HybridParseForest[] parseForests) {
        return new HybridSkippedNode(production, parseForests);
    }

    @Override public HybridCharacterNode createCharacterNode(ParseState parseState) {
        HybridCharacterNode characterNode = new HybridCharacterNode(parseState.inputStack.getChar());

        observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public HybridParseForest[] parseForestsArray(int length) {
        return new HybridParseForest[length];
    }

    @Override protected HybridParseNode filteredTopParseNode(HybridParseNode parseNode,
        List<HybridDerivation> derivations) {
        HybridParseNode topParseNode = new HybridParseNode(parseNode.production, derivations.get(0));

        for(int i = 1; i < derivations.size(); i++)
            topParseNode.addDerivation(derivations.get(i));

        return topParseNode;
    }

}
