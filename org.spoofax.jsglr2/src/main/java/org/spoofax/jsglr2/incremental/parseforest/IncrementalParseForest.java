package org.spoofax.jsglr2.incremental.parseforest;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public abstract class IncrementalParseForest implements IParseForest {
    private final int width;

    protected IncrementalParseForest(int width) {
        super();
        this.width = width;
    }

    public abstract boolean isTerminal();

    @Override public int width() {
        return width;
    }

    @Override public String toString() {
        TreePrettyPrinter printer = new TreePrettyPrinter();

        prettyPrint(printer);

        return printer.get().trim();
    }

    abstract protected void prettyPrint(TreePrettyPrinter printer);

    public abstract String getYield();

    public abstract String getYield(int length);
}
