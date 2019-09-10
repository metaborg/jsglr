package org.spoofax.jsglr2.parseforest.empty;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridCharacterNode;
import org.spoofax.jsglr2.parseforest.hybrid.HybridDerivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.IStackNode;

public class NullParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<HybridParseForest, StackNode>>
//@formatter:on
    extends ParseForestManager<HybridParseForest, HybridParseNode, HybridDerivation, StackNode, ParseState> {

    @Override public HybridParseNode createParseNode(Parse<HybridParseForest, StackNode, ParseState> parse,
        IStackNode stack, IProduction production, HybridDerivation firstDerivation) {
        return null;
    }

    @Override public HybridParseForest filterStartSymbol(HybridParseForest parseForest, String startSymbol,
        Parse<HybridParseForest, StackNode, ParseState> parse) {
        return null;
    }

    @Override public HybridDerivation createDerivation(Parse<HybridParseForest, StackNode, ParseState> parse,
        IStackNode stack, IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        return null;
    }

    @Override public void addDerivation(Parse<HybridParseForest, StackNode, ParseState> parse,
        HybridParseNode parseNode, HybridDerivation derivation) {
    }

    @Override public HybridCharacterNode createCharacterNode(Parse<HybridParseForest, StackNode, ParseState> parse) {
        return null;
    }

    @Override public HybridParseForest[] parseForestsArray(int length) {
        return null;
    }

}
