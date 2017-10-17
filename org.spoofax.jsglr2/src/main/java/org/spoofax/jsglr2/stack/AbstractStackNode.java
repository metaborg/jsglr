package org.spoofax.jsglr2.stack;

import org.spoofax.jsglr2.parsetable.IState;

public abstract class AbstractStackNode<ParseForest> {

	public final int stackNumber;
	public final IState state;
	public final int offset;
	
	public AbstractStackNode(int stackNumber, IState state, int offset) {
		this.stackNumber = stackNumber;
        this.state = state;
        this.offset = offset;
	}
    
	// True if non-empty and all out links are rejected
    public abstract boolean allOutLinksRejected();
	
}
