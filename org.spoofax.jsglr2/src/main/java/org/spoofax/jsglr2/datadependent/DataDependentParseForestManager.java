package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
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
    ParseForestManager<DataDependentParseForest, DataDependentDerivation, DataDependentParseNode, StackNode, ParseState> {

    public DataDependentParseForestManager(
        ParserObserving<DataDependentParseForest, DataDependentDerivation, DataDependentParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<DataDependentParseForest, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<DataDependentParseForest, DataDependentDerivation, DataDependentParseNode, StackNode_, ParseState_>
        factory() {
        return DataDependentParseForestManager::new;
    }

    @Override public DataDependentParseNode createParseNode(ParseState parseState, IStackNode stack,
        IProduction production, DataDependentDerivation firstDerivation) {
        DataDependentParseNode parseNode = new DataDependentParseNode(production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

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

    @Override public DataDependentDerivation createDerivation(ParseState parseState, IStackNode stack,
        IProduction production, ProductionType productionType, DataDependentParseForest[] parseForests) {
        DataDependentDerivation derivation = new DataDependentDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState, DataDependentParseNode parseNode,
        DataDependentDerivation derivation) {

        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public DataDependentCharacterNode createCharacterNode(ParseState parseState) {
        DataDependentCharacterNode characterNode = new DataDependentCharacterNode(parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public DataDependentParseForest[] parseForestsArray(int length) {
        return new DataDependentParseForest[length];
    }

}
