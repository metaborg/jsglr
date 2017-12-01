package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.IGoto;

public class ProductionToGotoForLoop implements IProductionToGoto {

    private final IGoto[] gotos;

    public ProductionToGotoForLoop(IGoto[] gotos) {
        this.gotos = gotos;
    }

    @Override public boolean contains(int productionId) {
        for(IGoto gotoAction : gotos) {
            for(int gotoActionProductionId : gotoAction.productionIds())
                if(gotoActionProductionId == productionId)
                    return true;
        }

        return false;
    }

    @Override public int get(int productionId) {
        for(IGoto gotoAction : gotos) {
            for(int gotoActionProductionId : gotoAction.productionIds())
                if(gotoActionProductionId == productionId)
                    return gotoAction.gotoStateId();
        }

        throw new IllegalStateException();
    }

}
