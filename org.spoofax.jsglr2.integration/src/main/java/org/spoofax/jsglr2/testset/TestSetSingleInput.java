package org.spoofax.jsglr2.testset;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;
import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public abstract class TestSetSingleInput<ContentType, Input extends TestInput<ContentType>>
    extends TestSetInput<ContentType, Input> {

    // Path in org.spoofax.jsglr2.integration/src/main/resources/samples directory (if internal) or an absolute path (if
    // not internal)
    public final String filename;

    public TestSetSingleInput(String filename) {
        this(filename, true);
    }

    public TestSetSingleInput(String filename, boolean internal) {
        super(Type.SINGLE, internal);

        this.filename = filename;
    }

    static class StringInputSet extends TestSetSingleInput<String, StringInput> {
        public StringInputSet(String filename, boolean internal) {
            super(filename, internal);
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
