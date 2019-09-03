package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;

import com.google.common.collect.Lists;

public class LayoutSensitiveDerivation extends LayoutSensitiveParseForest
    implements IDerivation<LayoutSensitiveParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final LayoutSensitiveParseForest[] parseForests;

    public Position leftPosition, rightPosition;

    // longest-match positions
    // public Set<PositionInterval> longestMatchPos = Sets.newLinkedHashSet();

    public LayoutSensitiveDerivation(AbstractParse<?, ?, ?> parse, Position startPosition, Position leftPosition,
        Position rightPosition, Position endPosition, IProduction production, ProductionType productionType,
        LayoutSensitiveParseForest[] parseForests) {
        super(startPosition, endPosition);

        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;

        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public ProductionType productionType() {
        return productionType;
    }

    @Override public LayoutSensitiveParseForest[] parseForests() {
        return parseForests;
    }

    public List<PositionInterval> getLongestMatchPositions() {
        List<PositionInterval> result = Lists.newArrayList();
        if(production.isLongestMatch()) {
            result.add(new PositionInterval(getStartPosition(), getEndPosition()));
        }
        for(LayoutSensitiveParseForest pf : parseForests) {
            if(pf instanceof LayoutSensitiveDerivation) {
                result.addAll(((LayoutSensitiveDerivation) pf).getLongestMatchPositions());
            } else if(pf instanceof LayoutSensitiveParseNode) {
                List<PositionInterval> positions = ((LayoutSensitiveParseNode) pf).getLongestMatchPositions();

                result.addAll(positions);
            }
        }
        return result;
    }
}
