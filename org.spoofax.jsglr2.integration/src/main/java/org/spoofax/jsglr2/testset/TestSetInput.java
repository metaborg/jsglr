package org.spoofax.jsglr2.testset;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public abstract class TestSetInput<Input> {

    public enum Type {
        SINGLE, // A single file from the org.spoofax.jsglr2.integration samples resources directory
        MULTIPLE, // All files with a certain extension from an absolute path
        SIZED // Custom input string of dynamic size based on given argument
    }

    public final Type type;

    protected abstract Input getInput(String filename, String input);

    public abstract List<Input> getInputs() throws IOException;

    protected TestSetInput(Type type) {
        this.type = type;
    }

    protected String getFileAsString(String filename) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/samples/" + filename);

        return inputStreamAsString(inputStream);
    }

    protected String inputStreamAsString(InputStream inputStream) throws IOException {
        try(Scanner s = new Scanner(inputStream)) {
            s.useDelimiter("\\A");

            return s.hasNext() ? s.next() : "";
        }
    }

}
