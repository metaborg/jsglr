package org.spoofax.jsglr2.datadependent;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.IBasicParseNode;
import org.spoofax.jsglr2.parser.Position;

public class DataDependentParseNode extends BasicParseForest
    implements IBasicParseNode<BasicParseForest, DataDependentDerivation> {

    public final IProduction production;
    private final List<DataDependentDerivation> derivations = new ArrayList<>();

    public DataDependentParseNode(Position startPosition, Position endPosition, IProduction production) {
        super(startPosition, endPosition);
        this.production = production;
    }

    @Override public String descriptor() {
        return production.descriptor();
    }

    @Override public List<DataDependentDerivation> getDerivations() {
        return derivations;
    }
}
