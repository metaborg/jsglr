package org.spoofax.jsglr2.integrationtest;

import jakarta.annotation.Nullable;

public final class ParseNodeDescriptor {

    public final int offset, width;
    @Nullable public String sort, cons;
    /**
     * Parse node reuse may be different for {@link org.spoofax.jsglr2.parseforest.ParseForestConstruction}.Full or
     * .Optimized. This field is used to control to which variant this descriptor applies. If it is `null`, it applies
     * to both variants.
     */
    @Nullable public final Boolean onlyForFullForest;

    public ParseNodeDescriptor(int offset, int width, @Nullable String sort, @Nullable String cons,
        @Nullable Boolean onlyForFullForest) {
        this.offset = offset;
        this.width = width;
        this.sort = sort;
        this.cons = cons;
        this.onlyForFullForest = onlyForFullForest;
    }

    public ParseNodeDescriptor(int offset, int width, @Nullable String sort, @Nullable String cons) {
        this(offset, width, sort, cons, null);
    }

    @Override public String toString() {
        return "ParseNodeDescriptor{offset=" + offset + ", width=" + width + ", sort='" + sort + '\'' + ", cons='"
            + cons + '\'' + '}';
    }
}
