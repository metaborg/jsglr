package org.spoofax.jsglr2.states;

import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.actions.ActionsPerCharacterClass;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.util.Range;

class SingleActionGroupForRange extends ActionsForRange {

    private final ActionsPerCharacterClass actionsPerCharacterClass;

    public SingleActionGroupForRange(Range range, ActionsPerCharacterClass actionsPerCharacterClass) {
        super(range);
        this.actionsPerCharacterClass = actionsPerCharacterClass;
    }

    public List<IAction> getActions() {
        return actionsPerCharacterClass.actions;
    }

    public Iterable<IAction> getActions(int character) {
        if(range.contains(character) && actionsPerCharacterClass.characterClass.contains(character))
            return actionsPerCharacterClass.actions;
        else
            return Collections.EMPTY_LIST;
    }

    public Iterable<IReduce> getReduceActions(Parse parse) {
        if(range.contains(parse.currentChar) && actionsPerCharacterClass.characterClass.contains(parse.currentChar)) {
            return actionsPerCharacterClass.getReduceActions(parse);

            // return () -> new Iterator<IReduce>() {
            // int index = 0;
            //
            // private boolean actionApplies(IAction action) {
            // return action.actionType() == ActionType.REDUCE
            // || (action.actionType() == ActionType.REDUCE_LOOKAHEAD
            // && ((IReduceLookahead) action).allowsLookahead(parse));
            // }
            //
            // @Override public boolean hasNext() {
            // // skip non-applicable actions
            // while(index < actionsPerCharacterClass.actions.size()
            // && !actionApplies(actionsPerCharacterClass.actions.get(index))) {
            // index++;
            // }
            // return index < actionsPerCharacterClass.actions.size();
            // }
            //
            // @Override public IReduce next() {
            // if(!hasNext()) {
            // throw new NoSuchElementException();
            // }
            // return (IReduce) actionsPerCharacterClass.actions.get(index++);
            // }
            // };
        } else
            return Collections.EMPTY_LIST;
    }

}