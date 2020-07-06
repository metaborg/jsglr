package org.spoofax.jsglr2.parseforest.mock;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;

public class MockDerivation implements IDerivation<MockParseForest> {

    public final MockParseForest[] parseForests;

    public MockDerivation(MockParseForest[] parseForests) {
        this.parseForests = parseForests;
    }

    @Override public IProduction production() {
        return null;
    }

    @Override public ProductionType productionType() {
        return null;
    }

    @Override public MockParseForest[] parseForests() {
        return parseForests;
    }

}
