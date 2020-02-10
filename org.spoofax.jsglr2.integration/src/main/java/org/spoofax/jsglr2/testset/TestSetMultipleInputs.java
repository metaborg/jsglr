package org.spoofax.jsglr2.testset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spoofax.jsglr2.testset.testinput.StringInput;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public abstract class TestSetMultipleInputs<Input extends TestInput<String>> extends TestSetInput<String, Input> {

    public final String path; // Absolute path to search in
    public final String extension; // Extension for files to find in path

    public TestSetMultipleInputs(String path, String extension) {
        super(Type.MULTIPLE, false);

        this.path = path;
        this.extension = extension;
    }

    @Override public List<Input> getInputs() throws IOException {
        List<Input> inputs = new ArrayList<>();

        for(File file : filesInPath(new File(path))) {
            String filename = file.getName();
            if(filename.endsWith("." + extension)) {
                String input = inputStreamAsString(new FileInputStream(file));

                inputs.add(getInput(filename, input));
            }
        }

        return inputs;
    }

    private Set<File> filesInPath(File path) {
        Set<File> acc = new HashSet<>();

        filesInPath(path, acc);

        return acc;
    }

    private void filesInPath(final File path, Set<File> acc) {
        File[] files = path.listFiles();
        if(files == null)
            throw new IllegalStateException("Directory " + path + " not found!");
        for(final File subPath : files) {
            if(subPath.isDirectory())
                filesInPath(subPath, acc);
            else
                acc.add(subPath);
        }
    }

    public static class StringInputSet extends TestSetMultipleInputs<StringInput> {
        public StringInputSet(String path, String extension) {
            super(path, extension);
        }

        @Override protected StringInput getInput(String filename, String input) {
            return new StringInput(filename, input);
        }
    }

}
