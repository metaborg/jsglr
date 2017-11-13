package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;

public final class StateFactory implements IStateFactory {

    ProductionToGotoType productionToGotoType;

    public StateFactory() {
        this(ProductionToGotoType.CapsuleBinaryRelation);
    }

    public StateFactory(ProductionToGotoType productionToGotoType) {
        this.productionToGotoType = productionToGotoType;
    }

    public IState from(int stateNumber, IGoto[] gotos, IAction[] actions) {
        IProductionToGoto productionToGoto;

        switch(productionToGotoType) {
            case CapsuleBinaryRelation:
                productionToGoto = new ProductionToGotoCapsuleBinaryRelationImmutable(gotos);
                break;
            case ForLoop:
                productionToGoto = new ProductionToGotoForLoop(gotos);
                break;
            case JavaMap:
                productionToGoto = new ProductionToGotoJavaMap(gotos);
                break;
            default:
                productionToGoto = null;
                break;
        }

        return new State(stateNumber, actions, productionToGoto);
    }

}
