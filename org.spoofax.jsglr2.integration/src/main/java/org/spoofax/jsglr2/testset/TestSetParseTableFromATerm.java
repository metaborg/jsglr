package org.spoofax.jsglr2.testset;

public class TestSetParseTableFromATerm extends TestSetParseTable {

    /**
     * Path of the parse table file (without .tbl extension) in
     * org.spoofax.jsglr2.integration/src/main/resources/parsetables
     */
    public final String name;

    protected TestSetParseTableFromATerm(String name) {
        super(Source.ATERM);

        this.name = name;
    }

}
