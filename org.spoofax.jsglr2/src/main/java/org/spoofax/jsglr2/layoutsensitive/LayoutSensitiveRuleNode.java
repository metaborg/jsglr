package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.RuleNode;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.PositionInterval;

import com.google.common.collect.Lists;

public class LayoutSensitiveRuleNode extends RuleNode implements IDerivation<BasicParseForest> {

    public Position leftPosition, rightPosition;

    // longest-match positions
    // public Set<PositionInterval> longestMatchPos = Sets.newLinkedHashSet();

    public LayoutSensitiveRuleNode(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position leftPosition,
        Position rightPosition, Position endPosition, IProduction production, ProductionType productionType,
        BasicParseForest[] parseForests) {
        super(nodeNumber, parse, startPosition, endPosition, production, productionType, parseForests);
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;

        // if(production.isLongestMatch()) {
        // PositionInterval nodePos = new PositionInterval(startPosition, endPosition);
        // longestMatchPos.add(nodePos);
        // }
        //
        // for(BasicParseForest pf : parseForests) {
        // if(pf instanceof LayoutSensitiveSymbolNode) {
        // if(((LayoutSensitiveSymbolNode) pf).isAmbiguous()) {
        // System.out.println();
        // }
        // longestMatchPos.addAll(((LayoutSensitiveSymbolNode) pf).getOnlyDerivation().longestMatchPos);
        // } else if(pf instanceof LayoutSensitiveRuleNode) {
        // longestMatchPos.addAll(((LayoutSensitiveRuleNode) pf).longestMatchPos);
        // }
        // }

    }

    public List<PositionInterval> getLongestMatchPositions() {
//        System.out.println("getting positions for " + this);
        List<PositionInterval> result = Lists.newArrayList();
        if(production.isLongestMatch()) {
            result.add(new PositionInterval(startPosition, endPosition));
        }
        for(BasicParseForest pf : parseForests) {
            if(pf instanceof LayoutSensitiveRuleNode) {
                result.addAll(((LayoutSensitiveRuleNode) pf).getLongestMatchPositions());
            } else if(pf instanceof LayoutSensitiveSymbolNode) {
                List<PositionInterval> positions = ((LayoutSensitiveSymbolNode) pf).getLongestMatchPositions();
                
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
