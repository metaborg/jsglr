package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;

public class BasicParseNode extends BasicParseForest implements IBasicParseNode<BasicParseForest, BasicDerivation> {

    public final IProduction production;
    private final List<BasicDerivation> derivations = new ArrayList<>();

    public BasicParseNode(IProduction production) {
        this.production = production;
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public List<BasicDerivation> getDerivations() {
        return derivations;
    }

}
