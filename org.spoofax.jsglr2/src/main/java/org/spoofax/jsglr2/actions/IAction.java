package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.parser.IParseInput;

public interface IAction {

    static IActionsFactory factory() {
        return new ActionsFactory(true);
    }

    ActionType actionType();

    static boolean allowsLookahead(IAction action, IParseInput parseInput) {
        return action.actionType() != ActionType.REDUCE_LOOKAHEAD
            || ((IReduceLookahead) action).allowsLookahead(parseInput);
    }

    static boolean isApplicableReduce(IAction action, IParseInput parseInput) {
        return action.actionType() == ActionType.REDUCE || (action.actionType() == ActionType.REDUCE_LOOKAHEAD
            && ((IReduceLookahead) action).allowsLookahead(parseInput));
    }

}
