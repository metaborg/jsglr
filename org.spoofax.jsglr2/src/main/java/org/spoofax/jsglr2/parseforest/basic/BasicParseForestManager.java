package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class BasicParseForestManager extends ParseForestManager<BasicParseForest, SymbolNode, RuleNode> {

    @Override
    public SymbolNode createParseNode(AbstractParse<BasicParseForest, ?> parse, Position beginPosition, IProduction production,
        RuleNode firstDerivation) {
        SymbolNode symbolNode = new SymbolNode(beginPosition, parse.currentPosition(), production);

        // parse.notify(observer -> observer.createParseNode(symbolNode, production));

        addDerivation(parse, symbolNode, firstDerivation);

        return symbolNode;
    }

    @Override
    public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol, AbstractParse<BasicParseForest, ?> parse) {
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
            SymbolNode filteredTopNode = new SymbolNode(topNode.getStartPosition(), topNode.getEndPosition(), topNode.production);

            for(RuleNode derivation : result)
                filteredTopNode.addDerivation(derivation);

            return filteredTopNode;
        }
    }

    @Override
    public RuleNode createDerivation(AbstractParse<BasicParseForest, ?> parse, Position beginPosition, IProduction production,
        ProductionType productionType, BasicParseForest[] parseForests) {
        RuleNode ruleNode = new RuleNode(beginPosition, parse.currentPosition(), production, productionType,
            parseForests);

        // parse.notify(observer -> observer.createDerivation(ruleNode.nodeNumber, production, parseForests));

        return ruleNode;
    }

    @Override
    public void addDerivation(AbstractParse<BasicParseForest, ?> parse, SymbolNode symbolNode, RuleNode ruleNode) {
        // parse.notify(observer -> observer.addDerivation(symbolNode));

        symbolNode.addDerivation(ruleNode);
    }

    @Override
    public TermNode createCharacterNode(AbstractParse<BasicParseForest, ?> parse) {
        TermNode termNode = new TermNode(parse.currentPosition(), parse.currentChar);

        // parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override
    public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
