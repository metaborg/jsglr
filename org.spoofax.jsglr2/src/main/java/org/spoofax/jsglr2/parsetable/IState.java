package org.spoofax.jsglr2.parsetable;

import java.util.Optional;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;

public interface IState {

    int stateNumber();

    IAction[] actions();

    boolean isRejectable();

    Iterable<IAction> applicableActions(int character);

    Iterable<IReduce> applicableReduceActions(Parse parse);

    Iterable<IAction> applicableActionsEOF();

    Optional<Integer> getGotoId(int productionId);

}
