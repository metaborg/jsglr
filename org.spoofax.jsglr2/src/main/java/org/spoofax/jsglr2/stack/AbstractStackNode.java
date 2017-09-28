package org.spoofax.jsglr2.stack;

import org.spoofax.jsglr2.parsetable.IState;

public abstract class AbstractStackNode<ParseForest> {

	public final int stackNumber;
	public final IState state;
	
	public AbstractStackNode(int stackNumber, IState state) {
		this.stackNumber = stackNumber;
        this.state = state;
	}
    
    public abstract boolean allLinksRejected();
	
}
