package org.spoofax.jsglr2.parseforest.empty;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.CharacterNode;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class NullParseForestManager extends ParseForestManager<HybridParseForest, ParseNode, Derivation> {

    @Override
    public ParseNode createParseNode(Parse<HybridParseForest, ?> parse, Position beginPosition, IProduction production,
        Derivation firstDerivation) {
        return null;
    }

    @Override
    public HybridParseForest filterStartSymbol(HybridParseForest parseForest, String startSymbol) {
        return null;
    }

    @Override
    public Derivation createDerivation(Parse<HybridParseForest, ?> parse, Position beginPosition,
        IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        return null;
    }

    @Override
    public void addDerivation(Parse<HybridParseForest, ?> parse, ParseNode parseNode, Derivation derivation) {
    }

    @Override
    public CharacterNode createCharacterNode(Parse<HybridParseForest, ?> parse) {
        return null;
    }

    @Override
    public HybridParseForest[] parseForestsArray(int length) {
        return null;
    }

}
