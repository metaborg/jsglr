package org.spoofax.jsglr2.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.jsglr2.characterclasses.ICharacterClass;
import org.spoofax.jsglr2.parser.Parse;

public final class ActionsPerCharacterClass {

    public final ICharacterClass characterClass;
    public final List<IAction> actions;

    public ActionsPerCharacterClass(ICharacterClass characterClass, IAction[] actions) {
        this.characterClass = characterClass;
        this.actions = Arrays.asList(actions);
    }

    public final boolean appliesTo(int character) {
        return characterClass.contains(character);
    }

    @Override public String toString() {
        return characterClass.toString() + "->" + actions.toString();
    }

    public final List<IReduce> getReduceActions(Parse parse) {
        List<IReduce> res = new ArrayList<>();

        for(IAction action : actions)
            if(action.actionType() == ActionType.REDUCE || (action.actionType() == ActionType.REDUCE_LOOKAHEAD
                && ((IReduceLookahead) action).allowsLookahead(parse)))
                res.add((IReduce) action);

        return res;
    }

}
