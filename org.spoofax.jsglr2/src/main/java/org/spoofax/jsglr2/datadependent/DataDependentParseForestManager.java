package org.spoofax.jsglr2.datadependent;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class DataDependentParseForestManager
    extends ParseForestManager<DataDependentParseForest, DataDependentParseNode, DataDependentDerivation> {

    @Override public DataDependentParseNode createParseNode(AbstractParse<DataDependentParseForest, ?> parse,
        Position beginPosition, IProduction production, DataDependentDerivation firstDerivation) {
        DataDependentParseNode parseNode = new DataDependentParseNode(production);

        // parse.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parse, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public DataDependentParseForest filterStartSymbol(DataDependentParseForest parseForest,
        String startSymbol, AbstractParse<DataDependentParseForest, ?> parse) {
        DataDependentParseNode topNode = (DataDependentParseNode) parseForest;
        List<DataDependentDerivation> result = new ArrayList<>();

        for(DataDependentDerivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            DataDependentParseNode filteredTopNode = new DataDependentParseNode(topNode.production);

            for(DataDependentDerivation derivation : result)
                filteredTopNode.addDerivation(derivation);

            return filteredTopNode;
        }
    }

    @Override public DataDependentDerivation createDerivation(AbstractParse<DataDependentParseForest, ?> parse,
        Position beginPosition, IProduction production, ProductionType productionType,
        DataDependentParseForest[] parseForests) {
        DataDependentDerivation derivation = new DataDependentDerivation(production, productionType, parseForests);

        // parse.notify(observer -> observer.createDerivation(derivation.nodeNumber, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(AbstractParse<DataDependentParseForest, ?> parse,
        DataDependentParseNode parseNode, DataDependentDerivation derivation) {
        // parse.notify(observer -> observer.addDerivation(parseNode));

        parseNode.addDerivation(derivation);
    }

    @Override public DataDependentCharacterNode createCharacterNode(AbstractParse<DataDependentParseForest, ?> parse) {
        DataDependentCharacterNode characterNode = new DataDependentCharacterNode(parse.currentChar);

        // parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return characterNode;
    }

    @Override public DataDependentParseForest[] parseForestsArray(int length) {
        return new DataDependentParseForest[length];
    }

}
