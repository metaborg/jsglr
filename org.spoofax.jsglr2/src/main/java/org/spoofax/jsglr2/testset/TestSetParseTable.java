package org.spoofax.jsglr2.testset;

public abstract class TestSetParseTable {

    public enum Source {
        ATERM, SDF3
    }

    public final Source source;

    protected TestSetParseTable(Source source) {
        this.source = source;
    }

}
