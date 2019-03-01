package org.spoofax.jsglr2.incremental.parseforest;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public abstract class IncrementalParseForest extends AbstractParseForest {
    private final Position extent;
    public IncrementalDerivation parent;
    public int childIndex = -1;

    protected IncrementalParseForest(Position extent) {
        super(null, null);
        this.extent = extent;
    }

    @Override public Position getStartPosition() {
        if(parent == null)
            return new Position(0, 1, 1);
        Position result = parent.parent.getStartPosition();
        assert (childIndex != -1);
        for(int i = 0; i < childIndex; ++i) {
            if(parent.parseForests[i] != null) // Parse forests can be null if layout is skipped
                result = result.add(parent.parseForests[i].extent);
        }

        return result;
    }

    @Override public Position getEndPosition() {
        // Explicitly add one position to the extent, as the EndPosition is assumed to be exclusive in the Imploder.
        return getStartPosition().add(extent).add(new Position(2, 1, 2));
    }

    public boolean isTerminal() {
        return this instanceof IncrementalCharacterNode;
    }

    public boolean isFragile() {
        return true; // TODO Right now, everything is fragile :D
    }

    public IncrementalParseForest leftBreakdown() {
        return this; // Default implementation for non-breakdown-able parse forests (like character nodes)
    }

    public IncrementalParseForest popLookAhead() {
        if(this == IncrementalCharacterNode.EOF_NODE)
            return null; // cannot pop lookahead if lookahead == eos
        IncrementalParseForest res = this;
        while(res.rightSibling() == null)
            res = res.parent.parent;
        IncrementalParseForest result = res.rightSibling(); // from previous version?
        return result.isFragile() ? result.leftBreakdown() : result;
    }

    public IncrementalParseForest rightSibling() {
        if(parent == null || childIndex + 1 == parent.parseForests().length) {
            return IncrementalCharacterNode.EOF_NODE;
        } else
            return parent.parseForests()[childIndex + 1];
    }

    public Position getExtent() {
        return extent;
    }

    @Override public String toString() {
        TreePrettyPrinter printer = new TreePrettyPrinter();

        prettyPrint(printer);

        return printer.get();
    }

    abstract protected void prettyPrint(TreePrettyPrinter printer);

    public abstract String getSource();
}
