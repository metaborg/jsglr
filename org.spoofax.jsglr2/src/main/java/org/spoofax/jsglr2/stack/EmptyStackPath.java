package org.spoofax.jsglr2.stack;

public class EmptyStackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest>
    extends StackPath<StackNode, ParseForest> {

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
    public boolean contains(StackLink<StackNode, ParseForest> link) {
        return false;
    }

}
