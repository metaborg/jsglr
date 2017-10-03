package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.util.List;

public class JSGLR1Java8Benchmark extends JSGLR1ParseTableBenchmark {
    
    public JSGLR1Java8Benchmark() {
        super("Java8");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getMultipleInputs("/path/to/some/java/project", "java");
    }

}
