package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.jsglr2.testset.*;

public class MeasureTestSetReader extends TestSetReader<StringInput> {

    public MeasureTestSetReader(TestSet testSet) {
        super(testSet);
    }

    @Override protected String basePath() {
        try {
            return new File(
                MeasureTestSetReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getParent()
                + "/classes/";
        } catch(URISyntaxException e) {
            throw new IllegalStateException("base path for measurements could not be retrieved");
        }
    }

    @Override public InputStream resourceInputStream(String resource) throws Exception {
        return new FileInputStream(new File(basePath() + resource));
    }

    @Override protected String getFileAsString(String filename) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/samples/" + filename);

        return inputStreamAsString(inputStream);
    }

    @Override protected StringInput getInput(String filename, String input) {
        return new StringInput(filename, input);
    }

    public Iterable<InputBatch> getInputBatches() throws IOException {
        switch(testSet.input.type) {
            case SINGLE:
                TestSetSingleInput testSetSingleInput = (TestSetSingleInput) testSet.input;

                return Arrays.asList(new InputBatch(getSingleInput(testSetSingleInput.filename), -1));
            case MULTIPLE:
                TestSetMultipleInputs testSetMultipleInputs = (TestSetMultipleInputs) testSet.input;

                return Arrays.asList(
                    new InputBatch(getMultipleInputs(testSetMultipleInputs.path, testSetMultipleInputs.extension), -1));
            case SIZED:
                TestSetSizedInput testSizedInput = (TestSetSizedInput) testSet.input;

                if(testSizedInput.sizes == null)
                    throw new IllegalStateException("invalid input type (sizes missing)");

                List<InputBatch> result = new ArrayList<>();

                for(int size : testSizedInput.sizes) {
                    result.add(new InputBatch(new StringInput("", testSizedInput.get(size)), size));
                }

                return result;
            default:
                throw new IllegalStateException("invalid input type (does have a size)");
        }
    }

    public class InputBatch {
        public Iterable<StringInput> inputs;
        public int size;

        public InputBatch(Iterable<StringInput> inputs, int size) {
            this.inputs = inputs;
            this.size = size;
        }

        public InputBatch(StringInput input, int size) {
            this.inputs = Arrays.asList(input);
            this.size = size;
        }
    }

}
