package org.spoofax.jsglr2.testset;

public class TestSetMultipleInputs extends TestSetInput {

    public final String path; // Absolute path to search in
    public final String extension; // Extension for files to find in path

    public TestSetMultipleInputs(String path, String extension) {
        super(Type.MULTIPLE);

        this.path = path;
        this.extension = extension;
    }

}
