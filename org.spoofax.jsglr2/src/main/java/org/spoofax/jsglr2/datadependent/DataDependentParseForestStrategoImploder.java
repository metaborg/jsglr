package org.spoofax.jsglr2.datadependent;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;

public class DataDependentParseForestStrategoImploder
    extends StrategoTermImploder<DataDependentParseForest, DataDependentParseNode, DataDependentDerivation> {

    public DataDependentParseForestStrategoImploder() {
        super();
    }

    @Override protected IProduction parseNodeProduction(DataDependentParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected DataDependentDerivation parseNodeOnlyDerivation(DataDependentParseNode parseNode) {
        return parseNode.getFirstDerivation();
    }

    @Override protected List<DataDependentDerivation>
        parseNodePreferredAvoidedDerivations(DataDependentParseNode parseNode) {
        return parseNode.getPreferredAvoidedDerivations();
    }

    @Override protected List<DataDependentDerivation>
        longestMatchedDerivations(List<DataDependentDerivation> derivations) {
        // TODO remove derivations according to longest match criteria
        return derivations;
    }

}
