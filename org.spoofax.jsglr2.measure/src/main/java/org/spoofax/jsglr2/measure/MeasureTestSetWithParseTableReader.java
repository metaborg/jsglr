package org.spoofax.jsglr2.measure;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Stream;

import org.spoofax.jsglr2.testset.TestSetInput;
import org.spoofax.jsglr2.testset.TestSetSizedInput;
import org.spoofax.jsglr2.testset.TestSetWithParseTable;
import org.spoofax.jsglr2.testset.TestSetWithParseTableReader;
import org.spoofax.jsglr2.testset.testinput.TestInput;

public class MeasureTestSetWithParseTableReader<ContentType, Input extends TestInput<ContentType>>
    extends TestSetWithParseTableReader<ContentType, Input> {

    MeasureTestSetWithParseTableReader(TestSetWithParseTable<ContentType, Input> testSet) {
        super(testSet);
    }

    @Override protected String basePath() {
        try {
            URL url = MeasureTestSetWithParseTableReader.class.getProtectionDomain().getCodeSource().getLocation();
            String path = url.toURI().getPath();

            return new File(path).getParent() + "/classes/";
        } catch(URISyntaxException e) {
            throw new IllegalStateException("base path for measurements could not be retrieved");
        }
    }

    public Stream<InputBatch> getInputBatches() throws IOException {
        if(testSet.input.type == TestSetInput.Type.SIZED) {
            TestSetSizedInput<ContentType, Input> sizedInput = (TestSetSizedInput<ContentType, Input>) testSet.input;

            if(sizedInput.sizes == null)
                throw new IllegalStateException("invalid input type (sizes missing)");

            return Arrays.stream(sizedInput.sizes).mapToObj(size -> new InputBatch(sizedInput.getInputs(size), size));
        } else
            return Stream.of(new InputBatch(testSet.input.getInputs(), -1));
    }

    public class InputBatch {
        public Iterable<Input> inputs;
        public int size;

        InputBatch(Iterable<Input> inputs, int size) {
            this.inputs = inputs;
            this.size = size;
        }
    }

}
