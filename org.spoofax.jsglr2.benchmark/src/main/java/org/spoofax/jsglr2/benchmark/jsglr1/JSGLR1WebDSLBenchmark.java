package org.spoofax.jsglr2.benchmark.jsglr1;

import java.io.IOException;
import java.util.List;

public class JSGLR1WebDSLBenchmark extends JSGLR1ParseTableBenchmark {
    
    public JSGLR1WebDSLBenchmark() {
        super("WebDSL");
    }
    
    protected List<Input> getInputs() throws IOException {
        return getSingleInput("WebDSL/built-in.app");
    }

}
