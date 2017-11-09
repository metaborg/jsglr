package org.spoofax.jsglr2.parsetable;

public class ParseTable implements IParseTable {

    final private IProduction[] productions;
    final private IState[] states;
    final private int startStateNumber;

    public ParseTable(IProduction[] productions, IState[] states, int startStateNumber) {
        this.productions = productions;
        this.states = states;
        this.startStateNumber = startStateNumber;
    }

    public IProduction[] productions() {
        return productions;
    }

    public IState startState() {
        return states[startStateNumber];
    }

    public IState getState(int stateNumber) {
        return states[stateNumber];
    }

}
