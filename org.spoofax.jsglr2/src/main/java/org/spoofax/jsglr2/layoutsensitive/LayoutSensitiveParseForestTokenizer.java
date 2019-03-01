package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.tokenizer.Tokenizer;

public class LayoutSensitiveParseForestTokenizer
    extends Tokenizer<BasicParseForest, LayoutSensitiveParseNode, LayoutSensitiveDerivation> {

    @Override protected IProduction parseNodeProduction(LayoutSensitiveParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected Iterable<LayoutSensitiveDerivation> parseNodeDerivations(LayoutSensitiveParseNode parseNode) {
        return parseNode.getDerivations();
    }

}
