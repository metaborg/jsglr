package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.RuleNode;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveRuleNode extends RuleNode implements IDerivation<BasicParseForest> {

    public Position leftPosition, rightPosition;

    public LayoutSensitiveRuleNode(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position leftPosition,
        Position rightPosition, Position endPosition, IProduction production, ProductionType productionType,
        BasicParseForest[] parseForests) {
        super(nodeNumber, parse, startPosition, endPosition, production, productionType, parseForests);
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
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
