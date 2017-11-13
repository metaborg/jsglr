package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;

public interface IState {

    int stateNumber();

    IAction[] actions();

    boolean isRejectable();

    void markRejectable();

    Iterable<IAction> applicableActions(int character);

    Iterable<IReduce> applicableReduceActions(Parse parse);

    boolean hasGoto(int productionId);

    int getGotoId(int productionId);

}
