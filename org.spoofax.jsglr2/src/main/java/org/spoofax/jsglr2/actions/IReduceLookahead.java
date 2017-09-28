package org.spoofax.jsglr2.actions;

public interface IReduceLookahead extends IReduce {
    
    default public ActionType actionType() {
        return ActionType.REDUCE_LOOKAHEAD;
    }
    
    boolean allowsLookahead(String lookahead);

}
