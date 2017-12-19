package org.spoofax.jsglr2.actions;

public interface IShift extends IAction {

    @Override
    default public ActionType actionType() {
        return ActionType.SHIFT;
    }

    int shiftStateId();

}
