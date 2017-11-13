package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;

public final class StateFactory implements IStateFactory {

    public IState from(int stateNumber, IGoto[] gotos, IAction[] actions) {
        return new State(stateNumber, actions, new ProductionToGoto(gotos));
    }

}
