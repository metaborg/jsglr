package org.spoofax.jsglr2.stack;

public class NonEmptyStackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest> extends StackPath<StackNode, ParseForest> {

	protected final StackPath<StackNode, ParseForest> next;
	protected final StackLink<StackNode, ParseForest> link;
	
	public NonEmptyStackPath(StackLink<StackNode, ParseForest> stackLink) {
		super(1);
		this.next = new EmptyStackPath<StackNode, ParseForest>(stackLink.to);
		this.link = stackLink;
	}
	
	public NonEmptyStackPath(StackLink<StackNode, ParseForest> stackLink, StackPath<StackNode, ParseForest> next) {
		super(next.length + 1);
		this.next = next;
		this.link = stackLink;
	}
    
    public boolean isEmpty() {
        return false;
    }
	
	public StackNode lastStackNode() {
		return this.next.lastStackNode();
	}
	
	public boolean contains(StackLink<StackNode, ParseForest> link) {
		return this.link == link || (this.next != null && this.next.contains(link));
	}

}
