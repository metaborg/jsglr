package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;

public final class State implements IState {

    private final int stateNumber;
    private boolean rejectable;

    final ICharacterToActions characterToActions;
    final IProductionToGoto productionToGoto;

    public State(int stateNumber, ICharacterToActions characterToActions, IProductionToGoto productionToGoto) {
        this.stateNumber = stateNumber;
        this.characterToActions = characterToActions;
        this.productionToGoto = productionToGoto;
        this.rejectable = false;
    }

    @Override public int stateNumber() {
        return stateNumber;
    }

    @Override public boolean isRejectable() {
        return rejectable;
    }

    public void markRejectable() {
        this.rejectable = true;
    }

    public IAction[] actions() {
        return characterToActions.getActions();
    }

    @Override public Iterable<IAction> getActions(int character) {
        return characterToActions.getActions(character);
    }

    @Override public Iterable<IReduce> getReduceActions(Parse parse) {
        return characterToActions.getReduceActions(parse);
    }

    public boolean hasGoto(int productionId) {
        return productionToGoto.contains(productionId);
    }

    @Override public int getGotoId(int productionId) {
        return productionToGoto.get(productionId);
    }

    @Override public boolean equals(Object obj) {
        if(!(obj instanceof State))
            return false;

        State otherState = (State) obj;

        return this.stateNumber == otherState.stateNumber;
    }

}
