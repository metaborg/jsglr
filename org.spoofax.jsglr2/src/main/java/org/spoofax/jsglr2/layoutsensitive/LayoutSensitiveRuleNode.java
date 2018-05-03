package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.RuleNode;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

import com.google.common.collect.Lists;

public class LayoutSensitiveRuleNode extends RuleNode implements IDerivation<BasicParseForest> {

    public Position leftPosition, rightPosition;
    
    // longest-match positions
    public List<Position[]> longestMatchPos = Lists.newArrayList();

    public LayoutSensitiveRuleNode(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position leftPosition,
        Position rightPosition, Position endPosition, IProduction production, ProductionType productionType,
        BasicParseForest[] parseForests) {
        super(nodeNumber, parse, startPosition, endPosition, production, productionType, parseForests);
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        
        if(production.isLongestMatch()) {
            Position[] nodePos = { startPosition, endPosition };
            longestMatchPos.add(nodePos);
        }
        
        for(BasicParseForest pf : parseForests) {
            if(pf instanceof LayoutSensitiveSymbolNode) {
                longestMatchPos.addAll(((LayoutSensitiveSymbolNode) pf).getOnlyDerivation().longestMatchPos);
            } else if(pf instanceof LayoutSensitiveRuleNode) {
                longestMatchPos.addAll(((LayoutSensitiveRuleNode) pf).longestMatchPos);
            }
        }
    }

    @Override public String toString() {
        String buf = "prod(";
             
        buf += "prod(" + production +  ", [";
        
        int i = 0;
        for(BasicParseForest pf : parseForests) {
            if(i != 0) {
                buf += ", ";
            }
            if(pf != null) {
                buf += pf.toString();
            } 
            i++;
            
        }
        buf += "])";

        return buf;
    }

}
