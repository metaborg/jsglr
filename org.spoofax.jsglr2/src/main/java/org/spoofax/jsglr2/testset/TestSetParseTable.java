package org.spoofax.jsglr2.testset;

public abstract class TestSetParseTable {

    public enum Source {
        ATERM, GRAMMAR_DEF
    }

    public final Source source;

    protected TestSetParseTable(Source source) {
        this.source = source;
    }

}
