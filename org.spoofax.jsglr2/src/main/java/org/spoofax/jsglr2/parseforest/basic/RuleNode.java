package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class RuleNode extends BasicParseForest implements IDerivation<BasicParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final BasicParseForest[] parseForests;

    public RuleNode(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position endPosition,
        IProduction production, ProductionType productionType, BasicParseForest[] parseForests) {
        super(nodeNumber, parse, startPosition, endPosition);
        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;
    }

    @Override
    public String descriptor() {
        return production.descriptor();
    }

    @Override
    public IProduction production() {
        return production;
    }

    @Override
    public BasicParseForest[] parseForests() {
        return parseForests;
    }

}
