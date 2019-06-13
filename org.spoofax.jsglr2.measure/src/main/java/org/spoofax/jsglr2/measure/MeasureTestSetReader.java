package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetInput;
import org.spoofax.jsglr2.testset.TestSetReader;
import org.spoofax.jsglr2.testset.TestSetSizedInput;

public class MeasureTestSetReader<Input> extends TestSetReader<Input> {

    public MeasureTestSetReader(TestSet<Input> testSet) {
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

    public Iterable<InputBatch> getInputBatches() throws IOException {
        if(testSet.input.type == TestSetInput.Type.SIZED) {
            TestSetSizedInput<Input> testSizedInput = (TestSetSizedInput<Input>) testSet.input;

            if(testSizedInput.sizes == null)
                throw new IllegalStateException("invalid input type (sizes missing)");

            List<InputBatch> result = new ArrayList<>();

            for(int size : testSizedInput.sizes) {
                result.add(new InputBatch(testSizedInput.getInputs(size), size));
            }

            return result;
        }
        return Collections.singletonList(new InputBatch(testSet.input.getInputs(), -1));
    }

    public class InputBatch {
        public Iterable<Input> inputs;
        public int size;

        public InputBatch(Iterable<Input> inputs, int size) {
            this.inputs = inputs;
            this.size = size;
        }

        public InputBatch(Input input, int size) {
            this.inputs = Collections.singletonList(input);
            this.size = size;
        }
    }

}
