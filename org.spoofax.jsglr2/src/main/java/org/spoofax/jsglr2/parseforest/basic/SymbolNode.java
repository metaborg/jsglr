package org.spoofax.jsglr2.parseforest.basic;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class SymbolNode extends BasicParseForest implements IBasicSymbolNode<BasicParseForest, RuleNode> {

    public final IProduction production;
    private final List<RuleNode> derivations = new ArrayList<>();

    public SymbolNode(Position startPosition, Position endPosition, IProduction production) {
        super(startPosition, endPosition);
        this.production = production;
    }

    @Override
    public String descriptor() {
        return production.descriptor();
    }

    @Override
    public List<RuleNode> getDerivations() {
        return derivations;
    }
}
