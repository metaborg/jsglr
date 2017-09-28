package org.spoofax.jsglr2.stack;

import java.util.List;

public abstract class StackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest> {
	
	protected int length;
    
    public abstract boolean isEmpty();
	
	public abstract List<ParseForest> getParseForests();
	
	public abstract StackNode lastStackNode();
	
	public abstract boolean contains(StackLink<StackNode, ParseForest> link);

}
