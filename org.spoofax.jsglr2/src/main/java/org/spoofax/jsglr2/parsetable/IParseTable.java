package org.spoofax.jsglr2.parsetable;

public interface IParseTable {

    public IState startState();

    public IState getState(int stateNumber);

}
