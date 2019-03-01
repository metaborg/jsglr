package org.spoofax.jsglr2.datadependent;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.basic.BasicCharacterNode;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class DataDependentParseForestManager
    extends ParseForestManager<BasicParseForest, DataDependentParseNode, DataDependentDerivation> {

    @Override public DataDependentParseNode createParseNode(AbstractParse<BasicParseForest, ?> parse,
        Position beginPosition, IProduction production, DataDependentDerivation firstDerivation) {
        DataDependentParseNode parseNode =
            new DataDependentParseNode(beginPosition, parse.currentPosition(), production);

        // parse.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parse, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol,
        AbstractParse<BasicParseForest, ?> parse) {
        DataDependentParseNode topNode = (DataDependentParseNode) parseForest;
        List<DataDependentDerivation> result = new ArrayList<DataDependentDerivation>();

        for(DataDependentDerivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            DataDependentParseNode filteredTopNode =
                new DataDependentParseNode(topNode.getStartPosition(), topNode.getEndPosition(), topNode.production);

            for(DataDependentDerivation derivation : result)
                filteredTopNode.addDerivation(derivation);

            return filteredTopNode;
        }
    }

    @Override public DataDependentDerivation createDerivation(AbstractParse<BasicParseForest, ?> parse,
        Position beginPosition, IProduction production, ProductionType productionType,
        BasicParseForest[] parseForests) {
        DataDependentDerivation derivation = new DataDependentDerivation(beginPosition, parse.currentPosition(),
            production, productionType, parseForests);

        // parse.notify(observer -> observer.createDerivation(derivation.nodeNumber, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(AbstractParse<BasicParseForest, ?> parse, DataDependentParseNode parseNode,
        DataDependentDerivation derivation) {
        // parse.notify(observer -> observer.addDerivation(parseNode));

        parseNode.addDerivation(derivation);
    }

    @Override public BasicCharacterNode createCharacterNode(AbstractParse<BasicParseForest, ?> parse) {
        BasicCharacterNode termNode = new BasicCharacterNode(parse.currentPosition(), parse.currentChar);

        // parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
