package org.spoofax.jsglr2.parseforest.empty;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parseforest.hybrid.HybridCharacterNode;
import org.spoofax.jsglr2.parseforest.hybrid.HybridDerivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class NullParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<HybridParseForest, StackNode>>
//@formatter:on
    extends ParseForestManager<HybridParseForest, HybridDerivation, HybridParseNode, StackNode, ParseState> {

    public NullParseForestManager(
        ParserObserving<HybridParseForest, HybridDerivation, HybridParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<HybridParseForest, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<HybridParseForest, HybridDerivation, HybridParseNode, StackNode_, ParseState_> factory() {
        return NullParseForestManager::new;
    }

    @Override public HybridParseNode createParseNode(ParseState parseState, IStackNode stack, IProduction production,
        HybridDerivation firstDerivation) {
        return null;
    }

    @Override public HybridParseForest filterStartSymbol(HybridParseForest parseForest, String startSymbol,
        ParseState parseState) {
        return null;
    }

    @Override public HybridDerivation createDerivation(ParseState parseState, IStackNode stack, IProduction production,
        ProductionType productionType, HybridParseForest[] parseForests) {
        return null;
    }

    @Override public void addDerivation(ParseState parseState, HybridParseNode parseNode, HybridDerivation derivation) {
    }

    @Override public HybridCharacterNode createCharacterNode(ParseState parseState) {
        return null;
    }

    @Override public HybridParseForest[] parseForestsArray(int length) {
        return null;
    }

}
