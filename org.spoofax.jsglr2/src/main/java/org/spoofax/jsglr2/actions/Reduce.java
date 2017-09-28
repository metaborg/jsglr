package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class Reduce extends Action implements IReduce {

	private final IProduction production;
    private final ProductionType productionType;
	private final int arity;
	
	public Reduce(ICharacters characters, IProduction production, ProductionType productionType, int arity) {
	    super(characters);

        this.production = production;
        this.productionType = productionType;
        this.arity = arity;
	}
	
	public IProduction production() {
	    return production;
	}
	
	public ProductionType productionType() {
        return productionType;
    }
    
    public int arity() {
        return arity;
    }
	
}
