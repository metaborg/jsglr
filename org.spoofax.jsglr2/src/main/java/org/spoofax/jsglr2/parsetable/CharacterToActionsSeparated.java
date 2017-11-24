package org.spoofax.jsglr2.parsetable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.spoofax.jsglr2.actions.ActionForCharacterClass;
import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.IReduceLookahead;
import org.spoofax.jsglr2.parser.Parse;

public final class CharacterToActionsSeparated implements ICharacterToActions {

    private final ActionForCharacterClass[] actions;

    public CharacterToActionsSeparated(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        List<ActionForCharacterClass> actionPerCharacterClasses = new ArrayList<>();

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            for(IAction action : actionsPerCharacterClass.actions)
                actionPerCharacterClasses.add(new ActionForCharacterClass(actionsPerCharacterClass.characters, action));
        }

        actions = new ActionForCharacterClass[actionPerCharacterClasses.size()];

        actionPerCharacterClasses.toArray(actions);
    }

    @Override public IAction[] getActions() {
        IAction[] res = new IAction[actions.length];

        for(int i = 0; i < actions.length; i++)
            res[i] = actions[i].action;

        return res;
    }

    @Override public Iterable<IAction> getActions(int character) {
        return () -> new Iterator<IAction>() {
            int index = 0;

            @Override public boolean hasNext() {
                // skip non-applicable actions
                while(index < actions.length && !actions[index].appliesTo(character)) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IAction next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return actions[index++].action;
            }
        };
    }

    @Override public Iterable<IReduce> getReduceActions(Parse parse) {
        return () -> new Iterator<IReduce>() {
            int index = 0;

            @Override public boolean hasNext() {
                // skip non-applicable actions
                while(index < actions.length && !(actions[index].appliesTo(parse.currentChar)
                    && (actions[index].action.actionType() == ActionType.REDUCE
                        || (actions[index].action.actionType() == ActionType.REDUCE_LOOKAHEAD
                            && ((IReduceLookahead) actions[index].action).allowsLookahead(parse))))) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IReduce next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (IReduce) actions[index++].action;
            }
        };
    }

}
