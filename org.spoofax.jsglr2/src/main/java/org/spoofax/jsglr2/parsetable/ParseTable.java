package org.spoofax.jsglr2.parsetable;

public class ParseTable implements IParseTable {

    final private IState[] states;
    final private int startStateNumber;

    public ParseTable(IState[] states, int startStateNumber) {
        this.states = states;
        this.startStateNumber = startStateNumber;
    }

    @Override public IState startState() {
        return states[startStateNumber];
    }

    @Override public IState getState(int stateNumber) {
        return states[stateNumber];
    }

}
