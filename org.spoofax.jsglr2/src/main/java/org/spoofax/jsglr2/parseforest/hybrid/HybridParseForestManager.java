package org.spoofax.jsglr2.parseforest.hybrid;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.IStackNode;

public class HybridParseForestManager extends ParseForestManager<HybridParseForest, HybridParseNode, HybridDerivation> {

    @Override public HybridParseNode createParseNode(AbstractParse<HybridParseForest, ?> parse, IStackNode stack,
        IProduction production, HybridDerivation firstDerivation) {
        HybridParseNode parseNode = new HybridParseNode(production, firstDerivation);

        parse.observing.notify(observer -> observer.createParseNode(parseNode, production));
        parse.observing.notify(observer -> observer.addDerivation(parseNode));

        return parseNode;
    }

    @Override public HybridParseForest filterStartSymbol(HybridParseForest parseForest, String startSymbol,
        AbstractParse<HybridParseForest, ?> parse) {
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

    @Override public HybridDerivation createDerivation(AbstractParse<HybridParseForest, ?> parse, IStackNode stack,
        IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        HybridDerivation derivation = new HybridDerivation(production, productionType, parseForests);

        parse.observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(AbstractParse<HybridParseForest, ?> parse, HybridParseNode parseNode,
        HybridDerivation derivation) {
        parse.observing.notify(observer -> observer.addDerivation(parseNode));

        parseNode.addDerivation(derivation);
    }

    @Override public HybridCharacterNode createCharacterNode(AbstractParse<HybridParseForest, ?> parse) {
        HybridCharacterNode characterNode = new HybridCharacterNode(parse.currentChar);

        parse.observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public HybridParseForest[] parseForestsArray(int length) {
        return new HybridParseForest[length];
    }

}
