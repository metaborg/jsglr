package org.spoofax.jsglr2.actions;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.metaborg.parsetable.actions.IAccept;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.actions.IReduceLookahead;
import org.metaborg.parsetable.actions.IShift;
import org.metaborg.parsetable.characterclasses.ICharacterClass;

public interface IActionsFactory {

    IShift getShift(int shiftStateId);

    IReduce getReduce(IProduction production, ProductionType productionType, int arity);

    IReduceLookahead getReduceLookahead(IProduction production, ProductionType productionType, int arity,
        ICharacterClass[] followRestriction);

    IAccept getAccept();

}
