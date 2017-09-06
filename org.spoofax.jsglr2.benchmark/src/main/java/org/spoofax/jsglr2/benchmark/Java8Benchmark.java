package org.spoofax.jsglr2.benchmark;

import java.io.IOException;
import java.util.List;

public class Java8Benchmark extends JSGLR2Benchmark {
    
    public Java8Benchmark() {
        super("Java8.tbl");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getMultipleInputs("/path/to/some/java/project", "java");
    }

}
