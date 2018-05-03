package org.spoofax.jsglr2.datadependent;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;

public class DataDependentParseForestStrategoImploder
    extends StrategoTermImploder<BasicParseForest, DataDependentSymbolNode, DataDependentRuleNode> {

    public DataDependentParseForestStrategoImploder() {
        super(new DataDependentParseForestTokenizer());
    }

    @Override protected IProduction parseNodeProduction(DataDependentSymbolNode symbolNode) {
        return symbolNode.production;
    }

    @Override protected DataDependentRuleNode parseNodeOnlyDerivation(DataDependentSymbolNode symbolNode) {
        return symbolNode.getOnlyDerivation();
    }

    @Override protected List<DataDependentRuleNode>
        parseNodePreferredAvoidedDerivations(DataDependentSymbolNode symbolNode) {
        return symbolNode.getPreferredAvoidedDerivations();
    }

    @Override protected List<DataDependentRuleNode> longestMatchedDerivations(List<DataDependentRuleNode> derivations) {
        // TODO remove derivations according to longest match criteria
        return derivations;
    }

}
