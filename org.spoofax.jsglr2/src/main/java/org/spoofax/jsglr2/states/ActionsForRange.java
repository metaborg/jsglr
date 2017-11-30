package org.spoofax.jsglr2.states;

import java.util.Arrays;
import java.util.Iterator;

import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.IReduceLookahead;
import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parser.Parse;

public class ActionsForRange {

    public final IAction[] actions;
    public final int from, to;

    public ActionsForRange(IAction[] actions, int from, int to) {
        this.actions = actions;
        this.from = from;
        this.to = to;
    }

    public Iterable<IAction> getActions() {
        return () -> new Iterator<IAction>() {
            int index = 0;

            @Override public boolean hasNext() {
                return index < actions.length;
            }

            @Override public IAction next() {
                return actions[index++];
            }
        };
    }

    public Iterable<IReduce> getReduceActions(Parse parse) {
        return () -> new Iterator<IReduce>() {
            int index = 0;

            private boolean actionApplies(IAction action) {
                return action.actionType() == ActionType.REDUCE || (action.actionType() == ActionType.REDUCE_LOOKAHEAD
                    && ((IReduceLookahead) action).allowsLookahead(parse));
            }

            @Override public boolean hasNext() {
                while(index < actions.length && !actionApplies(actions[index])) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IReduce next() {
                return (IReduce) actions[index++];
            }
        };
    }

    @Override public String toString() {
        return "{" + ICharacterClass.intToString(from) + "," + ICharacterClass.intToString(to) + "}->"
            + Arrays.toString(actions);
    }

}
