package org.spoofax.jsglr2.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NonEmptyStackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest> extends StackPath<StackNode, ParseForest> {

	protected final int length;
	protected final StackPath<StackNode, ParseForest> next;
	protected final StackLink<StackNode, ParseForest> link;
	
	public NonEmptyStackPath(StackLink<StackNode, ParseForest> stackLink) {
		this.length = 1;
		this.next = new EmptyStackPath<StackNode, ParseForest>(stackLink.to);
		this.link = stackLink;
	}
	
	public NonEmptyStackPath(StackLink<StackNode, ParseForest> stackLink, StackPath<StackNode, ParseForest> next) {
		this.length = next.length + 1;
		this.next = next;
		this.link = stackLink;
	}
    
    public boolean isEmpty() {
        return false;
    }
	
	public List<ParseForest> getParseForests() {
		List<ParseForest> res = new ArrayList<ParseForest>(length);
		
		for (StackPath<StackNode, ParseForest> path = this; !path.isEmpty(); path = ((NonEmptyStackPath<StackNode, ParseForest>) path).next)
            res.add(((NonEmptyStackPath<StackNode, ParseForest>) path).link.parseForest);
		
		Collections.reverse(res);
		
		return res;
	}
	
	public StackNode lastStackNode() {
		return this.next.lastStackNode();
	}
	
	public boolean contains(StackLink<StackNode, ParseForest> link) {
		return this.link == link || (this.next != null && this.next.contains(link));
	}

}
