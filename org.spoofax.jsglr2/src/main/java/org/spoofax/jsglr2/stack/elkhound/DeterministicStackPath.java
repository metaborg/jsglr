package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;

public class DeterministicStackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest> extends StackPath<StackNode, ParseForest> {

	public final ParseForest[] parseForests;
	private final StackNode lastStackNode;
	
	public DeterministicStackPath(ParseForest[] parseForests, StackNode lastStackNode) {
		super(parseForests.length);
		this.parseForests = parseForests;
		this.lastStackNode = lastStackNode;
	}
    
    public boolean isEmpty() {
        return length == 0;
    }
	
	public StackNode lastStackNode() {
		return lastStackNode;
	}
	
	public boolean contains(StackLink<StackNode, ParseForest> link) {
		// We can (possibly incorrect) return false here since this method is only called in a non-LR context and this type of path is only used in the LR/Elkhound context
		return false; 
	}

}
