package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IGoto;

public class StateFactory implements IStateFactory {

    private final ActionsPerCharacterClassRepresentation actionsPerCharacterClassRepresentation;
    private final ProductionToGotoRepresentation productionToGotoRepresentation;

    public static ActionsPerCharacterClassRepresentation defaultActionsPerCharacterClassRepresentation =
        ActionsPerCharacterClassRepresentation.DisjointSorted;
    public static ProductionToGotoRepresentation defaultProductionToGotoRepresentation =
        ProductionToGotoRepresentation.JavaHashMap;

    public StateFactory() {
        this(defaultActionsPerCharacterClassRepresentation, defaultProductionToGotoRepresentation);
    }

    public StateFactory(ActionsPerCharacterClassRepresentation actionsPerCharacterClassRepresentation,
        ProductionToGotoRepresentation productionToGotoType) {
        this.actionsPerCharacterClassRepresentation = actionsPerCharacterClassRepresentation;
        this.productionToGotoRepresentation = productionToGotoType;
    }

    public IState from(int stateId, IGoto[] gotos, ActionsPerCharacterClass[] actionsPerCharacterClass) {
        ICharacterToActions characterToActions;
        IProductionToGoto productionToGoto;

        switch(actionsPerCharacterClassRepresentation) {
            case DisjointSorted:
                characterToActions = new CharacterToActionsDisjointSorted(actionsPerCharacterClass);
                break;
            case Separated:
                characterToActions = new CharacterToActionsSeparated(actionsPerCharacterClass);
                break;
            default:
                characterToActions = null;
                break;
        }

        switch(productionToGotoRepresentation) {
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

        return new State(stateId, characterToActions, productionToGoto);
    }

}
