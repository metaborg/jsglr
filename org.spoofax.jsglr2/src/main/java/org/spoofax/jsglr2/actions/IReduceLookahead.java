package org.spoofax.jsglr2.actions;

import org.spoofax.jsglr2.parser.Parse;

public interface IReduceLookahead extends IReduce {

    @Override default public ActionType actionType() {
        return ActionType.REDUCE_LOOKAHEAD;
    }

    boolean allowsLookahead(Parse parse);

}
