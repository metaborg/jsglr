package org.spoofax.jsglr2.states;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.util.Range;

class MultipleActionGroupsForRange extends ActionsForRange {

    private final ActionsPerCharacterClass[] actionsPerCharacterClasses;

    public MultipleActionGroupsForRange(Range range, ActionsPerCharacterClass[] actionsPerCharacterClasses) {
        super(range);
        this.actionsPerCharacterClasses = actionsPerCharacterClasses;
    }

    public List<IAction> getActions() {
        List<IAction> res = new ArrayList<>();

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses)
            res.addAll(actionsPerCharacterClass.actions);

        return res;
    }

    public Iterable<IAction> getActions(int character) {
        if(!range.contains(character))
            return Collections.EMPTY_LIST;

        Set<IAction> actions = new HashSet<>();

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            if(actionsPerCharacterClass.appliesTo(character))
                actions.addAll(actionsPerCharacterClass.actions);
        }

        return actions;

        // return () -> new Iterator<IAction>() {
        // int characterClassIndex = 0, actionInCharacterClassIndex = 0;
        //
        // @Override public boolean hasNext() {
        // while(characterClassIndex < actionsPerCharacterClasses.length
        // && actionInCharacterClassIndex < actionsPerCharacterClasses[characterClassIndex].actions.size()) {
        // actionInCharacterClassIndex++;
        //
        // if(actionInCharacterClassIndex >= actionsPerCharacterClasses[characterClassIndex].actions.size()) {
        // characterClassIndex++;
        // actionInCharacterClassIndex = 0;
        // }
        // }
        //
        // return characterClassIndex < actionsPerCharacterClasses.length
        // && actionInCharacterClassIndex < actionsPerCharacterClasses[characterClassIndex].actions.size();
        // }
        //
        // @Override public IAction next() {
        // if(!hasNext()) {
        // throw new NoSuchElementException();
        // }
        //
        // int nextActionInCharacterClassIndex = actionInCharacterClassIndex++;
        //
        // if(actionInCharacterClassIndex >= actionsPerCharacterClasses[characterClassIndex].actions.size()) {
        // characterClassIndex++;
        // actionInCharacterClassIndex = 0;
        // }
        //
        // return actionsPerCharacterClasses[characterClassIndex].actions.get(nextActionInCharacterClassIndex);
        // }
        // };
    }

    public Iterable<IReduce> getReduceActions(Parse parse) {
        if(!range.contains(parse.currentChar))
            return Collections.EMPTY_LIST;

        Set<IReduce> reduces = new HashSet<>();

        for(ActionsPerCharacterClass actionsPerCharacterClass : actionsPerCharacterClasses) {
            if(actionsPerCharacterClass.appliesTo(parse.currentChar))
                reduces.addAll(actionsPerCharacterClass.getReduceActions(parse));
        }

        return reduces;

        // return () -> new Iterator<IReduce>() {
        // int characterClassIndex = 0, actionInCharacterClassIndex = 0;
        //
        // private boolean actionApplies(IAction action) {
        // return action.actionType() == ActionType.REDUCE || (action.actionType() == ActionType.REDUCE_LOOKAHEAD
        // && ((IReduceLookahead) action).allowsLookahead(parse));
        // }
        //
        // @Override public boolean hasNext() {
        // while(characterClassIndex < actionsPerCharacterClasses.length
        // && actionInCharacterClassIndex < actionsPerCharacterClasses[characterClassIndex].actions.size()
        // && actionApplies(
        // actionsPerCharacterClasses[characterClassIndex].actions.get(actionInCharacterClassIndex))) {
        // actionInCharacterClassIndex++;
        //
        // if(actionInCharacterClassIndex >= actionsPerCharacterClasses[characterClassIndex].actions.size()) {
        // characterClassIndex++;
        // actionInCharacterClassIndex = 0;
        // }
        // }
        //
        // return characterClassIndex < actionsPerCharacterClasses.length
        // && actionInCharacterClassIndex < actionsPerCharacterClasses[characterClassIndex].actions.size();
        // }
        //
        // @Override public IReduce next() {
        // if(!hasNext()) {
        // throw new NoSuchElementException();
        // }
        //
        // int nextActionInCharacterClassIndex = actionInCharacterClassIndex++;
        //
        // if(actionInCharacterClassIndex >= actionsPerCharacterClasses[characterClassIndex].actions.size()) {
        // characterClassIndex++;
        // actionInCharacterClassIndex = 0;
        // }
        //
        // return (IReduce) actionsPerCharacterClasses[characterClassIndex].actions
        // .get(nextActionInCharacterClassIndex);
        // }
        // };
    }

}