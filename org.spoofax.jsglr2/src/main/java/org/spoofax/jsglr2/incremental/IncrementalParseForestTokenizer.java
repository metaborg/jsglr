package org.spoofax.jsglr2.incremental;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.tokenizer.Tokenizer;

public class IncrementalParseForestTokenizer
    extends Tokenizer<IncrementalParseForest, IncrementalParseNode, IncrementalDerivation> {

    @Override protected IProduction parseNodeProduction(IncrementalParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected Iterable<IncrementalDerivation> parseNodeDerivations(IncrementalParseNode parseNode) {
        return parseNode.getDerivations();
    }

}
