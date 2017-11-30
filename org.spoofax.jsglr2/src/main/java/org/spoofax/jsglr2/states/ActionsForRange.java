package org.spoofax.jsglr2.states;

import java.util.List;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.util.Range;

abstract class ActionsForRange {

    public final Range range;

    protected ActionsForRange(Range range) {
        this.range = range;
    }

    abstract public List<IAction> getActions();

    abstract public Iterable<IAction> getActions(int character);

    abstract public Iterable<IReduce> getReduceActions(Parse parse);

}