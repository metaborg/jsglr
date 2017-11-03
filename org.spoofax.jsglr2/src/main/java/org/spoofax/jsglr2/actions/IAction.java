package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.characters.ICharacters;

public interface IAction {

    ActionType actionType();

    ICharacters characters();

    public default boolean appliesTo(int character) {
        return characters().containsCharacter(character);
    }

    static boolean typeMatchesShift(IAction action) {
        return action.actionType() == ActionType.SHIFT;
    }

    static boolean typeMatchesReduce(IAction action) {
        return action.actionType() == ActionType.REDUCE;
    }

    static boolean typeMatchesReduceLookahead(IAction action) {
        return action.actionType() == ActionType.REDUCE_LOOKAHEAD;
    }

    static boolean typeMatchesReduceOrReduceLookahead(IAction action) {
        return action.actionType() == ActionType.REDUCE || action.actionType() == ActionType.REDUCE_LOOKAHEAD;
    }

    static boolean typeMatchesAccept(IAction action) {
        return action.actionType() == ActionType.ACCEPT;
    }

}
