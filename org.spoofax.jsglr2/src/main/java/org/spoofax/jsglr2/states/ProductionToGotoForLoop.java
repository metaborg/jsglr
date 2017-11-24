package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.IGoto;

public class ProductionToGotoForLoop implements IProductionToGoto {

    private final IGoto[] gotos;

    public ProductionToGotoForLoop(IGoto[] gotos) {
        this.gotos = gotos;
    }

    @Override public boolean contains(int production) {
        for(IGoto gotoAction : gotos) {
            for(int productionId : gotoAction.productions())
                if(productionId == production)
                    return true;
        }

        return false;
    }

    @Override public int get(int production) {
        for(IGoto gotoAction : gotos) {
            for(int productionId : gotoAction.productions())
                if(productionId == production)
                    return gotoAction.gotoState();
        }

        throw new IllegalStateException();
    }

}
