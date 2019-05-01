package org.spoofax.jsglr2.states;

import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IGoto;
import org.metaborg.sdf2table.parsetable.query.*;

public class StateFactory implements IStateFactory {

    private final ActionsForCharacterRepresentation actionsForCharacterRepresentation;
    private final ProductionToGotoRepresentation productionToGotoRepresentation;

    public static ActionsForCharacterRepresentation defaultActionsForCharacterRepresentation =
        ActionsForCharacterRepresentation.DisjointSorted;
    public static ProductionToGotoRepresentation defaultProductionToGotoRepresentation =
        ProductionToGotoRepresentation.JavaHashMap;

    public StateFactory() {
        this(defaultActionsForCharacterRepresentation, defaultProductionToGotoRepresentation);
    }

    public StateFactory(ActionsForCharacterRepresentation actionsForCharacterRepresentation,
        ProductionToGotoRepresentation productionToGotoType) {
        this.actionsForCharacterRepresentation = actionsForCharacterRepresentation;
        this.productionToGotoRepresentation = productionToGotoType;
    }

    @Override public IState from(int stateId, IGoto[] gotos, ActionsPerCharacterClass[] actionsPerCharacterClass) {
        IActionsForCharacter actionsForCharacter;
        IProductionToGoto productionToGoto;

        switch(actionsForCharacterRepresentation) {
            case DisjointSorted:
                actionsForCharacter = new ActionsForCharacterDisjointSorted(actionsPerCharacterClass);
                break;
            case Separated:
                actionsForCharacter = new ActionsForCharacterSeparated(actionsPerCharacterClass);
                break;
            default:
                actionsForCharacter = null;
                break;
        }

        switch(productionToGotoRepresentation) {
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

        return new State(stateId, actionsForCharacter, productionToGoto);
    }

}
