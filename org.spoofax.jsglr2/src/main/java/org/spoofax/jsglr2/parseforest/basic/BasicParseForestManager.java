package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class BasicParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<BasicParseForest, StackNode>>
//@formatter:on
    extends ParseForestManager<BasicParseForest, BasicParseNode, BasicDerivation, StackNode, ParseState> {

    @Override public BasicParseNode createParseNode(Parse<BasicParseForest, StackNode, ParseState> parse,
        IStackNode stack, IProduction production, BasicDerivation firstDerivation) {
        BasicParseNode parseNode = new BasicParseNode(production);

        parse.observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parse, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol,
        Parse<BasicParseForest, StackNode, ParseState> parse) {
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

    @Override public BasicDerivation createDerivation(Parse<BasicParseForest, StackNode, ParseState> parse,
        IStackNode stack, IProduction production, ProductionType productionType, BasicParseForest[] parseForests) {
        BasicDerivation derivation = new BasicDerivation(production, productionType, parseForests);

        parse.observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(Parse<BasicParseForest, StackNode, ParseState> parse, BasicParseNode parseNode,
        BasicDerivation derivation) {
        parse.observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public BasicCharacterNode createCharacterNode(Parse<BasicParseForest, StackNode, ParseState> parse) {
        BasicCharacterNode termNode = new BasicCharacterNode(parse.state.currentChar);

        parse.observing.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
