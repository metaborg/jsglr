package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;

import java.util.ArrayList;
import java.util.List;

public class DataDependentParseNode
//@formatter:off
   <ParseForest extends IDataDependentParseForest,
    Derivation  extends IDataDependentDerivation<ParseForest>>
//@formatter:on
    implements IDataDependentParseNode<ParseForest, Derivation> {

    public final IProduction production;
    private final List<Derivation> derivations = new ArrayList<>();

    public DataDependentParseNode(IProduction production) {
        this.production = production;
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public List<Derivation> getDerivations() {
        return derivations;
    }

}
