package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;

public class BasicParseForestManager extends ParseForestManager<BasicParseForest, BasicParseNode, BasicDerivation> {

    @Override public BasicParseNode createParseNode(AbstractParse<BasicParseForest, ?> parse, IProduction production,
        BasicDerivation firstDerivation) {
        BasicParseNode parseNode = new BasicParseNode(production);

        // parse.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parse, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol,
        AbstractParse<BasicParseForest, ?> parse) {
        BasicParseNode topNode = (BasicParseNode) parseForest;
        List<BasicDerivation> result = new ArrayList<>();

        for(BasicDerivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            BasicParseNode filteredTopNode = new BasicParseNode(topNode.production);

            for(BasicDerivation derivation : result)
                filteredTopNode.addDerivation(derivation);

            return filteredTopNode;
        }
    }

    @Override public BasicDerivation createDerivation(AbstractParse<BasicParseForest, ?> parse, IProduction production,
        ProductionType productionType, BasicParseForest[] parseForests) {
        BasicDerivation derivation = new BasicDerivation(production, productionType, parseForests);

        // parse.notify(observer -> observer.createDerivation(derivation.nodeNumber, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(AbstractParse<BasicParseForest, ?> parse, BasicParseNode parseNode,
        BasicDerivation derivation) {
        // parse.notify(observer -> observer.addDerivation(parseNode));

        parseNode.addDerivation(derivation);
    }

    @Override public BasicCharacterNode createCharacterNode(AbstractParse<BasicParseForest, ?> parse) {
        BasicCharacterNode termNode = new BasicCharacterNode(parse.currentChar);

        // parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
