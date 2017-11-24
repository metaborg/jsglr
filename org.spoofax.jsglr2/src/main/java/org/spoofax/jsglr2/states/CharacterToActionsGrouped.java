package org.spoofax.jsglr2.states;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.IReduceLookahead;
import org.spoofax.jsglr2.parser.Parse;

public final class CharacterToActionsGrouped implements ICharacterToActions {

    private final ActionsPerCharacterClass[] actionsPerCharacterClasses;

    public CharacterToActionsGrouped(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        this.actionsPerCharacterClasses = actionsPerCharacterClasses;
    }

    @Override public IAction[] getActions() {
        List<IAction> res = new ArrayList<>();

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            for(IAction action : actionsPerCharacterClass.actions)
                res.add(action);
        }

        return res.toArray(new IAction[res.size()]);
    }

    @Override public Iterable<IAction> getActions(int character) {
        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            if(actionsPerCharacterClass.appliesTo(character))
                return actionsPerCharacterClass.actions;
        }

        return Collections.emptyList();
    }

    @Override public Iterable<IReduce> getReduceActions(Parse parse) {
        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            if(actionsPerCharacterClass.appliesTo(parse.currentChar)) {
                return () -> new Iterator<IReduce>() {
                    int index = 0;

                    @Override public boolean hasNext() {
                        // skip non-applicable actions
                        while(index < actionsPerCharacterClass.actions.size() && !(actionsPerCharacterClass.actions
                            .get(index).actionType() == ActionType.REDUCE
                            || (actionsPerCharacterClass.actions.get(index).actionType() == ActionType.REDUCE_LOOKAHEAD
                                && ((IReduceLookahead) actionsPerCharacterClass.actions.get(index))
                                    .allowsLookahead(parse)))) {
                            index++;
                        }
                        return index < actionsPerCharacterClass.actions.size();
                    }

                    @Override public IReduce next() {
                        if(!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (IReduce) actionsPerCharacterClass.actions.get(index++);
                    }
                };
            }
        }

        return Collections.emptyList();
    }

}
