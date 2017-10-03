package org.spoofax.jsglr2.stack;

public abstract class StackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest> {
	
	public final int length;
	
	protected StackPath(int length) {
		this.length = length;
	}
    
    public abstract boolean isEmpty();
	
	public abstract StackNode lastStackNode();
	
	public abstract boolean contains(StackLink<StackNode, ParseForest> link);

}
