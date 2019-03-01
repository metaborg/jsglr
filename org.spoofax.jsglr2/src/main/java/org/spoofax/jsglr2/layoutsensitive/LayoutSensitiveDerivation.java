package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;

import com.google.common.collect.Lists;

public class LayoutSensitiveDerivation extends BasicDerivation implements IDerivation<BasicParseForest> {

    public Position leftPosition, rightPosition;

    // longest-match positions
    // public Set<PositionInterval> longestMatchPos = Sets.newLinkedHashSet();

    public LayoutSensitiveDerivation(AbstractParse<?, ?> parse, Position startPosition, Position leftPosition,
        Position rightPosition, Position endPosition, IProduction production, ProductionType productionType,
        BasicParseForest[] parseForests) {
        super(startPosition, endPosition, production, productionType, parseForests);
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;

        // if(production.isLongestMatch()) {
        // PositionInterval nodePos = new PositionInterval(startPosition, endPosition);
        // longestMatchPos.add(nodePos);
        // }
        //
        // for(BasicParseForest pf : parseForests) {
        // if(pf instanceof LayoutSensitiveParseNode) {
        // if(((LayoutSensitiveParseNode) pf).isAmbiguous()) {
        // System.out.println();
        // }
        // longestMatchPos.addAll(((LayoutSensitiveParseNode) pf).getOnlyDerivation().longestMatchPos);
        // } else if(pf instanceof LayoutSensitiveDerivation) {
        // longestMatchPos.addAll(((LayoutSensitiveDerivation) pf).longestMatchPos);
        // }
        // }

    }

    public List<PositionInterval> getLongestMatchPositions() {
        // System.out.println("getting positions for " + this);
        List<PositionInterval> result = Lists.newArrayList();
        if(production.isLongestMatch()) {
            result.add(new PositionInterval(getStartPosition(), getEndPosition()));
        }
        for(BasicParseForest pf : parseForests) {
            if(pf instanceof LayoutSensitiveDerivation) {
                result.addAll(((LayoutSensitiveDerivation) pf).getLongestMatchPositions());
            } else if(pf instanceof LayoutSensitiveParseNode) {
                List<PositionInterval> positions = ((LayoutSensitiveParseNode) pf).getLongestMatchPositions();

                result.addAll(positions);
            }
        }
        return result;
    }

    // @Override public String toString() {
    //// String buf = "prod(";
    ////
    //// buf += "prod(" + production + ", [";
    ////
    //// int i = 0;
    //// for(BasicParseForest pf : parseForests) {
    //// if(i != 0) {
    //// buf += ", ";
    //// }
    //// if(pf != null) {
    //// buf += pf.toString();
    //// }
    //// i++;
    ////
    //// }
    //// buf += "])";
    //
    // return "";
    // }
}
