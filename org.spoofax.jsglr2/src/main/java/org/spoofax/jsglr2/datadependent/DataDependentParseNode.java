package org.spoofax.jsglr2.datadependent;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;

public class DataDependentParseNode extends DataDependentParseForest
    implements IBasicParseNode<DataDependentParseForest, DataDependentDerivation> {

    public final IProduction production;
    private final List<DataDependentDerivation> derivations = new ArrayList<>();

    public DataDependentParseNode(IProduction production) {
        this.production = production;
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public List<DataDependentDerivation> getDerivations() {
        return derivations;
    }

}
