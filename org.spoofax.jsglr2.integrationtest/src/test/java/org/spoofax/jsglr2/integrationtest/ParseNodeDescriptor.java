package org.spoofax.jsglr2.integrationtest;

import javax.annotation.Nullable;

public final class ParseNodeDescriptor {

    public final int offset, width;
    @Nullable public String sort, cons;

    public ParseNodeDescriptor(int offset, int width, @Nullable String sort, @Nullable String cons) {
        this.offset = offset;
        this.width = width;
        this.sort = sort;
        this.cons = cons;
    }

    @Override public String toString() {
        return "ParseNodeDescriptor{offset=" + offset + ", width=" + width + ", sort='" + sort + '\'' + ", cons='"
            + cons + '\'' + '}';
    }
}
