package org.spoofax.jsglr2.stack;

public abstract class StackPath<ParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    public final int length;

    protected StackPath(int length) {
        this.length = length;
    }

    public abstract boolean isEmpty();

    public abstract StackNode head();

    public abstract boolean contains(StackLink<ParseForest, StackNode> link);

}
