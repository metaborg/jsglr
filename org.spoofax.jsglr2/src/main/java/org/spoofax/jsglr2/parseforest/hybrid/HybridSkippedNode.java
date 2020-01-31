package org.spoofax.jsglr2.parseforest.hybrid;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import java.util.Collections;

import org.metaborg.parsetable.productions.IProduction;

public class HybridSkippedNode extends HybridParseNode {

    private final int width;

    public HybridSkippedNode(IProduction production, HybridParseForest[] parseForests) {
        super(production, null);
        this.width = sumWidth(parseForests);
    }

    @Override public int width() {
        return width;
    }

    // The following four methods are copied from IBasicSkippedNode
    // Extracting an interface with default methods does not work,
    // because the definitions in the direct superclass take precedence
    @Override public void addDerivation(HybridDerivation derivation) {
    }

    @Override public Iterable<HybridDerivation> getDerivations() {
        return Collections.emptyList();
    }

    @Override public HybridDerivation getFirstDerivation() {
        throw new UnsupportedOperationException("Cannot get derivation of skipped parse node");
    }

    @Override public boolean isAmbiguous() {
        return false;
    }

}
