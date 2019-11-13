package org.spoofax.jsglr2.testset;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public abstract class TestSetSingleInput<ContentType, Input extends TestInput<ContentType>>
    extends TestSetInput<ContentType, Input> {

    public final String filename; // Path in the org.spoofax.jsglr2.integration/src/main/resources/samples directory

    public TestSetSingleInput(String filename) {
        super(Type.SINGLE);

        this.filename = filename;
    }

    static class StringInputSet extends TestSetSingleInput<String, StringInput> {
        public StringInputSet(String filename) {
            super(filename);
        }

        @Override public List<StringInput> getInputs() throws IOException {
            return Collections.singletonList(getInput(filename, getFileAsString(filename)));
        }

        @Override protected StringInput getInput(String filename, String input) {
            return new StringInput(filename, input);
        }
    }

    static class IncrementalStringInputSet extends TestSetSingleInput<String[], IncrementalStringInput> {
        public IncrementalStringInputSet(String filename) {
            super(filename);
        }

        @Override public List<IncrementalStringInput> getInputs() throws IOException {
            return Collections.singletonList(getInput(filename, getFileAsString(filename).split("\4")));
        }

        @Override protected IncrementalStringInput getInput(String filename, String[] input) {
            return new IncrementalStringInput(filename, input);
        }
    }
}
