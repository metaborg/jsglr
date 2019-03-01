package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.tokenizer.Tokenizer;

public class DataDependentParseForestTokenizer
    extends Tokenizer<BasicParseForest, DataDependentParseNode, DataDependentDerivation> {

    @Override protected IProduction parseNodeProduction(DataDependentParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected Iterable<DataDependentDerivation> parseNodeDerivations(DataDependentParseNode parseNode) {
        return parseNode.getDerivations();
    }

}
