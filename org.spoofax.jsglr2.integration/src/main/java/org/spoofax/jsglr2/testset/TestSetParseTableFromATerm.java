package org.spoofax.jsglr2.testset;

public class TestSetParseTableFromATerm extends TestSetParseTable {

    public final String file;
    public final boolean internal;

    public TestSetParseTableFromATerm(String file, boolean internal) {
        super(Source.ATERM);

        this.file = file;
        this.internal = internal;
    }

}
