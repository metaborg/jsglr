package org.spoofax.jsglr2.stack;

import java.util.Collections;
import java.util.List;

public class EmptyStackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest> extends StackPath<StackNode, ParseForest> {

	protected final int length;
	private final StackNode stackNode;
	
	public EmptyStackPath(StackNode stackNode) {
		this.length = 0;
		this.stackNode = stackNode;
	}
	
	public boolean isEmpty() {
	    return true;
	}
	
	public List<ParseForest> getParseForests() {
		return Collections.emptyList();
	}
	
	public StackNode lastStackNode() {
		return this.stackNode;
	}
	
	public boolean contains(StackLink<StackNode, ParseForest> link) {
		return false;
	}
	
}
