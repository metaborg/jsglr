package org.spoofax.jsglr2.parsetable;

public interface IParseTable {

    public IProduction[] productions();

    public IState startState();

    public IState getState(int stateNumber);

}
