package org.spoofax.jsglr2.actions;

public interface IAccept extends IAction {
    
    default public ActionType actionType() {
        return ActionType.ACCEPT;
    }

}
