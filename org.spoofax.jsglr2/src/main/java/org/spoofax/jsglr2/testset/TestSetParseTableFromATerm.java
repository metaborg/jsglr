package org.spoofax.jsglr2.testset;

public class TestSetParseTableFromATerm extends TestSetParseTable {

    public final String name; // Path of file (without .tbl extension) in
                              // org.spoofax.jsglr2/src/test/resources/parsetable

    protected TestSetParseTableFromATerm(String name) {
        super(Source.ATERM);

        this.name = name;
    }

}
