package org.spoofax.jsglr2.testset;

public class TestSetSingleInput extends TestSetInput {

    public final String filename; // Path in the org.spoofax.jsglr2/src/test/resources/samples directory

    public TestSetSingleInput(String filename) {
        super(Type.SINGLE);

        this.filename = filename;
    }

}
