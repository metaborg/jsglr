package org.spoofax.jsglr2.tokenizer;

import org.spoofax.jsglr2.parseforest.basic.RuleNode;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.SymbolNode;
import org.spoofax.jsglr2.parsetable.IProduction;

public class BasicParseForestTokenizer extends Tokenizer<BasicParseForest, SymbolNode, RuleNode> {

    protected IProduction parseNodeProduction(SymbolNode symbolNode) {
        return symbolNode.production;
    }

    protected Iterable<RuleNode> parseNodeDerivations(SymbolNode symbolNode) {
        return symbolNode.getDerivations();
    }

}
