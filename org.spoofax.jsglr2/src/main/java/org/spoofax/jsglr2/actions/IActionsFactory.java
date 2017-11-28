package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacterClass;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public interface IActionsFactory {
	
	IShift getShift(int shiftStateId);
	
	IReduce getReduce(IProduction production, ProductionType productionType, int arity);
	
	IReduceLookahead getReduceLookahead(IProduction production, ProductionType productionType, int arity, ICharacterClass[] followRestriction);
	
	IAccept getAccept();

}
