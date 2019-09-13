package org.spoofax.jsglr2.parseforest.hybrid;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;

import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class HybridParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<HybridParseForest, StackNode>>
//@formatter:on
    extends ParseForestManager<HybridParseForest, HybridParseNode, HybridDerivation, StackNode, ParseState> {

    @Override public HybridParseNode createParseNode(
        ParserObserving<HybridParseForest, StackNode, ParseState> observing, ParseState parseState, IStackNode stack,
        IProduction production, HybridDerivation firstDerivation) {
        HybridParseNode parseNode = new HybridParseNode(production, firstDerivation);

        observing.notify(observer -> observer.createParseNode(parseNode, production));
        observing.notify(observer -> observer.addDerivation(parseNode, firstDerivation));

        return parseNode;
    }

    @Override public HybridParseForest filterStartSymbol(HybridParseForest parseForest, String startSymbol,
        ParseState parseState) {
        HybridParseNode topNode = (HybridParseNode) parseForest;
        List<HybridDerivation> result = new ArrayList<>();

        for(HybridDerivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            HybridParseNode filteredTopNode = new HybridParseNode(topNode.production, result.get(0));

            for(int i = 1; i < result.size(); i++)
                filteredTopNode.addDerivation(result.get(i));

            return filteredTopNode;
        }
    }

    @Override public HybridDerivation createDerivation(
        ParserObserving<HybridParseForest, StackNode, ParseState> observing, ParseState parseState, IStackNode stack,
        IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        HybridDerivation derivation = new HybridDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParserObserving<HybridParseForest, StackNode, ParseState> observing,
        ParseState parseState, HybridParseNode parseNode, HybridDerivation derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public HybridCharacterNode createCharacterNode(
        ParserObserving<HybridParseForest, StackNode, ParseState> observing, ParseState parseState) {
        HybridCharacterNode characterNode = new HybridCharacterNode(parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public HybridParseForest[] parseForestsArray(int length) {
        return new HybridParseForest[length];
    }

}
