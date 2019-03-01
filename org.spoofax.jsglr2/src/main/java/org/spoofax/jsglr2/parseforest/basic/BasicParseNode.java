package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parser.Position;

public class BasicParseNode extends BasicParseForest implements IBasicParseNode<BasicParseForest, BasicDerivation> {

    public final IProduction production;
    private final List<BasicDerivation> derivations = new ArrayList<>();

    public BasicParseNode(Position startPosition, Position endPosition, IProduction production) {
        super(startPosition, endPosition);
        this.production = production;
    }

    @Override public String descriptor() {
        return production.descriptor();
    }

    @Override public List<BasicDerivation> getDerivations() {
        return derivations;
    }
}
