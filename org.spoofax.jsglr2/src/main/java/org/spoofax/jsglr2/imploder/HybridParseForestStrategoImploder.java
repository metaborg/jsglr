package org.spoofax.jsglr2.imploder;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.hybrid.HybridDerivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.tokenizer.HybridParseForestTokenizer;

public class HybridParseForestStrategoImploder
    extends StrategoTermImploder<HybridParseForest, HybridParseNode, HybridDerivation> {

    public HybridParseForestStrategoImploder() {
        super(new HybridParseForestTokenizer());
    }

    @Override protected IProduction parseNodeProduction(HybridParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected HybridDerivation parseNodeOnlyDerivation(HybridParseNode parseNode) {
        return parseNode.getOnlyDerivation();
    }

    @Override protected List<HybridDerivation> parseNodePreferredAvoidedDerivations(HybridParseNode parseNode) {
        return parseNode.getPreferredAvoidedDerivations();
    }

    @Override protected List<HybridDerivation> longestMatchedDerivations(List<HybridDerivation> derivations) {
        // TODO remove derivations according to longest match criteria
        return derivations;
    }

}
