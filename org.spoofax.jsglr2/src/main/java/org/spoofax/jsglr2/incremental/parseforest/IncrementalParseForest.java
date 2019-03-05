package org.spoofax.jsglr2.incremental.parseforest;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public abstract class IncrementalParseForest implements IParseForest {
    private final int width;
    public IncrementalDerivation parent;
    public int childIndex = -1;

    protected IncrementalParseForest(int width) {
        super();
        this.width = width;
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
        if(parent == null)
            return IncrementalCharacterNode.EOF_NODE;
        else if(childIndex + 1 == parent.parseForests().length)
            return null;
        else
            return parent.parseForests()[childIndex + 1];
    }

    @Override public int width() {
        return width;
    }

    @Override public String toString() {
        TreePrettyPrinter printer = new TreePrettyPrinter();

        prettyPrint(printer);

        return printer.get();
    }

    abstract protected void prettyPrint(TreePrettyPrinter printer);

    public abstract String getSource();
}
