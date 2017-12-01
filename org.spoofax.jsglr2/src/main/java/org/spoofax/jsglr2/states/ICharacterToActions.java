package org.spoofax.jsglr2.states;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;

public interface ICharacterToActions {

    /*
     * Returns all actions. Only used during parse table loading for marking rejectable states, thus the implementation
     * of this method does not influence parsing performance.
     */
    IAction[] getActions();

    /*
     * Returns actions applicable to the given character.
     */
    Iterable<IAction> getActions(int character);

    /*
     * Returns reduce actions (possibly with lookahead) applicable to the given character.
     */
    Iterable<IReduce> getReduceActions(Parse<?, ?> parse);

}
