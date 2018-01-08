package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.states.IState;

public interface IParseTable {

    public IState getStartState();

    public IState getState(int stateId);

}
