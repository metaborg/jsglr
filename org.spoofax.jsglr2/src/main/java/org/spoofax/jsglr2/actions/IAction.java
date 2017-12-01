package org.spoofax.jsglr2.actions;

public interface IAction {

    static IActionsFactory factory() {
        return new ActionsFactory(true);
    }

    ActionType actionType();

}
