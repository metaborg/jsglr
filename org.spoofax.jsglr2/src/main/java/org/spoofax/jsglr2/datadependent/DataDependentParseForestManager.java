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
   <ParseForest extends IDataDependentParseForest,
    Derivation  extends IDataDependentDerivation<ParseForest>,
    ParseNode   extends IDataDependentParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public DataDependentParseForestManager(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_ extends IDataDependentParseForest,
    Derivation_  extends IDataDependentDerivation<ParseForest_>,
    ParseNode_   extends IDataDependentParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<ParseForest_, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_> factory() {
        return DataDependentParseForestManager::new;
    }

    @Override public ParseNode createParseNode(ParseState parseState, IStackNode stack, IProduction production,
        Derivation firstDerivation) {
        ParseNode parseNode = (ParseNode) new DataDependentParseNode(production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public ParseForest filterStartSymbol(ParseForest parseForest, String startSymbol, ParseState parseState) {
        ParseNode topNode = (ParseNode) parseForest;
        List<Derivation> result = new ArrayList<>();

        for(Derivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production().startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            ParseNode filteredTopNode = (ParseNode) new DataDependentParseNode(topNode.production());

            for(Derivation derivation : result)
                filteredTopNode.addDerivation(derivation);

            return (ParseForest) filteredTopNode;
        }
    }

    @Override public Derivation createDerivation(ParseState parseState, IStackNode stack, IProduction production,
        ProductionType productionType, ParseForest[] parseForests) {
        Derivation derivation = (Derivation) new DataDependentDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState, ParseNode parseNode, Derivation derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public ParseForest createCharacterNode(ParseState parseState) {
        ParseForest characterNode = (ParseForest) new DataDependentCharacterNode(parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(characterNode, parseState.currentChar));

        return characterNode;
    }

    @Override public ParseForest[] parseForestsArray(int length) {
        return (ParseForest[]) new IDataDependentParseForest[length];
    }

}
