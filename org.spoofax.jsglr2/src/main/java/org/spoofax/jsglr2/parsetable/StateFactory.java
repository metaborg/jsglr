package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;

public final class StateFactory implements IStateFactory {

    ProductionToGotoRepresentation productionToGotoType;

    public StateFactory() {
        this(ProductionToGotoRepresentation.JavaHashMap);
    }

    public StateFactory(ProductionToGotoRepresentation productionToGotoType) {
        this.productionToGotoType = productionToGotoType;
    }

    public IState from(int stateNumber, IGoto[] gotos, IAction[] actions) {
        IProductionToGoto productionToGoto;

        switch(productionToGotoType) {
            case CapsuleImmutableBinaryRelation:
                productionToGoto = new ProductionToGotoCapsuleBinaryRelationImmutable(gotos);
                break;
            case ForLoop:
                productionToGoto = new ProductionToGotoForLoop(gotos);
                break;
            case JavaHashMap:
                productionToGoto = new ProductionToGotoJavaHashMap(gotos);
                break;
            default:
                productionToGoto = null;
                break;
        }

        return new State(stateNumber, actions, productionToGoto);
    }

}
