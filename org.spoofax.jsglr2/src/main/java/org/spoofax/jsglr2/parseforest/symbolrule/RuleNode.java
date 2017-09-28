package org.spoofax.jsglr2.parseforest.symbolrule;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class RuleNode extends SRParseForest implements IDerivation<SRParseForest> {
	
	public final IProduction production;
    public final ProductionType productionType;
	public final SRParseForest[] parseForests;
	
	public RuleNode(int nodeNumber, Parse parse, Position startPosition, Position endPosition, IProduction production, ProductionType productionType, SRParseForest[] parseForests) {
		super(nodeNumber, parse, startPosition, endPosition);
		this.production = production;
        this.productionType = productionType;
		this.parseForests = parseForests;
	}
	
	public String descriptor() {
		return production.descriptor();
	}

    public IProduction production() {
        return production;
    }
    
    public SRParseForest[] parseForests() {
        return parseForests;
    }

}
