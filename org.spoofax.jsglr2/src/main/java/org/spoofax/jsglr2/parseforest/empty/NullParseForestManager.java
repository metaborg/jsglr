package org.spoofax.jsglr2.parseforest.empty;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridCharacterNode;
import org.spoofax.jsglr2.parseforest.hybrid.HybridDerivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class NullParseForestManager extends ParseForestManager<HybridParseForest, HybridParseNode, HybridDerivation> {

    @Override public HybridParseNode createParseNode(AbstractParse<HybridParseForest, ?> parse, Position beginPosition,
        IProduction production, HybridDerivation firstDerivation) {
        return null;
    }

    @Override public HybridParseForest filterStartSymbol(HybridParseForest parseForest, String startSymbol,
        AbstractParse<HybridParseForest, ?> parse) {
        return null;
    }

    @Override public HybridDerivation createDerivation(AbstractParse<HybridParseForest, ?> parse,
        Position beginPosition, IProduction production, ProductionType productionType,
        HybridParseForest[] parseForests) {
        return null;
    }

    @Override public void addDerivation(AbstractParse<HybridParseForest, ?> parse, HybridParseNode parseNode,
        HybridDerivation derivation) {
    }

    @Override public HybridCharacterNode createCharacterNode(AbstractParse<HybridParseForest, ?> parse) {
        return null;
    }

    @Override public HybridParseForest[] parseForestsArray(int length) {
        return null;
    }

}
