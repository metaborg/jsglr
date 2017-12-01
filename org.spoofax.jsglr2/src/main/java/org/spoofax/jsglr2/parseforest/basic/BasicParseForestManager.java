package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class BasicParseForestManager extends ParseForestManager<BasicParseForest, SymbolNode, RuleNode> {

    @Override
    public SymbolNode createParseNode(Parse<BasicParseForest, ?> parse, Position beginPosition, IProduction production,
        RuleNode firstDerivation) {
        SymbolNode symbolNode =
            new SymbolNode(parse.parseNodeCount++, parse, beginPosition, parse.currentPosition(), production);

        parse.notify(observer -> observer.createParseNode(symbolNode, production));

        addDerivation(parse, symbolNode, firstDerivation);

        return symbolNode;
    }

    @Override
    public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol) {
        SymbolNode topNode = (SymbolNode) parseForest;
        List<RuleNode> result = new ArrayList<RuleNode>();

        for(RuleNode derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            SymbolNode filteredTopNode = new SymbolNode(topNode.nodeNumber, topNode.parse, topNode.startPosition,
                topNode.endPosition, topNode.production);

            for(RuleNode derivation : result)
                filteredTopNode.addDerivation(derivation);

            return filteredTopNode;
        }
    }

    @Override
    public RuleNode createDerivation(Parse<BasicParseForest, ?> parse, Position beginPosition, IProduction production,
        ProductionType productionType, BasicParseForest[] parseForests) {
        RuleNode ruleNode = new RuleNode(parse.parseNodeCount++, parse, beginPosition, parse.currentPosition(),
            production, productionType, parseForests);

        parse.notify(observer -> observer.createDerivation(ruleNode.nodeNumber, production, parseForests));

        return ruleNode;
    }

    @Override
    public void addDerivation(Parse<BasicParseForest, ?> parse, SymbolNode symbolNode, RuleNode ruleNode) {
        parse.notify(observer -> observer.addDerivation(symbolNode));

        boolean initNonAmbiguous = symbolNode.isAmbiguous();

        symbolNode.addDerivation(ruleNode);

        if(initNonAmbiguous && symbolNode.isAmbiguous())
            parse.ambiguousParseNodes++;
    }

    @Override
    public TermNode createCharacterNode(Parse<BasicParseForest, ?> parse) {
        TermNode termNode = new TermNode(parse.parseNodeCount++, parse, parse.currentPosition(), parse.currentChar);

        parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override
    public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
