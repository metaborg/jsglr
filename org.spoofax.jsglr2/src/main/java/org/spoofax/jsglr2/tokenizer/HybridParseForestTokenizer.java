package org.spoofax.jsglr2.tokenizer;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.hybrid.HybridDerivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;

public class HybridParseForestTokenizer extends Tokenizer<HybridParseForest, HybridParseNode, HybridDerivation> {

    @Override protected IProduction parseNodeProduction(HybridParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected Iterable<HybridDerivation> parseNodeDerivations(HybridParseNode parseNode) {
        return parseNode.getDerivations();
    }

}
