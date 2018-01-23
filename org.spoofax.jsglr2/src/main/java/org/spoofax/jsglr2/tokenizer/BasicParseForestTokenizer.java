package org.spoofax.jsglr2.tokenizer;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.RuleNode;
import org.spoofax.jsglr2.parseforest.basic.SymbolNode;

public class BasicParseForestTokenizer extends Tokenizer<BasicParseForest, SymbolNode, RuleNode> {

    @Override
    protected IProduction parseNodeProduction(SymbolNode symbolNode) {
        return symbolNode.production;
    }

    @Override
    protected Iterable<RuleNode> parseNodeDerivations(SymbolNode symbolNode) {
        return symbolNode.getDerivations();
    }

}
