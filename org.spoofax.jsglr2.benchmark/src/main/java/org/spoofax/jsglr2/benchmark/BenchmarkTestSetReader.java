package org.spoofax.jsglr2.benchmark;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.spoofax.jsglr2.testset.TestSet;
import org.spoofax.jsglr2.testset.TestSetReader;

public abstract class BenchmarkTestSetReader<Input> extends TestSetReader<Input> {

    public BenchmarkTestSetReader(TestSet testSet) {
        super(testSet);
    }

    @Override protected String basePath() {
        try {
            return new File(
                BenchmarkTestSetReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getParent()
                + "/classes/";
        } catch(URISyntaxException e) {
            throw new IllegalStateException("base path for benchmarks could not be retrieved");
        }
    }

    @Override public InputStream resourceInputStream(String resource) throws Exception {
        return new FileInputStream(new File(basePath() + resource));
    }

    @Override protected String getFileAsString(String filename) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/samples/" + filename);

        return inputStreamAsString(inputStream);
    }

}
