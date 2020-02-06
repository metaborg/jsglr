package org.spoofax.jsglr2.parseforest.hybrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridParseNode extends HybridParseForest implements IParseNode<HybridParseForest, HybridDerivation> {

    private final int width;
    private final IProduction production;
    private final HybridDerivation firstDerivation;
    private List<HybridDerivation> otherDerivations;

    public HybridParseNode(int width, IProduction production, HybridDerivation firstDerivation) {
        this.width = width;
        this.production = production;
        this.firstDerivation = firstDerivation;
        this.otherDerivations = null;
    }

    public HybridParseNode(int width, IProduction production) {
        this(width, production, null);
    }

    @Override public int width() {
        return width;
    }

    public IProduction production() {
        return production;
    }

    public void addDerivation(HybridDerivation derivation) {
        if(otherDerivations == null)
            otherDerivations = new ArrayList<>();

        otherDerivations.add(derivation);
    }

    public Iterable<HybridDerivation> getDerivations() {
        if(otherDerivations == null) {
            return Collections.singleton(firstDerivation);
        } else {
            return SingleElementWithListIterable.of(firstDerivation, otherDerivations);
        }
    }

    public HybridDerivation getFirstDerivation() {
        if(firstDerivation == null)
            throw new UnsupportedOperationException("Cannot get derivation of skipped parse node");

        return firstDerivation;
    }

    public boolean isAmbiguous() {
        return otherDerivations != null;
    }

}
