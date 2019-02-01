package org.spoofax.jsglr2.testset;

public class TestSetParseTableFromSDF3 extends TestSetParseTable {

    public final String name; // Path of SDF3 file

    protected TestSetParseTableFromSDF3(String name) {
        super(Source.SDF3);

        this.name = name;
    }

}
