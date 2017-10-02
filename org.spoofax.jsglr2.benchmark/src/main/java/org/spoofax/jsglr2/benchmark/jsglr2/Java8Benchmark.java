package org.spoofax.jsglr2.benchmark.jsglr2;

import java.io.IOException;
import java.util.List;

public class Java8Benchmark extends JSGLR2ParseTableBenchmark {
    
    public Java8Benchmark() {
        super("Java8");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getMultipleInputs("/path/to/some/java/project", "java");
    }

}
