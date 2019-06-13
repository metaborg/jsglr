package org.spoofax.jsglr2.testset;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public abstract class TestSetSingleInput<Input extends TestInput<String>> extends TestSetInput<String, Input> {

    public final String filename; // Path in the org.spoofax.jsglr2.integration/src/main/resources/samples directory

    public TestSetSingleInput(String filename) {
        super(Type.SINGLE);

        this.filename = filename;
    }

    @Override public List<Input> getInputs() throws IOException {
        return Collections.singletonList(getInput(filename, getFileAsString(filename)));
    }

    static class StringInputSet extends TestSetSingleInput<StringInput> {
        public StringInputSet(String filename) {
            super(filename);
        }

        @Override protected StringInput getInput(String filename, String input) {
            return new StringInput(filename, input);
        }
    }

}
