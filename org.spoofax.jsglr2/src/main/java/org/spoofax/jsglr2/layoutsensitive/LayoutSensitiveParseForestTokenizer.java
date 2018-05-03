package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.tokenizer.Tokenizer;

public class LayoutSensitiveParseForestTokenizer extends Tokenizer<BasicParseForest, LayoutSensitiveSymbolNode, LayoutSensitiveRuleNode> {

    @Override
    protected IProduction parseNodeProduction(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.production;
    }

    @Override
    protected Iterable<LayoutSensitiveRuleNode> parseNodeDerivations(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.getDerivations();
    }

}
