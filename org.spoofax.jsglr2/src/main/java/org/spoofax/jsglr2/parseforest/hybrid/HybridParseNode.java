package org.spoofax.jsglr2.parseforest.hybrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.util.TreePrettyPrinter;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class HybridParseNode extends HybridParseForest implements IParseNode<HybridParseForest, HybridDerivation> {

    public final IProduction production;
    private final HybridDerivation firstDerivation;
    private List<HybridDerivation> otherDerivations;

    public HybridParseNode(IProduction production, HybridDerivation firstDerivation) {
        this.production = production;
        this.firstDerivation = firstDerivation;
        this.otherDerivations = null;
    }

    public IProduction production() {
        return production;
    }

    public void addDerivation(HybridDerivation derivation) {
        if(otherDerivations == null)
            otherDerivations = new ArrayList<HybridDerivation>();

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
        return firstDerivation;
    }

    public boolean isAmbiguous() {
        return otherDerivations != null;
    }

}
