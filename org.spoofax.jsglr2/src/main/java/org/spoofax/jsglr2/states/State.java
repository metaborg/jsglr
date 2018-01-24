package org.spoofax.jsglr2.states;

import org.metaborg.parsetable.IParseInput;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.parsetable.query.IActionsForCharacter;
import org.metaborg.sdf2table.parsetable.query.IProductionToGoto;

public final class State implements IState {

    private final int stateId;
    private boolean rejectable;

    final IActionsForCharacter actionsForCharacter;
    final IProductionToGoto productionToGoto;

    public State(int stateId, IActionsForCharacter actionsForCharacter, IProductionToGoto productionToGoto) {
        this.stateId = stateId;
        this.actionsForCharacter = actionsForCharacter;
        this.productionToGoto = productionToGoto;
        this.rejectable = false;
    }

    @Override
    public int id() {
        return stateId;
    }

    @Override
    public boolean isRejectable() {
        return rejectable;
    }

    public void markRejectable() {
        this.rejectable = true;
    }

    public IAction[] actions() {
        return actionsForCharacter.getActions();
    }

    @Override
    public Iterable<IAction> getApplicableActions(IParseInput parseInput) {
        return actionsForCharacter.getApplicableActions(parseInput);
    }

    @Override
    public Iterable<IReduce> getApplicableReduceActions(IParseInput parseInput) {
        return actionsForCharacter.getApplicableReduceActions(parseInput);
    }

    public boolean hasGoto(int productionId) {
        return productionToGoto.contains(productionId);
    }

    @Override
    public int getGotoId(int productionId) {
        return productionToGoto.get(productionId);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof State))
            return false;

        State otherState = (State) obj;

        return this.stateId == otherState.stateId;
    }

}
