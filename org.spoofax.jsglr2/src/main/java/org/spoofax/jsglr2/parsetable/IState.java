package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parser.Parse;

import java.util.Optional;

public interface IState {

    int stateNumber();

	IAction[] actions();
    
    boolean isRejectable();
    
    Iterable<IAction> applicableActions(int character);
    
    Iterable<IReduce> applicableReduceActions(Parse parse);

    Optional<Integer> getGotoId(int productionId);

}
