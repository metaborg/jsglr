package org.spoofax.jsglr2.stack.paths;

import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class EmptyStackPath<ParseForest, StackNode extends AbstractStackNode<ParseForest>>
    extends StackPath<ParseForest, StackNode> {

    private final StackNode stackNode;

    public EmptyStackPath(StackNode stackNode) {
        super(0);
        this.stackNode = stackNode;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public StackNode head() {
        return this.stackNode;
    }

    @Override
    public boolean contains(StackLink<ParseForest, StackNode> link) {
        return false;
    }

}
