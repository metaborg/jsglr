package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.IParseInput;

public interface IState {

    static StateFactory factory() {
        return new StateFactory();
    }

    static StateFactory factory(ActionsForCharacterRepresentation actionsForCharacterRepresentation,
        ProductionToGotoRepresentation productionToGotoRepresentation) {
        return new StateFactory(actionsForCharacterRepresentation, productionToGotoRepresentation);
    }

    int id();

    boolean isRejectable();

    Iterable<IAction> getApplicableActions(IParseInput parseInput);

    Iterable<IReduce> getApplicableReduceActions(IParseInput parseInput);

    int getGotoId(int productionId);

}
