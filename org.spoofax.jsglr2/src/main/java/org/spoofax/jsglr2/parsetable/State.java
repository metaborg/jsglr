package org.spoofax.jsglr2.parsetable;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;

public class State implements IState {
	
	private final int stateNumber;
	private final IGoto[] gotos;
	private final IAction[] actions;
	private boolean rejectable;
	
	public State(int stateNumber, IGoto[] gotos, IAction[] actions) {
		this.stateNumber = stateNumber;
		this.gotos = gotos;
        this.actions = actions;
        this.rejectable = false;
	}
	
	public int stateNumber() {
	    return stateNumber;
	}
	
	public IGoto[] gotos() {
	    return gotos;
	}
    
	public IAction[] actions() {
	    return actions;
	}
    
    public boolean isRejectable() {
        return rejectable;
    }
    
    public void markRejectable() {
        this.rejectable = true;
    }
	
	public List<IAction> applicableActions(int character) {
		List<IAction> res = new ArrayList<IAction>();
		
		for (IAction action : actions) {
			if (action.appliesTo(character))
				res.add(action);
		}
		
		return res;
	}
	
	public List<IReduce> applicableReduceActions(int character) {
		List<IReduce> res = new ArrayList<IReduce>();
		
		for (IAction action : actions) {
			if (action.actionType() == ActionType.REDUCE && action.appliesTo(character)) // TODO: handle reduces with lookahead
				res.add((IReduce) action);
		}
		
		return res;
	}
	
	public IGoto getGoto(int production) {
		for (IGoto gotoAction : gotos) {
			for (int otherProduction : gotoAction.productions())
				if (production == otherProduction)
					return gotoAction;
		}
		
		return null;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof State))
			return false;
		
		State otherState = (State) obj;
		
		return this.stateNumber == otherState.stateNumber;
	}

}
