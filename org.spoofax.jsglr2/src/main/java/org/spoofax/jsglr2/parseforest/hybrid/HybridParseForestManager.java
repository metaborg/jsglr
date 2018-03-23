package org.spoofax.jsglr2.parseforest.hybrid;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class HybridParseForestManager extends ParseForestManager<HybridParseForest, ParseNode, Derivation> {

    @Override public ParseNode createParseNode(Parse<HybridParseForest, ?> parse, Position beginPosition,
        IProduction production, Derivation firstDerivation) {
        ParseNode parseNode = new ParseNode(parse.parseNodeCount++, parse, beginPosition, parse.currentPosition(),
            production, firstDerivation);

        // parse.notify(observer -> observer.createParseNode(parseNode, production));
        // parse.notify(observer -> observer.addDerivation(parseNode));

        return parseNode;
    }

    @Override public HybridParseForest filterStartSymbol(HybridParseForest parseForest, String startSymbol) {
        ParseNode topNode = (ParseNode) parseForest;
        List<Derivation> result = new ArrayList<Derivation>();

        for(Derivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            ParseNode filteredTopNode = new ParseNode(topNode.nodeNumber, topNode.parse, topNode.startPosition,
                topNode.endPosition, topNode.production, result.get(0));

            for(int i = 1; i < result.size(); i++)
                filteredTopNode.addDerivation(result.get(i));

            return filteredTopNode;
        }
    }

    @Override public Derivation createDerivation(Parse<HybridParseForest, ?> parse, Position beginPosition,
        IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        Derivation derivation = new Derivation(production, productionType, parseForests);

        int derivationNumber = parse.parseNodeCount++;

        // parse.notify(observer -> observer.createDerivation(derivationNumber, production, derivation.parseForests));

        return derivation;
    }

    @Override public void addDerivation(Parse<HybridParseForest, ?> parse, ParseNode parseNode, Derivation derivation) {
        // parse.notify(observer -> observer.addDerivation(parseNode));

        boolean initNonAmbiguous = parseNode.isAmbiguous();

        parseNode.addDerivation(derivation);

        if(initNonAmbiguous && parseNode.isAmbiguous())
            parse.ambiguousParseNodes++;
    }

    @Override public CharacterNode createCharacterNode(Parse<HybridParseForest, ?> parse) {
        CharacterNode characterNode =
            new CharacterNode(parse.parseNodeCount++, parse, parse.currentPosition(), parse.currentChar);

        // parse.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public HybridParseForest[] parseForestsArray(int length) {
        return new HybridParseForest[length];
    }

}
