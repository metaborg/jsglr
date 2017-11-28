package org.spoofax.jsglr2.actions;

public interface IAction {

    static IActionsFactory factory() {
        return new ActionsFactory(true);
    }

    ActionType actionType();

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
