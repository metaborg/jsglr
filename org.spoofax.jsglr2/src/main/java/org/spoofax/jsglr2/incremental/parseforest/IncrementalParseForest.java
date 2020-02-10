package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public abstract class IncrementalParseForest implements IParseForest {
    private final int width;

    protected IncrementalParseForest(int width) {
        this.width = width;
    }

    /** Returns whether this parse node is in theory reusable, not taking into account the stack it's shifted onto. */
    public abstract boolean isReusable();

    /** Returns whether this parse node is reusable, taking into account the state of the stack it's shifted onto. */
    public abstract boolean isReusable(IState stackState);

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

    /**
     * Warning: calling this method for every node in the parse tree is very memory-inefficient. If you need parts of
     * the input string, you're better off saving the input string and calling `charAt` or `substring` on that.
     *
     * @return The yield of this parse forest node.
     */
    public abstract String getYield();

    /**
     * Slightly optimized by only traversing the parts of the tree that are necessary to calculate the requested yield.
     *
     * @param length
     *            The length of the string to return.
     * @return The first `length` characters of the yield of this parse forest node.
     */
    public abstract String getYield(int length);
}
