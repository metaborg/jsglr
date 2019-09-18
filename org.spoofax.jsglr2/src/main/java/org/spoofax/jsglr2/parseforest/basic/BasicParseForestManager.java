package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class BasicParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<BasicParseForest, StackNode>>
//@formatter:on
    extends ParseForestManager<BasicParseForest, BasicParseNode, BasicDerivation, StackNode, ParseState> {

    public BasicParseForestManager(ParserObserving<BasicParseForest, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<BasicParseForest, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<BasicParseForest, BasicParseNode, BasicDerivation, StackNode_, ParseState_> factory() {
        return BasicParseForestManager::new;
    }

    @Override public BasicParseNode createParseNode(ParseState parseState, IStackNode stack, IProduction production,
        BasicDerivation firstDerivation) {
        BasicParseNode parseNode = new BasicParseNode(production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol,
        ParseState parseState) {
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

    @Override public BasicDerivation createDerivation(ParseState parseState, IStackNode stack, IProduction production,
        ProductionType productionType, BasicParseForest[] parseForests) {
        BasicDerivation derivation = new BasicDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState, BasicParseNode parseNode, BasicDerivation derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public BasicCharacterNode createCharacterNode(ParseState parseState) {
        BasicCharacterNode termNode = new BasicCharacterNode(parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
