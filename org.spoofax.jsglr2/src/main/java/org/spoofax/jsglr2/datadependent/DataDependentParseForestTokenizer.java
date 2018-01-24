package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.tokenizer.Tokenizer;

public class DataDependentParseForestTokenizer extends Tokenizer<BasicParseForest, DataDependentSymbolNode, DataDependentRuleNode> {

    @Override
    protected IProduction parseNodeProduction(DataDependentSymbolNode symbolNode) {
        return symbolNode.production;
    }

    @Override
    protected Iterable<DataDependentRuleNode> parseNodeDerivations(DataDependentSymbolNode symbolNode) {
        return symbolNode.getDerivations();
    }

}
