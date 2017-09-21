package org.spoofax.jsglr2.parsetable;

import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;

public interface IState {

    int stateNumber();
    
    IGoto[] gotos();
    
	IAction[] actions();
    
    boolean isRejectable();
    
    Iterable<IAction> applicableActions(int character);
    
    Iterable<IReduce> applicableReduceActions(int character);
    
    IGoto getGoto(int productionNumber);
    
}
