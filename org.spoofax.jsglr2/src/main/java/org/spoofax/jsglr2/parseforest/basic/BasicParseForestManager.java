package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.IStackNode;

public class BasicParseForestManager<Parse extends AbstractParse<BasicParseForest, ?, ?>>
    extends ParseForestManager<BasicParseForest, BasicParseNode, BasicDerivation, Parse> {

    @Override public BasicParseNode createParseNode(Parse parse, IStackNode stack, IProduction production,
        BasicDerivation firstDerivation) {
        BasicParseNode parseNode = new BasicParseNode(production);

        parse.observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parse, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol, Parse parse) {
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

    @Override public BasicDerivation createDerivation(Parse parse, IStackNode stack, IProduction production,
        ProductionType productionType, BasicParseForest[] parseForests) {
        BasicDerivation derivation = new BasicDerivation(production, productionType, parseForests);

        parse.observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(Parse parse, BasicParseNode parseNode, BasicDerivation derivation) {
        parse.observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public BasicCharacterNode createCharacterNode(Parse parse) {
        BasicCharacterNode termNode = new BasicCharacterNode(parse.currentChar);

        parse.observing.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
