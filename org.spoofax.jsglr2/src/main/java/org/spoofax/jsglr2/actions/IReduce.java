package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public interface IReduce extends IAction {

    @Override
    default ActionType actionType() {
        return ActionType.REDUCE;
    }

    IProduction production();

    ProductionType productionType();

    int arity();

    default boolean isRejectProduction() {
        return productionType() == ProductionType.REJECT;
    }

}
