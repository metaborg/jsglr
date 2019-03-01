package org.spoofax.jsglr2.tokenizer;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;

public class BasicParseForestTokenizer extends Tokenizer<BasicParseForest, BasicParseNode, BasicDerivation> {

    @Override protected IProduction parseNodeProduction(BasicParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected Iterable<BasicDerivation> parseNodeDerivations(BasicParseNode parseNode) {
        return parseNode.getDerivations();
    }

}
