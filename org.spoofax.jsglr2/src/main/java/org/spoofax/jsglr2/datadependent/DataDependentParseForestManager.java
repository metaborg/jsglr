package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;

import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class DataDependentParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<DataDependentParseForest, StackNode>>
//@formatter:on
    extends
    ParseForestManager<DataDependentParseForest, DataDependentParseNode, DataDependentDerivation, StackNode, ParseState> {

    @Override public DataDependentParseNode createParseNode(
        ParserObserving<DataDependentParseForest, StackNode, ParseState> observing, ParseState parseState,
        IStackNode stack, IProduction production, DataDependentDerivation firstDerivation) {
        DataDependentParseNode parseNode = new DataDependentParseNode(production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(observing, parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public DataDependentParseForest filterStartSymbol(DataDependentParseForest parseForest,
        String startSymbol, ParseState parseState) {
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

    @Override public DataDependentDerivation createDerivation(
        ParserObserving<DataDependentParseForest, StackNode, ParseState> observing, ParseState parseState,
        IStackNode stack, IProduction production, ProductionType productionType,
        DataDependentParseForest[] parseForests) {
        DataDependentDerivation derivation = new DataDependentDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParserObserving<DataDependentParseForest, StackNode, ParseState> observing,
        ParseState parseState, DataDependentParseNode parseNode, DataDependentDerivation derivation) {

        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public DataDependentCharacterNode createCharacterNode(
        ParserObserving<DataDependentParseForest, StackNode, ParseState> observing, ParseState parseState) {
        DataDependentCharacterNode characterNode = new DataDependentCharacterNode(parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public DataDependentParseForest[] parseForestsArray(int length) {
        return new DataDependentParseForest[length];
    }

}
