package org.spoofax.jsglr2.tokenizer;

import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parsetable.IProduction;

public class HybridParseForestTokenizer extends Tokenizer<HybridParseForest, ParseNode, Derivation> {

    @Override
    protected IProduction parseNodeProduction(ParseNode parseNode) {
        return parseNode.production;
    }

    @Override
    protected Iterable<Derivation> parseNodeDerivations(ParseNode parseNode) {
        return parseNode.getDerivations();
    }

}
