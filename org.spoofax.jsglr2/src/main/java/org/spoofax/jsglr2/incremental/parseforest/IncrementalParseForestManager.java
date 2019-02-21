package org.spoofax.jsglr2.incremental.parseforest;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class IncrementalParseForestManager
    extends ParseForestManager<IncrementalParseForest, IncrementalParseNode, IncrementalDerivation> {

    @Override public IncrementalParseForest filterStartSymbol(IncrementalParseForest parseForest, String startSymbol,
        AbstractParse<IncrementalParseForest, ?> parse) {

        IncrementalParseNode topNode = (IncrementalParseNode) parseForest;
        List<IncrementalDerivation> result = new ArrayList<>();

        for(IncrementalDerivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            // TODO state? :(
            IncrementalParseNode filteredTopNode = new IncrementalParseNode(topNode.production, result.get(0), null);

            for(int i = 1; i < result.size(); i++)
                filteredTopNode.addDerivation(result.get(i));

            return filteredTopNode;
        }
    }

    @Override public IncrementalParseNode createParseNode(AbstractParse<IncrementalParseForest, ?> parse,
        Position ignored, IProduction production, IncrementalDerivation firstDerivation) {

        // TODO state? :(
        IncrementalParseNode parseNode = new IncrementalParseNode(production, firstDerivation, null);

        // parse.notify(observer -> observer.createParseNode(parseNode, production));
        // parse.notify(observer -> observer.addDerivation(parseNode));

        return parseNode;
    }

    @Override public IncrementalDerivation createDerivation(AbstractParse<IncrementalParseForest, ?> parse,
        Position ignored, IProduction production, ProductionType productionType,
        IncrementalParseForest[] parseForests) {

        // TODO state? :(
        IncrementalDerivation derivation = new IncrementalDerivation(production, productionType, parseForests, null);

        // parse.notify(observer -> observer.createDerivation(derivationNumber, production, derivation.parseForests));

        return derivation;
    }

    @Override public void addDerivation(AbstractParse<IncrementalParseForest, ?> parse, IncrementalParseNode parseNode,
        IncrementalDerivation derivation) {

        // parse.notify(observer -> observer.addDerivation(parseNode));

        parseNode.addDerivation(derivation);
    }

    @Override public IncrementalParseForest createCharacterNode(AbstractParse<IncrementalParseForest, ?> parse) {
        // TODO state? :(
        IncrementalCharacterNode characterNode = new IncrementalCharacterNode(parse.currentChar, null);

        // parse.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public IncrementalParseForest[] parseForestsArray(int length) {
        return new IncrementalParseForest[length];
    }
}
