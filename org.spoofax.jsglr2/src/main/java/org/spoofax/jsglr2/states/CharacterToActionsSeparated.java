package org.spoofax.jsglr2.states;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.spoofax.jsglr2.actions.ActionForCharacterClass;
import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;

public final class CharacterToActionsSeparated implements ICharacterToActions {

    private final ActionForCharacterClass[] actions;

    public CharacterToActionsSeparated(ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        List<ActionForCharacterClass> actionPerCharacterClasses = new ArrayList<>();

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            for(IAction action : actionsPerCharacterClass.actions)
                actionPerCharacterClasses
                    .add(new ActionForCharacterClass(actionsPerCharacterClass.characterClass, action));
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
                while(index < actions.length && !actions[index].appliesTo(character)) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IAction next() {
                return actions[index++].action;
            }
        };
    }

    @Override public Iterable<IReduce> getReduceActions(Parse parse) {
        return () -> new Iterator<IReduce>() {
            int index = 0;

            @Override public boolean hasNext() {
                while(index < actions.length && !(actions[index].appliesTo(parse.currentChar)
                    && IReduce.isApplicableReduce(actions[index].action, parse))) {
                    index++;
                }
                return index < actions.length;
            }

            @Override public IReduce next() {
                return (IReduce) actions[index++].action;
            }
        };
    }

}
