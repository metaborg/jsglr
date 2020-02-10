package org.spoofax.jsglr2.testset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.spoofax.jsglr2.testset.testinput.TestInput;

public abstract class TestSetReader<ContentType, Input extends TestInput<ContentType>> {

    protected final TestSet<ContentType, Input> testSet;

    protected TestSetReader(TestSet<ContentType, Input> testSet) {
        this.testSet = testSet;
    }

    protected abstract String basePath();

    public InputStream resourceInputStream(String resource) throws Exception {
        return new FileInputStream(new File(basePath() + resource));
    }

    public Iterable<Input> getInputs() throws IOException {
        return testSet.input.getInputs();
    }

    public Iterable<Input> getInputsForSize(int n) {
        if(testSet.input.type == TestSetInput.Type.SIZED) {
            return ((TestSetSizedInput<ContentType, Input>) testSet.input).getInputs(n);
        }
        throw new IllegalStateException("invalid input type (test set input should have a size)");
    }

}
