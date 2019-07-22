package org.spoofax.jsglr2.datadependent;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.IStackNode;

public class DataDependentParseForestManager<Parse extends AbstractParse<DataDependentParseForest, ?>>
    extends ParseForestManager<DataDependentParseForest, DataDependentParseNode, DataDependentDerivation, Parse> {

    @Override public DataDependentParseNode createParseNode(Parse parse, IStackNode stack, IProduction production,
        DataDependentDerivation firstDerivation) {
        DataDependentParseNode parseNode = new DataDependentParseNode(production);

        parse.observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parse, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public DataDependentParseForest filterStartSymbol(DataDependentParseForest parseForest,
        String startSymbol, Parse parse) {
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

    @Override public DataDependentDerivation createDerivation(Parse parse, IStackNode stack, IProduction production,
        ProductionType productionType, DataDependentParseForest[] parseForests) {
        DataDependentDerivation derivation = new DataDependentDerivation(production, productionType, parseForests);

        parse.observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(Parse parse, DataDependentParseNode parseNode,
        DataDependentDerivation derivation) {

        parse.observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public DataDependentCharacterNode createCharacterNode(Parse parse) {
        DataDependentCharacterNode characterNode = new DataDependentCharacterNode(parse.currentChar);

        parse.observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public DataDependentParseForest[] parseForestsArray(int length) {
        return new DataDependentParseForest[length];
    }

}
