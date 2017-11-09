package org.spoofax.jsglr2.stack.elkhound;

import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;

public class DeterministicStackPath<StackNode extends AbstractStackNode<ParseForest>, ParseForest>
    extends StackPath<StackNode, ParseForest> {

    public final ParseForest[] parseForests;
    private final StackNode head;

    public DeterministicStackPath(ParseForest[] parseForests, StackNode head) {
        super(parseForests != null ? parseForests.length : 0);
        this.parseForests = parseForests;
        this.head = head;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public StackNode head() {
        return head;
    }

    @Override
    public boolean contains(StackLink<StackNode, ParseForest> link) {
        // We can (possibly incorrect) return false here since this method is only called in a non-LR context and this
        // type of path is only used in the LR/Elkhound context
        return false;
    }

}
