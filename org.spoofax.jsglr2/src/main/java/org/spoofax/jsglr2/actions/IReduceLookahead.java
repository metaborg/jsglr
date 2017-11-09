package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.parser.Parse;

public interface IReduceLookahead extends IReduce {

    default public ActionType actionType() {
        return ActionType.REDUCE_LOOKAHEAD;
    }

    boolean allowsLookahead(String lookahead);

    boolean allowsLookahead(Parse parse);

}
