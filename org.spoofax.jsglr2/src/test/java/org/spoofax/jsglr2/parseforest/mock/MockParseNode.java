package org.spoofax.jsglr2.parseforest.mock;

import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;

public class MockParseNode extends MockParseForest implements IBasicParseNode<MockParseForest, MockDerivation> {

    public final String label;
    private final int width;
    private final List<MockDerivation> derivations;

    public MockParseNode(String label, int width, List<MockDerivation> derivations) {
        this.label = label;
        this.width = width;
        this.derivations = derivations;
    }

    @Override public int width() {
        return width;
    }

    @Override public IProduction production() {
        return null;
    }

    @Override public List<MockDerivation> getDerivations() {
        return derivations;
    }

    @Override public void disambiguate(MockDerivation derivation) {
        derivations.removeIf(alternative -> alternative != derivation);
    }

    @Override public String toString() {
        return label;
    }

}
