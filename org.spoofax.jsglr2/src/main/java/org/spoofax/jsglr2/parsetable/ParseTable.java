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

    @Override
    public IProduction[] productions() {
        return productions;
    }

    @Override
    public IState startState() {
        return states[startStateNumber];
    }

    @Override
    public IState getState(int stateNumber) {
        return states[stateNumber];
    }

}
