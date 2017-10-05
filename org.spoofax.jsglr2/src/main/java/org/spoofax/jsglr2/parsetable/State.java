package org.spoofax.jsglr2.parsetable;

import java.util.Iterator;

import org.spoofax.jsglr2.actions.ActionType;
import org.spoofax.jsglr2.actions.IAction;
import org.spoofax.jsglr2.actions.IGoto;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.actions.IReduceLookahead;
import org.spoofax.jsglr2.parser.Parse;

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
    
	public Iterable<IAction> applicableActions(int character) {
		return new Iterable<IAction>() {
			public Iterator<IAction> iterator() {
				return new Iterator<IAction>() {
					int i = 0;
					
					public boolean hasNext() {
						if (i < actions.length) {
							if (actions[i].appliesTo(character))
								return true;
							else {
								i++;
								
								return hasNext();
							}
						}
						
						return false;
					}
	
					public IAction next() {
						return actions[i++];
					}
				};
			}
		};
	}
	
	public Iterable<IReduce> applicableReduceActions(Parse parse) {
		return new Iterable<IReduce>() {
			public Iterator<IReduce> iterator() {
				return new Iterator<IReduce>() {
					int i = 0;
					
					public boolean hasNext() {
						if (i < actions.length) {
							if ((actions[i].actionType() == ActionType.REDUCE || actions[i].actionType() == ActionType.REDUCE_LOOKAHEAD && ((IReduceLookahead) actions[i]).allowsLookahead(parse)) && actions[i].appliesTo(parse.currentChar))
								return true;
							else {
								i++;
								
								return hasNext();
							}
						}
						
						return false;
					}
	
					public IReduce next() {
						return (IReduce) actions[i++];
					}
				};
			}
		};
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
