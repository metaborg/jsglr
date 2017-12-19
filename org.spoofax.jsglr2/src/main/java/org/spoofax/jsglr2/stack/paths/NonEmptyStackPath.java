package org.spoofax.jsglr2.stack.paths;

import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class NonEmptyStackPath<ParseForest, StackNode extends AbstractStackNode<ParseForest>>
    extends StackPath<ParseForest, StackNode> {

    public final StackPath<ParseForest, StackNode> tail;
    public final StackLink<ParseForest, StackNode> link;

    public NonEmptyStackPath(StackLink<ParseForest, StackNode> stackLink, StackPath<ParseForest, StackNode> tail) {
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
    public boolean contains(StackLink<ParseForest, StackNode> link) {
        return this.link == link || (this.tail != null && this.tail.contains(link));
    }

}
