package org.spoofax.jsglr2.tokenizer.hybrid;

import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.tokenizer.Tokenizer;

public class HTokenizer extends Tokenizer<HParseForest, ParseNode, Derivation> {

    protected IProduction parseNodeProduction(ParseNode parseNode) {
        return parseNode.production;
    }

    protected Iterable<Derivation> parseNodeDerivations(ParseNode parseNode) {
        return parseNode.getDerivations();
    }

}
