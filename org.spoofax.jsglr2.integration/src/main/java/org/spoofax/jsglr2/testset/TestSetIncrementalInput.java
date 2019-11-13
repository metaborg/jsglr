package org.spoofax.jsglr2.testset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.testinput.IncrementalStringInput;

public class TestSetIncrementalInput extends TestSetInput<String[], IncrementalStringInput> {

    // Directory in the org.spoofax.jsglr2.integration/src/main/resources/samples directory, containing files %d.in
    public final String directory;

    public TestSetIncrementalInput(String directory) {
        super(Type.INCREMENTAL);

        this.directory = directory;
    }

    @Override protected IncrementalStringInput getInput(String filename, String[] input) {
        return new IncrementalStringInput(filename, input);
    }

    @Override public List<IncrementalStringInput> getInputs() throws IOException {
        List<String> inputs = new ArrayList<>();
        for(int i = 0;; i++) {
            try {
                inputs.add(getFileAsString(
                    directory + (directory.endsWith(File.separator) ? "" : File.separator) + i + ".in"));
            } catch(NullPointerException | IOException ignored) {
                break;
            }
        }
        return Collections.singletonList(getInput(directory, inputs.toArray(new String[0])));
    }
}
