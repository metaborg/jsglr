package org.spoofax.jsglr2.stack;

public class StackLink<ParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    public final int linkNumber;
    public final StackNode from; // Farthest away from initial stack node
    public final StackNode to; // Closest to initial stack node
    public final ParseForest parseForest;
    private boolean isRejected;

    public StackLink(int linkNumber, StackNode from, StackNode to, ParseForest parseForest) {
        this.linkNumber = linkNumber;
        this.from = from;
        this.to = to;
        this.parseForest = parseForest;
        this.isRejected = false;
    }

    public void reject() {
        this.isRejected = true;
    }

    public boolean isRejected() {
        return this.isRejected;
    }

}
