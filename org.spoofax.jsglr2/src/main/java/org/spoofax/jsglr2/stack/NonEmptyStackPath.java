package org.spoofax.jsglr2.stack;

public class NonEmptyStackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest>
    extends StackPath<StackNode, ParseForest> {

    protected final StackPath<StackNode, ParseForest> tail;
    protected final StackLink<StackNode, ParseForest> link;

    public NonEmptyStackPath(StackLink<StackNode, ParseForest> stackLink, StackPath<StackNode, ParseForest> tail) {
        super(tail.length + 1);
        this.tail = tail;
        this.link = stackLink;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public StackNode head() {
        return this.link.to;
    }

    @Override
    public boolean contains(StackLink<StackNode, ParseForest> link) {
        return this.link == link || (this.tail != null && this.tail.contains(link));
    }

}
