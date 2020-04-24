package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;

public class BasicParseNode
//@formatter:off
   <ParseForest extends IBasicParseForest,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    implements IBasicParseNode<ParseForest, Derivation> {

    private final int width;
    private final IProduction production;
    protected final List<Derivation> derivations = new ArrayList<>();

    public BasicParseNode(int width, IProduction production) {
        this.width = width;
        this.production = production;
    }

    @Override public int width() {
        return width;
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public List<Derivation> getDerivations() {
        return derivations;
    }

}
