package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;

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

    Iterable<IAction> getActions(int character);

    Iterable<IReduce> getReduceActions(Parse<?, ?> parse);

    int getGotoId(int productionId);

}
