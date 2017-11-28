package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacterClass;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class ActionsFactory implements IActionsFactory {

	public IShift getShift(int shiftStateId) {
		return new Shift(shiftStateId);
	}

	@Override
	public IReduce getReduce(IProduction production, ProductionType productionType, int arity) {
		return new Reduce(production, productionType, arity);
	}

	@Override
	public IReduceLookahead getReduceLookahead(IProduction production, ProductionType productionType, int arity,
			ICharacterClass[] followRestriction) {
		return new ReduceLookahead(production, productionType, arity, followRestriction);
	}

	@Override
	public IAccept getAccept() {
		return Accept.SINGLETON;
	}
	
}
